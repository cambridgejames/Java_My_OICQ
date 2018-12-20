package cn.compscosys.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class SystemTrayHost {
	private JFrame component;
	private TrayIcon trayIcon;
	private SystemTray systemTray = SystemTray.getSystemTray();
	
	public SystemTrayHost(JFrame _component, Image _tray, String _tipText) {
		component = _component;

		trayIcon = new TrayIcon(_tray.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
		try {
			systemTray.add(trayIcon);
		} catch (AWTException e) { e.printStackTrace(); }
		
		if (_tipText != null) { trayIcon.setToolTip(_tipText); }	// 添加工具提示文本
		
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (component == null) return;
				component.setVisible(true);
				component.setExtendedState(JFrame.NORMAL);
			}
		});
	}
	
	public void setSystemTray(JFrame _component, Image _tray, String _tipText) {
		if (_component != null) { component = _component; }	// 如果容器不为空（设置了新容器），则更新该容器
		if (_tray != null) { trayIcon.setImage(_tray.getScaledInstance(16, 16, Image.SCALE_SMOOTH)); }	// 更新系统托盘图标
		if (_tipText != null) { trayIcon.setToolTip(_tipText); }	// 添加工具提示文本
	}
}
