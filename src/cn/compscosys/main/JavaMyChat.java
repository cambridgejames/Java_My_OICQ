package cn.compscosys.main;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import cn.compscosys.frame.JLoginDlg;
import cn.compscosys.frame.JMyChatDlg;
import cn.compscosys.tray.SystemTrayHost;

public class JavaMyChat {
	public static void main(String[] args) {
		
		initGobalFont(new Font("微软雅黑", Font.PLAIN, 16));
		JLoginDlg login = new JLoginDlg(null, "Java My OICQ", true);
		SystemTrayHost systemTrayHost = new SystemTrayHost(null,
				new ImageIcon(JavaMyChat.class.getResource("/cn/compscosys/images/systemtray/system_tray_icon_unloged.png")).getImage(),
				"Java My OICQ");
		login.setVisible(true);
		
		
		JMyChatDlg dlg = new JMyChatDlg(login.getAddress(), login.getNickname(), systemTrayHost);
		systemTrayHost.setSystemTray(dlg,
				new ImageIcon(JavaMyChat.class.getResource("/cn/compscosys/images/systemtray/system_tray_icon_default.png")).getImage(),
				null);
		dlg.setVisible(true);
	}
	
	public static void initGobalFont(Font font) {
	    FontUIResource fontResource = new FontUIResource(font);
	    for(Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if(value instanceof FontUIResource) {
	            UIManager.put(key, fontResource);
	        }
	    }
	}
}
