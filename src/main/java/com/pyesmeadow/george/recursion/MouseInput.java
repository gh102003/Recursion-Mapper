package com.pyesmeadow.george.recursion;

import com.pyesmeadow.george.recursion.network.Connection;
import com.pyesmeadow.george.recursion.network.Network;
import com.pyesmeadow.george.recursion.network.Node;
import com.pyesmeadow.george.recursion.network.Pathfinder;
import com.pyesmeadow.george.recursion.ui.CanvasButton;
import com.pyesmeadow.george.recursion.ui.CanvasDropdown;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.util.List;

public class MouseInput implements MouseInputListener {

	private final Network network;
	private final List<CanvasButton> buttonList;
	private final List<CanvasDropdown> dropdownList;

	public InteractionMode interactionMode = InteractionMode.ADD;

	@Nullable
	private Node selectedNode;

	public MouseInput(Network network, List<CanvasButton> buttonList, List<CanvasDropdown> dropdownList)
	{
		this.network = network;
		this.buttonList = buttonList;
		this.dropdownList = dropdownList;
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

		// If a dropdown was clicked
		for (int i = dropdownList.size() - 1; i >= 0; i--)
		{
			CanvasDropdown canvasDropdown = dropdownList.get(i);
			if (canvasDropdown.containsPoint(mouseX, mouseY))
			{
				canvasDropdown.click(mouseX, mouseY);

				deselectAll();

				return;
			}
			else
			{
				// Close dropdown if it was not clicked
				canvasDropdown.setDropdownOpen(false);
			}
		}

		// If a button was clicked
		for (int i = buttonList.size() - 1; i >= 0; i--)
		{
			CanvasButton canvasButton = buttonList.get(i);
			if (canvasButton.containsPoint(mouseX, mouseY))
			{
				canvasButton.click();

				deselectAll();

				return;
			}
		}

		if (interactionMode == InteractionMode.ADD)
		{
			// If a node is clicked, attempt to create a path
			List<Node> nodeList = network.getNodeList();
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
						network.getConnectionList().add(new Connection(selectedNode, node, network.assignConnectionID()));

						deselectAllNodes();
					}
					return;
				}
			}

			deselectAllNodes();

			// If the click was on empty space, attempt to create a node there
			network.getNodeList().add(new Node(mouseX, mouseY, network.assignNodeID()));
		}
		else if (interactionMode == InteractionMode.PATHFIND)
		{
			List<Node> nodeList = network.getNodeList();
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
			List<Node> nodeList = network.getNodeList();
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
						network.getConnectionList().remove(connection);
					}

					network.getNodeList().remove(node);
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
			List<Node> nodeList = network.getNodeList();
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

		for (int i = dropdownList.size() - 1; i >= 0; i--)
		{
			CanvasDropdown canvasDropdown = dropdownList.get(i);
			if (canvasDropdown.containsPoint(mouseX, mouseY))
			{
				canvasDropdown.setHover(true);
				return;
			}
			canvasDropdown.setHover(false);
		}

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

	public void deselectAll()
	{
		deselectAllNodes();
		deselectAllConnections();
	}

	public void deselectAllNodes()
	{
		List<Node> nodeList = network.getNodeList();
		int i = 0;
		while (i < nodeList.size())
		{
			Node node = nodeList.get(i);
			node.setSelected(false);
			i++;
		}
		selectedNode = null;
	}

	public void deselectAllConnections()
	{
		List<Connection> connectionList = network.getConnectionList();
		int i = 0;
		while (i < connectionList.size())
		{
			Connection connection = connectionList.get(i);
			connection.setSelected(false);
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

		public String toString()
		{
			return name;
		}
	}
}
