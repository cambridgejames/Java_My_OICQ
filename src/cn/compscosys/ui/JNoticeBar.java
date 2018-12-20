package cn.compscosys.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class JNoticeBar extends JPanel {
	private static final long serialVersionUID = 9180062754247580626L;
	
	public static enum messageType{FILE_TYPE, PICTURE_TYPE, NOTICE_TYPE, VOYE_TYPE};
	private static Image noNotice = new ImageIcon(JNoticeBar.class.getResource("/cn/compscosys/images/jsidebar/sidebar_default.png")).getImage();
	
	private JLabel titleHost = new JLabel("群通知");
	private DefaultTableModel tableModel = new DefaultTableModel(new String[][]{}, new String[] {"", ""}) {
		private static final long serialVersionUID = -2495187371674472828L;
		public boolean isCellEditable(int row, int column) { return false; }
    };
	private JTable itemHost = new JTable(tableModel) {
		private static final long serialVersionUID = -1699652231306103720L;
		public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
    		Component c = super.prepareRenderer(renderer, row, column);
    		if (c instanceof JComponent) { ((JComponent) c).setOpaque(false); }
    		return c;
    	}
    };
	
	public JNoticeBar(int height) {
		super();
		this.setLayout(null);
		titleHost.setFont(new Font("微软雅黑", Font.BOLD, 15));
		
		this.add(titleHost);
		this.add(itemHost);
		
		titleHost.setBounds(12, 0, 138, 40);
		itemHost.setBounds(6, 40, 150, height - 40);
		
		itemHost.setOpaque(false);
		itemHost.setShowGrid(false);
		itemHost.setForeground(new Color(119, 117, 116));
		itemHost.setFont(new Font("宋体", Font.PLAIN, 12));
		itemHost.setRowHeight(20);
		itemHost.getColumnModel().getColumn(0).setPreferredWidth(26);
		super.setSize(162, height);
		
		this.setOpaque(false);
	}
	
	public void addRow(messageType _type, String _title) {
		switch(_type) {
		case FILE_TYPE:
			this.tableModel.addRow(new String[] {"【文件】", _title});
			break;
		case NOTICE_TYPE:
			this.tableModel.addRow(new String[] {"【公告】", _title});
			break;
		case PICTURE_TYPE:
			this.tableModel.addRow(new String[] {"【图片】", _title});
			break;
		case VOYE_TYPE:
			this.tableModel.addRow(new String[] {"【投票】", _title});
			break;
		default:
			break;
		}

		if (tableModel.getRowCount() > 6) { this.tableModel.removeRow(0); }
	}
	
	public void removeRow() {
		if (tableModel.getRowCount() > 0) { this.tableModel.removeRow(0); }
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (tableModel.getRowCount() == 0) {
			g.drawImage(noNotice, 28, 46, noNotice.getWidth(this), noNotice.getHeight(this), this);
		}
	}
}
