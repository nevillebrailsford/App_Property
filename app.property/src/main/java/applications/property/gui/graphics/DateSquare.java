package applications.property.gui.graphics;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.time.LocalDate;
import java.util.Objects;

public class DateSquare extends Rectangle2D.Double {
	private static final long serialVersionUID = 1L;
	public static final double SIZE = 10.0;
	public static final double SPACING = SIZE + 4.0;;

	private double red = 150;
	private double green = 220;
	private double blue = 145;
	private Color color = null;
	private double x;
	private double y;
	private int notifiedCount;
	private int overdueCount;
	private LocalDate date;

	public DateSquare(double x, double y, int notifiedCount, int overdueCount, LocalDate date) {
		setRect(x, y, SIZE, SIZE);
		this.x = x;
		this.y = y;
		this.notifiedCount = notifiedCount;
		this.overdueCount = overdueCount;
		this.date = date;
		if (overdueCount > 0) {
			red = 250;
			green = 145 - (20 * overdueCount < 8 ? overdueCount : 7);
			blue = 145 - (20 * overdueCount < 8 ? overdueCount : 7);
		} else if (notifiedCount > 0) {
			red = 250;
			green = 180 - (5 * notifiedCount < 21 ? notifiedCount : 20);
			blue = 96 - (20 * notifiedCount < 5 ? notifiedCount : 4);
		}
		color = new Color((float) red / 256, (float) green / 256, (float) blue / 256);
	}

	public Color color() {
		return color;
	}

	public boolean intersects(int x, int y) {
		if (x > this.x && x < this.x + SIZE) {
			if (y > this.y && y < this.y + SIZE) {
				return true;
			}
		}
		return false;
	}

	public int getNotifiedCount() {
		return notifiedCount;
	}

	public int getOverdueCount() {
		return overdueCount;
	}

	public LocalDate getDate() {
		return date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(x, y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateSquare other = (DateSquare) obj;
		return java.lang.Double.doubleToLongBits(x) == java.lang.Double.doubleToLongBits(other.x)
				&& java.lang.Double.doubleToLongBits(y) == java.lang.Double.doubleToLongBits(other.y);
	}
}
