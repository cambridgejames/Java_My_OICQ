package cn.compscosys.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class JLoginDlg extends JDialog implements ActionListener {
	private static final long serialVersionUID = 8124102863882454416L;
	
	protected JLabel tipUserName;
	protected JLabel tipPassword;
	
	protected JTextField username;
	protected JComboBox<String> password;
	
	protected JButton login;
	protected JButton cancle;
	
	protected boolean isMatch = false;
	
	public JLoginDlg(JFrame frame, String title, boolean modal) {
		super(frame, title, modal);
		init();
		this.setSize(427, 327);					// Set width and height of window.
		this.setLocationRelativeTo(null);		// Display the window in center.
		this.setTitle(title);
		
		this.setResizable(false);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener( new WindowAdapter() { public void windowClosing(WindowEvent we){ System.exit(0); } } );
	}
	
	protected void init() {
		tipUserName = new JLabel("昵称：");
		tipPassword = new JLabel("组播地址：");
		username = new JTextField("翠花");
		//password = new JComboBox<String>(new String[]{"BASENESS_AN_ATTITUDE", "BIG_CAT", "DEFAULT_BUBBLE", "FORTUEN_CAT", "GIRL_AND_SHEEP", "PEACEFUL_SUNNY_DAY",
		//		"PEACH_BLOSSOMS", "PURE_WHITE_LACE", "SMALL_FLOWER_VINE", "SMILING_HIPPO", "WHITE_WATER"});
		password = new JComboBox<String>(new String[]{"224.255.10.0", "224.255.10.1", "224.255.10.2", "224.255.10.3", "224.255.10.4",
				"224.255.10.5", "224.255.10.6", "224.255.10.7", "224.255.10.8", "224.255.10.9", });
		login = new JButton("Login");
		cancle = new JButton("Cancle");
		
		login.addActionListener(this);
		cancle.addActionListener(this);
		
		setLayout(null);

		add(tipUserName);
		add(username);
		add(tipPassword);
		add(password);
		add(login);
		//add(cancle);

		tipUserName.setBounds(20, 20, 100, 25);
		username.setBounds(120, 20, 240, 25);
		tipPassword.setBounds(20, 65, 100, 25);
		password.setBounds(120, 65, 240, 25);
		login.setBounds(80, 110, 100, 25);
		//cancle.setBounds(220, 110, 100, 25);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == login) {
			Object[] options = {"OK"};
			String inputUserName = username.getText(), inputPassword = password.getSelectedItem().toString();
			if (inputUserName.isEmpty() || inputPassword.isEmpty()) {
				JOptionPane.showOptionDialog(null, "The user name or password can not be empty!", "Warning",
						JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
			}
			else {
				this.isMatch = true;
				this.dispose();
			}
		}
	}
	
	public String getNickname() { return this.username.getText(); }
	public String getAddress() { return this.password.getSelectedItem().toString(); }
	
	public boolean getSource() { return this.isMatch; }
}
