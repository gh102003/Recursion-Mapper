package com.pyesmeadow.george.recursion.network;

import com.pyesmeadow.george.recursion.Main;
import com.pyesmeadow.george.recursion.Renderer;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements ITraversable, Serializable {

	public static final String[] NODE_NAMES = {"London", "Norwich", "Paris", "Dubai", "New York", "San Francisco", "Frankfurt", "Berlin", "Washington D.C.", "Moscow", "Beijing", "Sydney", "Kingston", "Toronto", "Bury", "Stowmarket", "Elmswell", "Woolpit", "Rattlesden", "Wetherden", "Norton", "Ixworth", "Thurston", "Haughley"};
	private static final int RADIUS = 12;
	private static final long serialVersionUID = 1L;
	public final String name;
	public final char id;
	private final Network network;
	protected boolean selected = false;
	private int x, y;
	private List<Connection> connections = new ArrayList<Connection>();

	public Node(int x, int y, String name, char id, Network network)
	{
		this.x = x;
		this.y = y;
		this.name = name;
		this.id = id;
		this.network = network;
	}

	public List<Connection> getConnections()
	{
		return connections;
	}

	public void addConnection(Connection connection)
	{
		network.markDirty(true);
		connections.add(connection);
	}

	public boolean removeConnection(Connection connection)
	{
		try
		{
			return connections.remove(connection);
		}
		finally
		{
			network.markDirty(true);
		}
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		network.markDirty(true);
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		network.markDirty(true);
		this.y = y;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public void tick()
	{

	}

	public void render(Graphics2D g, Renderer.RenderMode renderMode)
	{
		g.setColor(Main.main.themeManager.getCurrentTheme().nodeColour);
		g.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
		g.setColor(Color.BLACK);
		// Cross
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		g.drawLine(x - 3, y - 3, x + 3, y + 3);
		g.drawLine(x - 3, y + 3, x + 3, y - 3);
		// Label
		g.drawString(String.valueOf(id), x + RADIUS + 2, y + RADIUS + 2);
		g.drawString(String.valueOf(name), x + RADIUS + 2, y + RADIUS + 12);

		if (selected)
		{
			g.setColor(new Color(255, 255, 255, 140));
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


