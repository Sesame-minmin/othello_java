package example;

import java.awt.FontMetrics;
import java.awt.Graphics;

public abstract class Alignment {

	public static int left(String msg, int x, Graphics g) {
		return x;
	}

	public static int center(String msg, int x, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		return x - fm.stringWidth(msg) / 2;
	}

	public static int right(String msg, int x, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		return x - fm.stringWidth(msg);
	}

	public static int base(int y, Graphics g) {
		return y;
	}

	// b + d - (a + d) / 2 = y
	public static int middle(int y, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		return y + (fm.getAscent() - fm.getDescent()) / 2;
	}

	public static int top(int y, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		return y + fm.getAscent();
	}

	public static int bottom(int y, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		return y - fm.getDescent();
	}

}
