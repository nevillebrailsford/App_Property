package applications.property.gui.modified;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTabbedPane;

import applications.property.gui.PropertyGUIConstants;

public class ColoredTabbedPane extends JTabbedPane {
	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		GradientPaint gp = new GradientPaint(0, 0, PropertyGUIConstants.LIGHT_BLUE, 0, getHeight(), PropertyGUIConstants.SANDY_BROWN);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

}
