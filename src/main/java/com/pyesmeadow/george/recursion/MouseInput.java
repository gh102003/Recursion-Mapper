package com.pyesmeadow.george.recursion;

import com.pyesmeadow.george.recursion.network.Connection;
import com.pyesmeadow.george.recursion.network.Node;
import com.pyesmeadow.george.recursion.network.Pathfinder;
import com.pyesmeadow.george.recursion.ui.CanvasButton;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.util.List;

public class MouseInput implements MouseInputListener {

	private final Main canvas;

	public InteractionMode interactionMode = InteractionMode.ADD;

	@Nullable
	private Node selectedNode;

	public MouseInput(Main canvas)
	{
		this.canvas = canvas;
	}

	private static boolean isPointInsideSquareRegion(int pointX, int pointY, int x1, int y1, int x2, int y2)
	{
		return pointX >= x1 && pointX <= x2 && pointY >= y1 && pointY <= y2;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		int mouseX = e.getX();
		int mouseY = e.getY();

		// If a button was clicked
		List<CanvasButton> buttonList = canvas.buttonList;
		for (int i = buttonList.size() - 1; i >= 0; i--)
		{
			CanvasButton canvasButton = buttonList.get(i);
			if (canvasButton.containsPoint(mouseX, mouseY))
			{
				canvasButton.click();

				deselectAllNodes();

				return;
			}
		}

		if (interactionMode == InteractionMode.ADD)
		{
			// If a node is clicked, attempt to create a path
			List<Node> nodeList = canvas.nodeList;
			for (int i = nodeList.size() - 1; i >= 0; i--)
			{
				Node node = nodeList.get(i);
				if (node.containsPoint(mouseX, mouseY))
				{
					// If no node is selected, select the node that was just clicked
					if (selectedNode == null)
					{
						node.setSelected(true);
						selectedNode = node;
					}
					else if (selectedNode == node)
					{
						deselectAllNodes();
					}
					else
					{
						// If another node is selected, create a new path and clear the selection
						canvas.connectionList.add(new Connection(selectedNode, node, Main.assignConnectionID()));

						deselectAllNodes();
					}
					return;
				}
			}

			deselectAllNodes();

			// If the click was on empty space, attempt to create a node there
			canvas.nodeList.add(new Node(mouseX, mouseY, Main.assignNodeID()));
		}
		else if (interactionMode == InteractionMode.PATHFIND)
		{
			List<Node> nodeList = canvas.nodeList;
			for (int i = nodeList.size() - 1; i >= 0; i--)
			{
				Node node = nodeList.get(i);
				if (node.containsPoint(mouseX, mouseY))
				{
					// If no node is selected, select the node that was just clicked
					if (selectedNode == null)
					{
						node.setSelected(true);
						selectedNode = node;
					}
					else
					{
						// If another node is selected, pathfind
						new Pathfinder(selectedNode, node).pathfind();

						deselectAllNodes();
					}
					return;
				}
			}

			deselectAllNodes();
		}
		else if (interactionMode == InteractionMode.DELETE)
		{
			// If a node is clicked, delete it
			List<Node> nodeList = canvas.nodeList;
			for (int i1 = nodeList.size() - 1; i1 >= 0; i1--)
			{
				Node node = nodeList.get(i1);
				if (node.containsPoint(mouseX, mouseY))
				{
					// Delete all connected paths
					List<Connection> connections = node.connections;
					for (int i = connections.size() - 1; i >= 0; i--)
					{
						Connection connection = connections.get(i);

						// Delete connection from other connected node
						connection.follow(node).connections.remove(connection);

						// Delete connection from connectionList
						canvas.connectionList.remove(connection);
					}

					canvas.nodeList.remove(node);
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// If the mouse has been released in move mode, deselect all nodes
		if (interactionMode == InteractionMode.MOVE)
		{
			deselectAllNodes();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{}

	@Override
	public void mouseExited(MouseEvent e)
	{}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		int mouseX = e.getX();
		int mouseY = e.getY();

		if (interactionMode == InteractionMode.MOVE)
		{
			List<Node> nodeList = canvas.nodeList;
			for (int i = nodeList.size() - 1; i >= 0; i--)
			{
				Node node = nodeList.get(i);
				if (node.containsPoint(mouseX, mouseY))
				{
					// If the selected node was dragged, move the node
					if (selectedNode == node)
					{
						node.x = mouseX;
						node.y = mouseY;
					}
					// If a different node was clicked, deselect all nodes and select
					else
					{
						deselectAllNodes();

						// Select the current node
						selectedNode = node;
						node.setSelected(true);
					}
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		int mouseX = e.getX();
		int mouseY = e.getY();

		List<CanvasButton> buttonList = canvas.buttonList;
		for (int i = buttonList.size() - 1; i >= 0; i--)
		{
			CanvasButton canvasButton = buttonList.get(i);
			if (canvasButton.containsPoint(mouseX, mouseY))
			{
				canvasButton.setHover(true);
				return;
			}
			canvasButton.setHover(false);
		}
	}

	public void deselectAllNodes()
	{
		List<Node> nodeList = canvas.nodeList;
		int i = 0;
		while (i < nodeList.size())
		{
			Node node = nodeList.get(i);
			node.setSelected(false);
			i++;
		}
		selectedNode = null;
	}

	public enum InteractionMode {
		ADD("Add"), MOVE("Move"), PATHFIND("Pathfind"), DELETE("Delete");

		private final String name;

		InteractionMode(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}
	}
}
