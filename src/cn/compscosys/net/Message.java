package cn.compscosys.net;

import java.io.Serializable;

import javax.swing.ImageIcon;

import cn.compscosys.ui.JBubble;

public class Message implements Serializable {
	private static final long serialVersionUID = -4214025272493537966L;
	
	private String nickname;
	private String localIp;
	private int localPort;
	
	private ImageIcon headPortrait;
	private String message;
	private JBubble.BubbleStyle bubbleStyle;
	
	public Message() {}
	public Message(String _nickname, String _localIp, int _localPort, ImageIcon _headPortrait, String _message, JBubble.BubbleStyle _bubbleStyle) {
		this.nickname = _nickname;
		this.localIp = _localIp;
		this.localPort = _localPort;
		this.headPortrait = _headPortrait;
		this.message = _message;
		this.bubbleStyle = _bubbleStyle;
	}
	public Message(Message _message) {
		this.nickname = _message.getNickname();
		this.localIp = _message.getLocalIp();
		this.localPort = _message.getLocalPort();
		this.headPortrait = _message.getHeadPortrait();
		this.message = _message.getMessage();
		this.bubbleStyle = _message.getBubbleStyle();
	}
	
	public void setNickname(String _nickname) { this.nickname = _nickname; }
	public void setLocalIp(String _localIp) { this.localIp = _localIp; }
	public void setLocalPort(int _localPort) { this.localPort = _localPort; }
	public void setHeadPortrait(ImageIcon _headPortrait) { this.headPortrait = _headPortrait; }
	public void setMessage(String _message) { this.message = _message; }
	public void setBubbleStyle(JBubble.BubbleStyle _bubbleStyle) { this.bubbleStyle = _bubbleStyle; }
	public void setMessage(String _nickname, String _localIp, int _localPort, ImageIcon _headPortrait, String _message, JBubble.BubbleStyle _bubbleStyle) {
		this.nickname = _nickname;
		this.localIp = _localIp;
		this.localPort = _localPort;
		this.headPortrait = _headPortrait;
		this.message = _message;
		this.bubbleStyle = _bubbleStyle;
	}
	public void setMessage(Message _message) {
		this.nickname = _message.getNickname();
		this.localIp = _message.getLocalIp();
		this.localPort = _message.getLocalPort();
		this.headPortrait = _message.getHeadPortrait();
		this.message = _message.getMessage();
		this.bubbleStyle = _message.getBubbleStyle();
	}
	
	public String getNickname() { return this.nickname; }
	public String getLocalIp() { return this.localIp; }
	public int getLocalPort() { return this.localPort; }
	public ImageIcon getHeadPortrait() { return this.headPortrait; }
	public String getMessage() { return this.message; }
	public JBubble.BubbleStyle getBubbleStyle() { return this.bubbleStyle; }
	
	public boolean isSender(String _localIp, int _localPort) {
		return this.localIp.equals(_localIp) && this.localPort == _localPort;
	}
	
	public String toString() {
		return this.message;
	}
}
