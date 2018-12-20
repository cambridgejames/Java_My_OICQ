package cn.compscosys.ui;

import javax.swing.JButton;

public class JButtonPlus extends JButton {
	private static final long serialVersionUID = -1211720756880726745L;
	public JButtonPlus(String _address, String _nickname) {
		super("<html><nobr><b><font color=white>" + _address + "&nbsp;@&nbsp;" + _nickname + "</font></b></nobr></html>");
		setContentAreaFilled(false);
		setBorder(null);
		setFocusPainted(false);
	}
	public void setText(String _text, boolean _isLine) {
		String _rawStr = super.getText();
		if (_isLine) {
			_rawStr = _rawStr.replace("<html><nobr><b><font color=white>", "<html><nobr><b><font color=white><u>");
			_rawStr = _rawStr.replace("</font></b></nobr></html>", "</u></font></b></nobr></html>");
			super.setText(_rawStr);
		}
		else {
			_rawStr = _rawStr.replace("<html><nobr><b><font color=white><u>", "<html><nobr><b><font color=white>");
			_rawStr = _rawStr.replace("</u></font></b></nobr></html>", "</font></b></nobr></html>");
			super.setText(_rawStr);
		}
	}
	public void setText(String _address, String _nickname) {
		super.setText("<html><nobr><b><font color=white>" + _address + "&nbsp;@&nbsp;" + _nickname + "</font></b></nobr></html>");
	}
}
