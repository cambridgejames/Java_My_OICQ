package cn.compscosys.frame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import cn.compscosys.extend.balloontip.BalloonTip;
import cn.compscosys.extend.scrollbarui.DemoScrollBarUI;
import cn.compscosys.net.Message;
import cn.compscosys.net.PostThreadHost;
import cn.compscosys.tray.SystemTrayHost;
import cn.compscosys.ui.JBubble;
import cn.compscosys.ui.JButtonPlus;
import cn.compscosys.ui.JMemberBar;
import cn.compscosys.ui.JNoticeBar;

public class JMyChatDlg extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1092937505282216621L;
	
	private static final Image functionList = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/main_frame_function_list.png")).getImage();
	
	private int xOld = 0;		// Used to handle mouse drag events.
	private int yOld = 0;

	private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
	private long oldCurrentTime = 0;
	
	private String multicastAddress;
	private String nickname;
	private ImageIcon headPortrait = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/head_portrait_default.png"));
	private JBubble.BubbleStyle bubbleStyle = JBubble.BubbleStyle.DEFAULT_BUBBLE;
	
	private JButtonPlus mainSettings;
	
	private JButton topMenu;
	private JButton minUp;
	private JButton maxUp;
	private JButton closeUp;
	private JButton closeDown;
	private JButton send;
	private JButton sendMethod;

	private StyledDocument styledDoc = new DefaultStyledDocument();
	private JTextPane recordPane = new JTextPane(styledDoc);
	private JScrollPane recordScrollPane = new JScrollPane(recordPane);
	private JPanel recordPanel = new JPanel();
	
	private JTextArea message = new JTextArea();
	private JScrollPane messageScroll = new JScrollPane(message);
	
	private JNoticeBar noticebar = new JNoticeBar(162);
	private JMemberBar memberbar = new JMemberBar(428);
	
	private PostThreadHost postThreadHost;
	private SystemTrayHost systemTrayHost;
	
	private ImageIcon defaultIcon = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/systemtray/system_tray_icon_default.png"));
	private ImageIcon blingblingIcon = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/systemtray/system_tray_icon_blingbling.gif"));
	
	private BalloonTip balloonTipUp;
	private BalloonTip balloonTipDown;
	
	public JMyChatDlg(String multicastAddress, String nickname, final SystemTrayHost systemTrayHost) {
		this.multicastAddress = multicastAddress;
		this.nickname = nickname;
		this.systemTrayHost = systemTrayHost;
		
		init();
		
		this.setTitle("当前组：" + multicastAddress);
		this.setSize(891, 683);					// Set width and height of window.
		this.setLocationRelativeTo(null);		// Display the window in center.
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setUndecorated(true);
		this.addWindowListener( new WindowAdapter() { public void windowClosing(WindowEvent we) { System.exit(0); } } );
		
		this.setIconImage(new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/main_frame_icon.png")).getImage());
		
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				xOld = e.getX();		//记录鼠标按下时的坐标
				yOld = e.getY();
			}
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (yOld > 37) return;
				int xOnScreen = e.getXOnScreen();
				int yOnScreen = e.getYOnScreen();
				int xx = xOnScreen - xOld;
				int yy = yOnScreen - yOld;
				setLocation(xx, yy);	//设置拖拽后，窗口的位置
			}
		});
		
		this.addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
				systemTrayHost.setSystemTray(null, defaultIcon.getImage(), null);
			}
			public void windowLostFocus(WindowEvent e) {}
		});
		
		try {
			postThreadHost = new PostThreadHost(this);
			postThreadHost.connect(multicastAddress);
		} catch (Exception e1) {
			setBalloonUp("加入多播组失败！请检查您的网络设置");
			mainSettings.setText("无组播", nickname);
			postThreadHost = null;
		}

		createStyle("currentTime", styledDoc, 12, false, false, Color.GRAY, StyleConstants.ALIGN_CENTER);
		createStyle("postTitle", styledDoc, 16, false, true, Color.GREEN, StyleConstants.ALIGN_RIGHT);
		createStyle("postMessage", styledDoc, 16, false, false, Color.DARK_GRAY, StyleConstants.ALIGN_RIGHT);
		createStyle("receiveTitle", styledDoc, 16, false, true, Color.BLUE, StyleConstants.ALIGN_LEFT);
		createStyle("receiveMessage", styledDoc, 16, false, false, Color.DARK_GRAY, StyleConstants.ALIGN_LEFT);
	}
	
	private void init() {
		// Set background image for main frame.
		JPanel imagePanel = new JPanel() {
			private static final long serialVersionUID = 6340741453902897509L;
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon ii = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/main_frame_background_default.png"));
				g.drawImage(ii.getImage(), 0, 0, getWidth(), getHeight(), ii.getImageObserver());
			}
		};
		add(imagePanel);
		
		mainSettings = new JButtonPlus(multicastAddress, nickname);

		ImageIcon topMenuDefault = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/top_method_default.png"));
		ImageIcon topMenuHover = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/top_method_hover.png"));
		ImageIcon topMenuClicked = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/top_method_clicked.png"));
		topMenu = buttonInit(topMenuDefault, topMenuHover, topMenuClicked);

		ImageIcon minUpDefault = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/min_up_default.png"));
		ImageIcon minUpHover = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/min_up_hover.png"));
		ImageIcon minUpClicked = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/min_up_clicked.png"));
		minUp = buttonInit(minUpDefault, minUpHover, minUpClicked);

		ImageIcon maxUpDefault = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/max_up_default.png"));
		ImageIcon maxUpHover = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/max_up_hover.png"));
		ImageIcon maxUpClicked = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/max_up_clicked.png"));
		maxUp = buttonInit(maxUpDefault, maxUpHover, maxUpClicked);

		ImageIcon closeUpDefault = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/close_up_default.png"));
		ImageIcon closeUpHover = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/close_up_hover.png"));
		ImageIcon closeUpClicked = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/close_up_clicked.png"));
		closeUp = buttonInit(closeUpDefault, closeUpHover, closeUpClicked);

		ImageIcon closeDownDefault = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/close_down_default.png"));
		ImageIcon closeDownClicked = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/close_down_clicked.png"));
		closeDown = buttonInit(closeDownDefault, closeDownDefault, closeDownClicked);

		ImageIcon sendDefault = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/send_default.png"));
		ImageIcon sendHover = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/send_hover.png"));
		ImageIcon sendClicked = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/send_clicked.png"));
		send = buttonInit(sendDefault, sendHover, sendClicked);

		ImageIcon sendMethodDefault = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/send_method_default.png"));
		ImageIcon sendMethodHover = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/send_method_hover.png"));
		ImageIcon sendMethodClicked = new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/frameui/send_method_clicked.png"));
		sendMethod = buttonInit(sendMethodDefault, sendMethodHover, sendMethodClicked);
		
		mainSettings.addActionListener(this);
		topMenu.addActionListener(this);
		minUp.addActionListener(this);
		maxUp.addActionListener(this);
		closeUp.addActionListener(this);
		closeDown.addActionListener(this);
		send.addActionListener(this);
		sendMethod.addActionListener(this);
		
		MouseAdapter changeCursor = new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				JButtonPlus _button = (JButtonPlus) e.getSource();
				_button.setText(_button.getText(), true);
			}
			public void mouseExited(MouseEvent e) {
				setCursor(Cursor.getDefaultCursor());
				JButtonPlus _button = (JButtonPlus) e.getSource();
				_button.setText(_button.getText(), false);
			}
		};

		mainSettings.addMouseListener(changeCursor);
		send.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JComponent.WHEN_IN_FOCUSED_WINDOW);

		imagePanel.setLayout(null);
		
		imagePanel.add(mainSettings);
		imagePanel.add(topMenu);
		imagePanel.add(minUp);
		imagePanel.add(maxUp);
		imagePanel.add(closeUp);
		imagePanel.add(closeDown);
		imagePanel.add(send);
		imagePanel.add(sendMethod);
		
		imagePanel.add(recordPanel);
		imagePanel.add(messageScroll);
		imagePanel.add(noticebar);
		imagePanel.add(memberbar);

		mainSettings.setBounds(260, 4, 370, 32);
		topMenu.setBounds(763, 0, 32, 32);
		minUp.setBounds(795, 0, 32, 32);
		maxUp.setBounds(827, 0, 32, 32);
		closeUp.setBounds(859, 0, 32, 32);
		closeDown.setBounds(540, 643, 72, 28);
		send.setBounds(626, 643, 63, 28);
		sendMethod.setBounds(689, 643, 25, 28);
		
		recordPane.setEditable(false);
		recordPanel.setBounds(0, 91, 727, 464);
		recordPanel.setOpaque(false);
		recordPanel.setLayout(null);
		recordPanel.add(recordScrollPane);
		recordScrollPane.setBounds(10, 0, 710, 469);
		recordScrollPane.setBorder(null);
		recordPane.setOpaque(false);
		recordScrollPane.setOpaque(false);
		recordScrollPane.getViewport().setOpaque(false);
		recordScrollPane.getVerticalScrollBar().setUI(new DemoScrollBarUI());

		message.setLineWrap(true);
		message.setWrapStyleWord(true);
		message.setBorder(null);
		message.setOpaque(false);
		messageScroll.setBounds(10, 590, 715, 50);
		messageScroll.setBorder(null);
		messageScroll.setOpaque(false);
		messageScroll.getViewport().setOpaque(false);
		messageScroll.getVerticalScrollBar().setUI(new DemoScrollBarUI());
		
		noticebar.setLocation(728, 91);
		memberbar.setBounds(728, 255, 162, 428);
		
		balloonTipUp = BalloonTip.createEdgedBalloonTip(mainSettings, BalloonTip.Alignment.LEFT_ALIGNED_BELOW, Color.BLACK, new Color(255, 255, 225), 10, 15, 20, true);
        balloonTipUp.setTriangleTipLocation(BalloonTip.TriangleTipLocation.SOUTH);
        balloonTipUp.setIcon(new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/balloon/frameicon.png")));
        balloonTipUp.setIconTextGap(10);
		
		balloonTipDown = BalloonTip.createEdgedBalloonTip(send, BalloonTip.Alignment.RIGHT_ALIGNED_ABOVE, Color.BLACK, new Color(255, 255, 225), 10, 15, 20, true);
        balloonTipDown.setTriangleTipLocation(BalloonTip.TriangleTipLocation.NORTH);
        balloonTipDown.setIcon(new ImageIcon(JMyChatDlg.class.getResource("/cn/compscosys/images/balloon/frameicon.png")));
        balloonTipDown.setIconTextGap(10);
	}
	
	private JButton buttonInit(ImageIcon _defaultIcon, ImageIcon _hoverIcon, ImageIcon _clickedIcon) {
		JButton _button = new JButton();
		_button.setIcon(_defaultIcon);
		_button.setRolloverIcon(_hoverIcon);
		_button.setPressedIcon(_clickedIcon);
		_button.setHorizontalTextPosition(SwingConstants.CENTER);
		_button.setVerticalTextPosition(SwingConstants.CENTER);
		_button.setBorder(null);
		return _button;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == closeUp || e.getSource() == closeDown) {
			System.exit(0);
		}
		else if (e.getSource() == mainSettings) {
			JMainSettingDlg login = new JMainSettingDlg(this, "Settings", true);
			login.setVisible(true);
			if (login.getSource()) {
				if (login.getAddress() != this.multicastAddress) {
					this.multicastAddress = login.getAddress();
					postThreadHost.connect(multicastAddress);
				}
				if (login.getNickname() != this.nickname) { this.nickname = login.getNickname(); }
				mainSettings.setText(multicastAddress, nickname);
				
				if (login.getBubbleStyle() != this.bubbleStyle.toString()) { this.bubbleStyle = JBubble.BubbleStyle.valueOf(login.getBubbleStyle()); }
				if (login.getImageIcon() != null) { this.headPortrait = login.getImageIcon(); }
			}
		}
		else if (e.getSource() == minUp) {
			setExtendedState(JFrame.ICONIFIED);
		}
		else if (e.getSource() == send) {
			if (postThreadHost == null) {
				setBalloonUp("当前连接不可用，请检查多播组设置");
				return;
			}
			else if (this.message.getText().length() == 0) {
				setBalloonDown("消息不能为空");
				return;
			}
			else if (this.message.getText().length() > 16000) {
				setBalloonDown("发送的消息过长");
				return;
			}
			postThreadHost.post(nickname, headPortrait, message.getText(), bubbleStyle);
		}
	}
	
	public void addMessage(Message _message, boolean _isSender) {
		recordPane.setCaretPosition(recordPane.getDocument().getLength());
		long currentTime = System.currentTimeMillis();
		if (currentTime - oldCurrentTime > 180000) {
			oldCurrentTime = currentTime;
			insertDoc(styledDoc, df.format(new Date(currentTime)) + "\r\n", "currentTime");
			recordPane.setCaretPosition(recordPane.getDocument().getLength());
		}
		
		if (_isSender) {
			this.message.setText("");
			recordPane.insertComponent(JBubble.createRoundedJBubble(_message.getHeadPortrait(), _message.getNickname(), _message.getMessage(),
					JBubble.Alignment.SENDER_ALIGNED, _message.getBubbleStyle(), 630));
			insertDoc(styledDoc, "\r\n", "currentTime");
		}
		else {
			recordPane.insertComponent(JBubble.createRoundedJBubble(_message.getHeadPortrait(), _message.getNickname(), _message.getMessage(),
					JBubble.Alignment.RECEIVER_ALIGNED, _message.getBubbleStyle(), 630));
			insertDoc(styledDoc, "\r\n", "currentTime");
		}
		recordPane.setCaretPosition(recordPane.getDocument().getLength());
		
		if (!isFocused()) {
			this.setVisible(true);
			systemTrayHost.setSystemTray(this, blingblingIcon.getImage(), null);
		}
	}
	
	public void setBalloonUp(String _tip) {
		balloonTipUp.setText(_tip);
		balloonTipUp.setVisible(true);
	}
	
	public void setBalloonDown(String _tip) {
		balloonTipDown.setText(_tip);
		balloonTipDown.setVisible(true);
	}
	
	public void createStyle(String style, StyledDocument doc, int size, Boolean bold, Boolean italic, Color color, int alignment) {
		Style sys = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		try { doc.removeStyle(style); } catch(Exception e) {}		// 若存在这种Style则先删除
		
		Style s = doc.addStyle(style,sys);							// 加入
		StyleConstants.setFontSize(s,size);							// 大小
		StyleConstants.setBold(s,bold);								// 粗体
		StyleConstants.setItalic(s,italic);							// 斜体
		StyleConstants.setForeground(s,color);						// 颜色
        StyleConstants.setAlignment(s, alignment);					// 对齐方式
        StyleConstants.setFontFamily(s, "微软雅黑");					// 字体
	}
	
	public void insertDoc(StyledDocument styledDoc, String content, String currentStyle) {
		try {
			int length = styledDoc.getLength();
			styledDoc.insertString( styledDoc.getLength(), content, styledDoc.getStyle(currentStyle));
			styledDoc.setParagraphAttributes(length, content.length(), styledDoc.getStyle(currentStyle), false);
		} catch (BadLocationException e) {
			System.err.println("BadLocationException: " + e);
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.drawImage(functionList, 0, 39, functionList.getWidth(this), functionList.getHeight(this), this);
	}
}
