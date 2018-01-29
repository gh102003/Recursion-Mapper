package com.pyesmeadow.george.recursion.path;

import java.awt.*;

public class Connection implements ITraversable {

	private final Node node1, node2;
	public char id;

	protected boolean selected = false;

	public Connection(Node node1, Node node2, char id)
	{
		this.node1 = node1;
		this.node2 = node2;
		this.id = id;

		node1.connections.add(this);
		node2.connections.add(this);
	}

	public void tick()
	{

	}

	public void render(Graphics2D g)
	{
		g.setColor(new Color(220, 0, 0));
		g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawLine(node1.x, node1.y, node2.x, node2.y);
		g.drawString(String.valueOf(id), (node1.x + node2.x) / 2, (node1.y + node2.y) / 2);

		if (selected)
		{
			g.setColor(new Color(255, 255, 255, 180));
			g.drawLine(node1.x, node1.y, node2.x, node2.y);
		}
	}

	/**
	 * Follows this connection to the node at the other end
	 *
	 * @param node a node at one end of this connection
	 * @return the other node
	 * @throws IllegalArgumentException if <code>node</code> is not part of this connection
	 */
	public Node follow(Node node)
	{
		if (node.equals(node1))
		{
			return node2;
		}
		else if (node.equals(node2))
		{
			return node1;
		}
		else
		{
			throw new IllegalArgumentException("The node must be at one end of this connection");
		}
	}

	@Override
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Connection)
		{
			Connection path = (Connection) obj;
			if (path.node1 == node1 && path.node2 == node2)
			{
				return true;
			}

			if (path.node1 == node2 && path.node2 == node1)
			{
				return true;
			}

			return false;
		}

		return super.equals(obj);
	}
}
