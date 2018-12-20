package cn.compscosys.frame;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JMainSettingDlg extends JDialog implements ActionListener {
	private static final long serialVersionUID = 8124102863882454416L;

	private JLabel tipNickname;
	private JLabel tipMulticast;
	private JLabel tipBubbleStyle;
	private JLabel tipImage;
	
	private JTextField nickname;
	private JComboBox<String> multicast;
	private JComboBox<String> bubbleStyle;
	private JTextField imagePath;
	
	private JButton editPath;
	private JButton login;
	
	private ImageIcon imageIcon;
	
	private boolean isMatch = false;
	
	public JMainSettingDlg(JFrame frame, String title, boolean modal) {
		super(frame, title, modal);
		init();
		this.setSize(397, 282);					// Set width and height of window.
		this.setLocationRelativeTo(null);		// Display the window in center.
		this.setTitle(title);
		
		this.setResizable(false);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.addWindowListener( new WindowAdapter() { public void windowClosing(WindowEvent we){ System.exit(0); } } );
	}
	
	private void init() {
		tipNickname = new JLabel("昵称：");
		tipMulticast = new JLabel("组播地址：");
		tipBubbleStyle = new JLabel("气泡样式：");
		tipImage = new JLabel("头像：");
		
		nickname = new JTextField("翠花");
		multicast = new JComboBox<String>(new String[]{"224.255.10.0", "224.255.10.1", "224.255.10.2", "224.255.10.3", "224.255.10.4",
				"224.255.10.5", "224.255.10.6", "224.255.10.7", "224.255.10.8", "224.255.10.9", });
		bubbleStyle = new JComboBox<String>(new String[]{"BASENESS_AN_ATTITUDE", "BIG_CAT", "DEFAULT_BUBBLE", "FORTUEN_CAT", "GIRL_AND_SHEEP", "PEACEFUL_SUNNY_DAY",
				"PEACH_BLOSSOMS", "PURE_WHITE_LACE", "SMALL_FLOWER_VINE", "SMILING_HIPPO", "WHITE_WATER"});
		imagePath = new JTextField();
		
		login = new JButton("保存");
		editPath = new JButton("浏览…");
		
		login.addActionListener(this);
		editPath.addActionListener(this);
		
		setLayout(null);

		add(tipNickname);
		add(nickname);
		add(tipMulticast);
		add(multicast);
		add(tipBubbleStyle);
		add(bubbleStyle);
		add(tipImage);
		add(imagePath);
		add(editPath);
		add(login);

		tipNickname.setBounds(20, 20, 100, 25);
		nickname.setBounds(120, 20, 240, 25);
		tipMulticast.setBounds(20, 65, 100, 25);
		multicast.setBounds(120, 65, 240, 25);
		tipBubbleStyle.setBounds(20, 110, 100, 25);
		bubbleStyle.setBounds(120, 110, 240, 25);
		tipImage.setBounds(20, 155, 100, 25);
		imagePath.setBounds(120, 155, 150, 25);
		editPath.setBounds(280, 155, 80, 25);
		login.setBounds(140, 200, 100, 25);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == login) {
			this.isMatch = true;
			this.dispose();
		}
		else if (e.getSource() == editPath) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("图像文件 *.bmp *.gif *.jpg *.png", "bmp", "gif", "jpg", "png");
			JFileChooser jfc = new JFileChooser();
			jfc.setFileFilter(filter);
	        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );
	        int value = jfc.showOpenDialog(this);
	        if (value == JFileChooser.APPROVE_OPTION) {
	        	String filePath = jfc.getSelectedFile().getAbsolutePath();
	        	this.imagePath.setText(filePath);
	        	imageIcon = new ImageIcon(filePath);
	        }
		}
	}
	
	public String getNickname() { return this.nickname.getText(); }
	public String getAddress() { return this.multicast.getSelectedItem().toString(); }
	public String getBubbleStyle() { return this.bubbleStyle.getSelectedItem().toString(); }
	public ImageIcon getImageIcon() { return new ImageIcon(this.imageIcon.getImage().getScaledInstance(48,48,Image.SCALE_SMOOTH)); }
	
	public boolean getSource() { return this.isMatch; }
}
