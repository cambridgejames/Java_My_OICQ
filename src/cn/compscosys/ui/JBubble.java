package cn.compscosys.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import sun.font.FontDesignMetrics;

public class JBubble extends JPanel {
	private static final long serialVersionUID = 3874821763437861392L;
	
	public static enum Alignment {RECEIVER_ALIGNED, SENDER_ALIGNED};
	public static enum BubbleStyle {BASENESS_AN_ATTITUDE, BIG_CAT, DEFAULT_BUBBLE, FORTUEN_CAT, GIRL_AND_SHEEP, PEACEFUL_SUNNY_DAY,
		PEACH_BLOSSOMS, PURE_WHITE_LACE, SMALL_FLOWER_VINE, SMILING_HIPPO, WHITE_WATER};
	
	private final Image headPortrait;
	private final String nickname;
	private final String message;
	private final Alignment alignment;
	private final BubbleStyle bubbleStyle;
	
	private final int maxWidth;
	private int bubbleTextWidth;
	private int bubbleTextHeight;

	private Color[] colors;
	
	private Font font = new Font("微软雅黑", Font.PLAIN, 16);
	
	private JTextArea textArea = new JTextArea();
	
	public static JBubble createRoundedJBubble(Image headPortrait, String nickname, String message, Alignment alignment, BubbleStyle bubbleStyle, int maxWidth) {
		return new JBubble(headPortrait.getScaledInstance(30, 30, Image.SCALE_DEFAULT), nickname, message, alignment, bubbleStyle, maxWidth);
	}
	
