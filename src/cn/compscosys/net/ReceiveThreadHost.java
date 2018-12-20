package cn.compscosys.net;

import java.net.DatagramPacket;
import java.net.MulticastSocket;

import cn.compscosys.frame.JMyChatDlg;

public class ReceiveThreadHost extends Thread implements Runnable {
	private int localPort;
	private String localIp;
	private MulticastSocket socket;
	private JMyChatDlg dlg;

	byte[] buff = new byte[524288];
	DatagramPacket packet;
	
	public ReceiveThreadHost(int localPort, String localIp, MulticastSocket socket, JMyChatDlg dlg) {
		this.localPort = localPort;
		this.localIp = localIp;
		this.socket = socket;
		this.dlg = dlg;
	}
	
	public void setSocket(MulticastSocket _socket) {
		this.socket = _socket;
	}
	
	public void run() {
		Message message;
		
		while(!isInterrupted()){
			packet = null;
			packet = new DatagramPacket(buff, 0, buff.length);
			try {
				socket.receive(packet);
				message = (Message)ObjTransformation.ByteToObject(packet.getData());
				dlg.addMessage(message, message.isSender(localIp, localPort));
			}/* catch(InterruptedException e){
                break;
            }*/ catch (Exception e) {
            	e.printStackTrace();
				continue;
			}
		}
	}
}
