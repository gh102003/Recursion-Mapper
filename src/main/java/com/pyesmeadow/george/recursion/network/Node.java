package com.pyesmeadow.george.recursion.network;

import com.pyesmeadow.george.recursion.Renderer;
import com.pyesmeadow.george.recursion.input.MouseInput;
import com.pyesmeadow.george.recursion.theme.Theme;
import com.pyesmeadow.george.recursion.theme.ThemeManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node implements ITraversable, Serializable {

	public static final String[] NODE_NAMES = {"London", "Norwich", "Paris", "Dubai", "New York", "San Francisco", "Frankfurt", "Berlin", "Washington D.C.", "Moscow", "Beijing", "Sydney", "Kingston", "Toronto", "Bury", "Stowmarket", "Elmswell", "Woolpit", "Rattlesden", "Wetherden", "Norton", "Ixworth", "Thurston", "Haughley"};
	//	private static final int RADIUS = 12;
	private static final long serialVersionUID = 1L;
	public final char id;
	private final Network network;
	protected boolean selected = false;
	private int x, y;
	private String name;
	private int weight;
	private int radius = 12;
	private List<Connection> connections = new ArrayList<Connection>();

	public Node(int x, int y, String name, int weight, char id, Network network)
	{
		this.x = x;
		this.y = y;
		this.weight = weight;
		this.name = name;
		this.id = id;
		this.network = network;
	}

	public Node(int x, int y, String name, char id, Network network)
	{
		this(x, y, name, new Random().nextInt(1000), id, network);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		network.markDirty(true);
		this.name = name;
	}

	public List<Connection> getConnections()
	{
		return connections;
	}

	public int getWeight()
	{
		return weight;
	}

	public void setWeight(int weight)
	{
		network.markDirty(true);
		this.weight = weight;
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

	public void render(Graphics2D g,
					   Renderer.RenderMode renderMode,
					   MouseInput.InteractionMode interactionMode,
					   ThemeManager themeManager)
	{
		if (renderMode == Renderer.RenderMode.TEXTURED)
		{
			BufferedImage settlementTiny = themeManager.getTexture(Theme.ThemeTextureType.SETTLEMENT_TINY);
			BufferedImage settlementSmall = themeManager.getTexture(Theme.ThemeTextureType.SETTLEMENT_SMALL);
			BufferedImage settlementMedium = themeManager.getTexture(Theme.ThemeTextureType.SETTLEMENT_MEDIUM);
			BufferedImage settlementLarge = themeManager.getTexture(Theme.ThemeTextureType.SETTLEMENT_LARGE);
			BufferedImage settlementGiant = themeManager.getTexture(Theme.ThemeTextureType.SETTLEMENT_GIANT);

			BufferedImage texture;
			if (weight < 200) texture = settlementTiny;
			else if (weight < 400) texture = settlementSmall;
			else if (weight < 600) texture = settlementMedium;
			else if (weight < 800) texture = settlementLarge;
			else texture = settlementGiant;

			g.drawImage(texture, x - (texture.getWidth() / 2), y - (texture.getHeight() / 2), null);
		}
		else
		{
			// Calculate radius based on renderMode and weight
			if (renderMode == Renderer.RenderMode.DYNAMIC)
			{
				radius = weight / 50 + 7;
			}

			g.setColor(themeManager.getCurrentTheme().nodeColour);
			g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
			g.setColor(Color.BLACK);
			// Cross
			g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
			g.drawLine(x - 3, y - 3, x + 3, y + 3);
			g.drawLine(x - 3, y + 3, x + 3, y - 3);
		}

		// Labels
		g.drawString(String.valueOf(id), x + radius + 2, y + radius + 2);
		g.drawString(name, x + radius + 2, y + radius + 15);
		if (interactionMode == MouseInput.InteractionMode.EDIT)
		{
			g.drawString("Weight: " + String.valueOf(weight), x + radius + 2, y + radius + 28);
		}

		if (selected)
		{
			g.setColor(new Color(255, 255, 255, 140));
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