	public static JBubble createRoundedJBubble(ImageIcon headPortrait, String nickname, String message, Alignment alignment, BubbleStyle bubbleStyle, int maxWidth) {
		return new JBubble(headPortrait.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT), nickname, message, alignment, bubbleStyle, maxWidth);
	}
	
	private JBubble(Image headPortrait, String nickname, String message, Alignment alignment, BubbleStyle bubbleStyle, int maxWidth) {
		this.headPortrait = headPortrait;
		this.nickname = nickname;
		this.message = message;
		this.alignment = alignment;
		this.bubbleStyle = bubbleStyle;
		this.maxWidth = maxWidth;
		this.colors = getColors(bubbleStyle);
		
		setOpaque(false);
		setLayout(null);
		
		LinkedList<String> messageList = makeLineBreaking(message);
		bubbleTextHeight = Math.max(20, messageList.size() * 22);

		textArea.setFont(font);
		textArea.setForeground(colors[2]);
		this.add(textArea);
		textArea.setSize(bubbleTextWidth, bubbleTextHeight);
		textArea.setOpaque(false);
		textArea.setEditable(false);
		
		while (messageList.size() > 0) {
			textArea.append(messageList.poll() + "\n");
		}

		switch (this.alignment) {
			case RECEIVER_ALIGNED:
				setPreferredSize(new Dimension(bubbleTextWidth + 70, bubbleTextHeight + 49));
				textArea.setLocation(53, 32);
				break;
			case SENDER_ALIGNED:
				setPreferredSize(new Dimension(bubbleTextWidth + 70, bubbleTextHeight + 31));
				textArea.setLocation(53, 14);
				break;
			default:
				break;
		}
	}
	
	private LinkedList<String> makeLineBreaking(String message) {
		LinkedList<String> messageList = new LinkedList<String>();
		int beginIndex;
		int endIndex;
		int totalLength;
		
		String[] sourceStrArray = message.split("\n");
		for(String sourceStr : sourceStrArray) {
			beginIndex = 0;
			endIndex = 1;
			while (endIndex < sourceStr.length()) {
				totalLength = FontDesignMetrics.getMetrics(font).stringWidth(sourceStr.substring(beginIndex, endIndex));
				if (totalLength > maxWidth - 30) {
					messageList.add(sourceStr.substring(beginIndex, endIndex - 1));
					bubbleTextWidth = Math.max(bubbleTextWidth, totalLength);
					beginIndex = endIndex - 1;
				}
				endIndex++;
			}
			messageList.add(sourceStr.substring(beginIndex, sourceStr.length()));
			bubbleTextWidth = Math.max(bubbleTextWidth, FontDesignMetrics.getMetrics(font).stringWidth(sourceStr.substring(beginIndex,
					sourceStr.length())));
		}
		return messageList;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		boolean corner = getSharpCorner(bubbleStyle);
		Image[] images = getCornerImages(this.bubbleStyle);
		int[][] offset = getOffset(bubbleStyle);
		
		switch (this.alignment) {
			case RECEIVER_ALIGNED:
				// Head Portrait
				g2d.drawImage(this.headPortrait, 3, 4, headPortrait.getWidth(this), headPortrait.getHeight(this), this);
				// Filled
				g2d.setColor(colors[1]);
				if (corner) { g2d.fillPolygon(new int[]{44, 38, 43}, new int[]{26, 26, 32}, 3); }
				g2d.fillRoundRect(43, 22, bubbleTextWidth + 25, bubbleTextHeight + 20, 16, 16);
				// Border
				g2d.setColor(colors[0]);
				if (corner) {
					g2d.drawLine(44, 26, 38, 26);
					g2d.drawLine(38, 26, 43, 32);
					g2d.drawLine(43, 32, 43, bubbleTextHeight + 34);
					g2d.drawArc(43, bubbleTextHeight + 26, 16, 16, 180, 90);
					g2d.drawLine(51, bubbleTextHeight + 42, bubbleTextWidth + 60, bubbleTextHeight + 42);
					g2d.drawArc(bubbleTextWidth + 52, bubbleTextHeight + 26, 16, 16, 270, 90);
					g2d.drawLine(bubbleTextWidth + 68, 30, bubbleTextWidth + 68, bubbleTextHeight + 34);
					g2d.drawArc(bubbleTextWidth + 52, 22, 16, 16, 0, 90);
					g2d.drawLine(51, 22, bubbleTextWidth + 60, 22);
					g2d.drawArc(43, 22, 16, 16, 90, 60);
				} else {
					g2d.drawRoundRect(43, 22, bubbleTextWidth + 25, bubbleTextHeight + 20, 16, 16);
				}
				// Corner Icon
				if (images[0] != null) g2d.drawImage(images[0], offset[0][0], offset[0][1], images[0].getWidth(this), images[0].getHeight(this), this);
				if (images[1] != null) g2d.drawImage(images[1], bubbleTextWidth + offset[0][2], offset[0][3], images[1].getWidth(this), images[1].getHeight(this), this);
				if (images[2] != null) g2d.drawImage(images[2], offset[0][4], bubbleTextHeight + offset[0][5], images[2].getWidth(this), images[2].getHeight(this), this);
				if (images[3] != null) g2d.drawImage(images[3], bubbleTextWidth + offset[0][6], bubbleTextHeight + offset[0][7], images[3].getWidth(this), images[3].getHeight(this), this);
				// Nickname
				g2d.setColor(Color.GRAY);
				g2d.setFont(new Font("微软雅黑", Font.PLAIN, 14));
				g2d.drawString(nickname, 43, 14);
				break;
			case SENDER_ALIGNED:
				// Head Portrait
				g2d.drawImage(this.headPortrait, 3, 4, headPortrait.getWidth(this), headPortrait.getHeight(this), this);
				// Filled
				g2d.setColor(colors[1]);
				if (corner) { g2d.fillPolygon(new int[]{44, 38, 43}, new int[]{8, 8, 14}, 3); }
				g2d.fillRoundRect(43, 4, bubbleTextWidth + 25, bubbleTextHeight + 20, 16, 16);
				// Border
				g2d.setColor(colors[0]);
				if (corner) {
					g2d.drawLine(44, 8, 38, 8);
					g2d.drawLine(38, 8, 43, 14);
					g2d.drawLine(43, 14, 43, bubbleTextHeight + 16);
					g2d.drawArc(43, bubbleTextHeight + 8, 16, 16, 180, 90);
					g2d.drawLine(51, bubbleTextHeight + 24, bubbleTextWidth + 60, bubbleTextHeight + 24);
					g2d.drawArc(bubbleTextWidth + 52, bubbleTextHeight + 8, 16, 16, 270, 90);
					g2d.drawLine(bubbleTextWidth + 68, 12, bubbleTextWidth + 68, bubbleTextHeight + 16);
					g2d.drawArc(bubbleTextWidth + 52, 4, 16, 16, 0, 90);
					g2d.drawLine(51, 4, bubbleTextWidth + 60, 4);
					g2d.drawArc(43, 4, 16, 16, 90, 60);
				} else {
					g2d.drawRoundRect(43, 4, bubbleTextWidth + 25, bubbleTextHeight + 20, 16, 16);
				}
				// Corner Icon
				if (images[0] != null) g2d.drawImage(images[0], offset[1][0], offset[1][1], images[0].getWidth(this), images[0].getHeight(this), this);
				if (images[1] != null) g2d.drawImage(images[1], bubbleTextWidth + offset[1][2], offset[1][3], images[1].getWidth(this), images[1].getHeight(this), this);
				if (images[2] != null) g2d.drawImage(images[2], offset[1][4], bubbleTextHeight + offset[1][5], images[2].getWidth(this), images[2].getHeight(this), this);
				if (images[3] != null) g2d.drawImage(images[3], bubbleTextWidth + offset[1][6], bubbleTextHeight + offset[1][7], images[3].getWidth(this), images[3].getHeight(this), this);
				break;
			default:
				break;
		}
	}
	
	public String getText() {
		return this.message;
	}
	
	public String toString() {
		return this.message;
	}
	
	private static Color[] getColors(BubbleStyle bubbleStyle) {
		// For current style returns three colors: borderColor, filledColor, textColor
		switch (bubbleStyle) {
		case BASENESS_AN_ATTITUDE:
			return new Color[]{new Color(99, 99, 99), new Color(255, 255, 255), new Color(53, 53, 53)};
		case BIG_CAT:
			return new Color[]{new Color(102, 60, 18), new Color(255, 241, 192), new Color(129, 60, 53)};
		case DEFAULT_BUBBLE:
			return new Color[]{new Color(255, 255, 255), new Color(255, 255, 255), new Color(0, 0, 0)};
		case FORTUEN_CAT:
		case GIRL_AND_SHEEP:
			return new Color[]{new Color(255, 254, 240), new Color(255, 254, 240), new Color(83, 119, 133)};
		case PEACEFUL_SUNNY_DAY:
		case PEACH_BLOSSOMS:
			return new Color[]{new Color(104, 104, 106), new Color(255, 255, 255), new Color(0, 0, 0)};
		case PURE_WHITE_LACE:
			return new Color[]{new Color(253, 139, 162), new Color(255, 255, 255), new Color(253, 139, 162)};
		case SMALL_FLOWER_VINE:
			return new Color[]{new Color(87, 175, 1), new Color(251, 252, 253), new Color(238, 120, 33)};
		case SMILING_HIPPO:
			return new Color[]{new Color(123, 167, 196), new Color(172, 217, 248), new Color(51, 71, 107)};
		case WHITE_WATER:
			return new Color[]{new Color(149, 198, 230), new Color(166, 212, 242), new Color(22, 71, 107)};
		default:
			return new Color[]{new Color(255, 255, 255), new Color(255, 255, 255), new Color(0, 0, 0)};
		}
	}
	
	private static boolean getSharpCorner(BubbleStyle bubbleStyle) {
		// For current style returns whether the bubble contains a sharp angle.
		switch (bubbleStyle) {
		case BASENESS_AN_ATTITUDE:
			return true;
		case BIG_CAT:
			return false;
		case DEFAULT_BUBBLE:
			return true;
		case FORTUEN_CAT:
		case GIRL_AND_SHEEP:
			return false;
		case PEACEFUL_SUNNY_DAY:
		case PEACH_BLOSSOMS:
			return false;
		case PURE_WHITE_LACE:
			return false;
		case SMALL_FLOWER_VINE:
			return false;
		case SMILING_HIPPO:
			return false;
		case WHITE_WATER:
			return true;
		default:
			return true;
		}
	}
	
	private static Image[] getCornerImages(BubbleStyle bubbleStyle) {
		// For current style returns four Images: Left_Above, Right_Above, Left_Below, Right_Below
		switch (bubbleStyle) {
		case BASENESS_AN_ATTITUDE:
			return new Image[] {null,
					new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/baseness_an_attitude_right_above.png")).getImage(),
					null,
					null};
		case BIG_CAT:
			return new Image[] {new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/big_cat_left_above.png")).getImage(),
					new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/big_cat_right_above.png")).getImage(),
					new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/big_cat_left_below.png")).getImage(),
					new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/big_cat_right_below.png")).getImage()};
		case DEFAULT_BUBBLE:
			return new Image[] {null,
					null,
					null,
					null};
		case FORTUEN_CAT:
		case GIRL_AND_SHEEP:
			return new Image[] {new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/girl_and_sheep_left_above.png")).getImage(),
					new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/girl_and_sheep_right_above.png")).getImage(),
					new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/girl_and_sheep_left_below.png")).getImage(),
					new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/girl_and_sheep_right_below.png")).getImage()};
		case PEACEFUL_SUNNY_DAY:
		case PEACH_BLOSSOMS:
			return new Image[] {new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/peach_blossoms_left_above.png")).getImage(),
					new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/peach_blossoms_right_above.png")).getImage(),
					null,
					null};
		case PURE_WHITE_LACE:
			return new Image[] {null,
					new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/pure_white_lace_right_above.png")).getImage(),
					null,
					null};
		case SMALL_FLOWER_VINE:
			return new Image[] {new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/small_flower_vine_left_above.png")).getImage(),
					new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/small_flower_vine_right_above.png")).getImage(),
					new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/small_flower_vine_left_below.png")).getImage(),
					null};
		case SMILING_HIPPO:
			return new Image[] {new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/smiling_hippo_left_above.png")).getImage(),
					new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/smiling_hippo_right_above.png")).getImage(),
					null,
					null};
		case WHITE_WATER:
			return new Image[] {null,
					null,
					null,
					new ImageIcon(JBubble.class.getResource("/cn/compscosys/images/jbubble/white_water_right_below.png")).getImage()};
		default:
			return new Image[] {null,
					null,
					null,
					null};
		}
	}
	
	private static int[][] getOffset(BubbleStyle bubbleStyle) {
		// For current style returns offsets of corner icons:
		// RECEIVER_ALIGNED: Left_Above_X, Left_Above_Y, Right_Above_X, Right_Above_Y, Left_Below_X, Left_Below_Y, Right_Below_X, Right_Below_Y
		// SENDER_ALIGNED: Left_Above_X, Left_Above_Y, Right_Above_X, Right_Above_Y, Left_Below_X, Left_Below_Y, Right_Below_X, Right_Below_Y
		// The unit of offset is pixel(s).
		switch (bubbleStyle) {
		case BASENESS_AN_ATTITUDE:
			return new int[][]{{0, 0, 48, 18, 0, 0, 0, 0}, {0, 0, 48, 0, 0, 0, 0, 0}};
		case BIG_CAT:
			return new int[][]{{38, 22, 49, 19, 40, 33, 59, 33}, {38, 4, 49, 1, 40, 15, 59, 15}};
		case DEFAULT_BUBBLE:
			return new int[][]{{0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}};
		case FORTUEN_CAT:
		case GIRL_AND_SHEEP:
			return new int[][]{{39, 22, 55, 17, 42, 37, 59, 36}, {39, 4, 55, -1, 42, 19, 59, 18}};
		case PEACEFUL_SUNNY_DAY:
		case PEACH_BLOSSOMS:
			return new int[][]{{42, 22, 57, 19, 0, 0, 0, 0}, {42, 4, 57, 1, 0, 0, 0, 0}};
		case PURE_WHITE_LACE:
			return new int[][]{{0, 0, 51, 22, 0, 0, 0, 0}, {0, 0, 51, 4, 0, 0, 0, 0}};
		case SMALL_FLOWER_VINE:
			return new int[][]{{40, 19, 52, 17, 40, 32, 0, 0}, {40, 1, 52, -1, 40, 14, 0, 0}};
		case SMILING_HIPPO:
			return new int[][]{{38, 22, 50, 17, 0, 0, 0, 0}, {38, 4, 50, -1, 0, 0, 0, 0}};
		case WHITE_WATER:
			return new int[][]{{0, 0, 0, 0, 0, 0, 44, 24}, {0, 0, 0, 0, 0, 0, 44, 6}};
		default:
			return new int[][]{{0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0}};
		}
	}
}
