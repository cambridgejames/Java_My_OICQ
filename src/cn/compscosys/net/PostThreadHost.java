package cn.compscosys.net;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;

import javax.swing.ImageIcon;

import cn.compscosys.frame.JMyChatDlg;
import cn.compscosys.ui.JBubble;

public class PostThreadHost {
	private MulticastSocket socket = null;
	private InetAddress address = null;
	private JMyChatDlg dlg;
	
	private ReceiveThreadHost receiveHost;
	private Thread receiveThread;
	
	// These three variables are not about communication, they are just about identifying the sender of the message.
	private ServerSocket serverSocket = null;
	private int localPort;
	private String localIp = InetAddress.getLocalHost().getHostAddress();
	
	public PostThreadHost(JMyChatDlg _dlg) throws Exception {
		socket = new MulticastSocket(59898);
		socket.setTimeToLive(3);
		
		serverSocket = new ServerSocket(0);
		localPort = serverSocket.getLocalPort();
		
		this.dlg = _dlg;
	}
	
	public void connect(String _address) {
		try {
			if (address != null) { socket.leaveGroup(address); }
			address = InetAddress.getByName(_address);
			socket.joinGroup(address);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (receiveHost == null) { receiveHost = new ReceiveThreadHost(localPort, localIp, socket, dlg); }
		//else { receiveHost.setSocket(socket); }
		if (receiveThread == null) { receiveThread = new Thread(new ReceiveThreadHost(localPort, localIp, socket, dlg)); receiveThread.start(); }
		
		/*if (receive != null) { receive.interrupt(); }
		receive = new Thread(new ReceiveThreadHost(localPort, localIp, socket, dlg));
		receive.start();*/
	}
	
	public void post(String _nickname, ImageIcon _headPortrait, String _message, JBubble.BubbleStyle _bBubbleStyle) {
		DatagramPacket packet = null;
		Message message = new Message(_nickname, localIp, localPort, _headPortrait, _message, _bBubbleStyle);
		byte[] buff = ObjTransformation.ObjectToByte(message);
		packet = new DatagramPacket(buff, buff.length, address, 59898);
		try {
			socket.send(packet);
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
	}
}
