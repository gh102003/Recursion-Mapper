package com.pyesmeadow.george.recursion.network;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Node implements ITraversable {

	public static final int RADIUS = 12;
	public int x, y;
	public List<Connection> connections = new ArrayList<Connection>();
	protected boolean selected = false;
	public char id;

	public Node(int x, int y, char id)
	{
		this.x = x;
		this.y = y;
		this.id = id;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public void tick()
	{

	}

	public void render(Graphics2D g)
	{
		g.setColor(new Color(0, 150, 255));
		g.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		g.drawLine(x - 3, y - 3, x + 3, y + 3);
		g.drawLine(x - 3, y + 3, x + 3, y - 3);
		g.drawString(String.valueOf(id), x + RADIUS + 2, y + RADIUS + 2);

		if (selected)
		{
			g.setColor(new Color(255, 255, 255, 180));
			g.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
		}
	}

	public boolean containsPoint(int pointX, int pointY)
	{
		int deltaX = pointX - x;
		int deltaY = pointY - y;

		// Calculate distance from centre to point using Pythagoras
		double delta = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

		// If the distance is greater than the radius, return false;
		return delta <= RADIUS;
	}
}


