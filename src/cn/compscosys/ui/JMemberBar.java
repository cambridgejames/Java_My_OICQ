package cn.compscosys.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class JMemberBar extends JPanel {
	private static final long serialVersionUID = 6165388644549742405L;
	
	public static enum MemberType{GROUP_OWNER, ADMINISTRATOR, ORDINARY_MEMBER};
	
	int memberCount = 0;
	
	private JLabel titleHost = new JLabel("0") {
		private static final long serialVersionUID = -8800929995521982759L;
		public void setText(String _text) {
			super.setText("群成员 " + _text);
		}
	};
	
	private DefaultListModel<MemberItem> listModel = new DefaultListModel<MemberItem>();
	private JList<MemberItem> list = new JList<MemberItem>(listModel);
	private JScrollPane listScroll = new JScrollPane(list);
	
	public JMemberBar(int height) {
		super();
		this.setLayout(null);
		this.setPreferredSize(new Dimension(162, height));
		this.setSize(162, height);
		titleHost.setFont(new Font("微软雅黑", Font.BOLD, 15));
		
		this.add(titleHost);
		this.add(listScroll);
		
		titleHost.setBounds(12, 0, 138, 40);
		listScroll.setBounds(0, 40, 162, height - 40);
		
		list.setOpaque(false);
		list.setBorder(null);
		((JLabel)list.getCellRenderer()).setOpaque(false);
		listScroll.setBorder(null);
		listScroll.setOpaque(false);
		listScroll.getViewport().setOpaque(false);
		
		this.setOpaque(false);
	}
	
	public void addRow(MemberItem m) {
		//MemberItem m = new MemberItem(headPortrait, nickname, memberType, localIp, localPort);
		this.listModel.addElement(m);
		memberCount++;
		this.titleHost.setText(((Integer)this.memberCount).toString());
		updateUI();
	}
	
	public void removaRow(/*String localIp, int localPort*/) {
		for (int i = 0; i < list.getVisibleRowCount(); i++) {
			//System.out.println(i);
		}
	}
}

class MemberItem extends JPanel implements Serializable {
	private static final long serialVersionUID = 1736899540348878142L;
	
	private JLabel nickname;
	private Image headPortrait;
	private JMemberBar.MemberType memberType;
	
	private final String localIp;
	private final int localPort;

	private static Image groupOwner = new ImageIcon(MemberItem.class.getResource("/cn/compscosys/images/jsidebar/group_owner_type_icon.png")).getImage();
	private static Image administrator = new ImageIcon(MemberItem.class.getResource("/cn/compscosys/images/jsidebar/administrator_type_icon.png")).getImage();
	
	public MemberItem(Image headPortrait, String nickname, JMemberBar.MemberType memberType, String localIp, int localPort) {
		this.nickname = new JLabel(nickname);
		this.nickname.setFont(new Font("宋体", Font.PLAIN, 13));
		this.headPortrait = headPortrait.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
		this.memberType = memberType;
		this.localIp = localIp;
		this.localPort = localPort;
		
		this.setPreferredSize(new Dimension(162, 28));
		this.setSize(162, 28);
		this.setOpaque(false);
		this.setLayout(null);
		this.add(this.nickname);
		this.nickname.setBounds(36, 0, this.memberType == JMemberBar.MemberType.ORDINARY_MEMBER ? 112 : 92, 28);
	}
	
	public void setHeadPortrait(Image _headPortrait) {
		this.headPortrait = _headPortrait.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
		this.updateUI();
	}
	
	public void setText(String _nickname) {
		this.nickname.setText(_nickname);
	}
	
	public void setType(JMemberBar.MemberType _memberType) {
		this.memberType = _memberType;
		this.nickname.setBounds(36, 0, this.memberType == JMemberBar.MemberType.ORDINARY_MEMBER ? 112 : 92, 28);
		this.updateUI();
	}
	
	public String getNickname() { return this.nickname.getText(); }
	public String getLocalIp() { return this.localIp; }
	public int getLocalPort() { return this.localPort; }
	
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj instanceof MemberItem) {
			MemberItem memberItem = (MemberItem) obj;
			return memberItem.getLocalIp().equals(this.localIp) && memberItem.getLocalPort() == this.localPort;
		}
		return false;
	}
	
	public String toString() {
		return this.nickname.getText();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.headPortrait, 12, 5, 18, 18, this);
		switch(this.memberType) {
		case ADMINISTRATOR:
			g.drawImage(administrator, 134, 6, 16, 16, this);
			break;
		case GROUP_OWNER:
			g.drawImage(groupOwner, 134, 6, 16, 16, this);
			break;
		default:
			break;
		}
	}
}
