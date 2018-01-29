package com.pyesmeadow.george.recursion.ui;

import java.awt.*;
import java.util.function.Consumer;

public class CanvasButton {

	public final int x, y, radius;
	private final Consumer<CanvasButton> onClick;
	protected String text;
	protected boolean hover = false;

	public CanvasButton(int x, int y, int radius, String text, Consumer<CanvasButton> onClick)
	{
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.text = text;
		this.onClick = onClick;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public void click()
	{
		onClick.accept(this);
	}

	public void tick()
	{

	}

	public void setHover(boolean hover)
	{
		this.hover = hover;
	}

	public void render(Graphics2D g)
	{
		g.setColor(new Color(0, 0, 0, 180));
		g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.fillOval(x - radius, y - radius, radius * 2, radius * 2);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		g.drawString(text, x - radius + 10, y + 10);

		if (hover)
		{
			g.setColor(new Color(255, 255, 255, 80));
			g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
		}
	}

	public boolean containsPoint(int pointX, int pointY)
	{
		int deltaX = pointX - x;
		int deltaY = pointY - y;

		// Calculate distance from centre to point using Pythagoras
		double delta = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

		// If the distance is greater than the radius, return false;
		return delta <= radius;
	}
}
