package com.pyesmeadow.george.recursion;

import com.pyesmeadow.george.recursion.network.Connection;
import com.pyesmeadow.george.recursion.network.Node;
import com.pyesmeadow.george.recursion.network.Pathfinder;
import com.pyesmeadow.george.recursion.network.io.NetworkManager;
import com.pyesmeadow.george.recursion.ui.CanvasButton;
import com.pyesmeadow.george.recursion.ui.CanvasDropdown;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Random;

public class MouseInput implements MouseInputListener {

	private final NetworkManager networkManager;
	private final List<CanvasButton> buttonList;
	private final List<CanvasDropdown> dropdownList;
	public InteractionMode interactionMode = InteractionMode.ADD;
	@Nullable
	private Node selectedNode;

	public MouseInput(NetworkManager networkManager, List<CanvasButton> buttonList, List<CanvasDropdown> dropdownList)
	{
		this.networkManager = networkManager;
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

		switch (interactionMode)
		{
			case ADD:
			{
				// If a node is clicked, attempt to create a path
				List<Node> nodeList = networkManager.network.getNodeList();
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
							networkManager.network.addConnection(new Connection(selectedNode,
									node,
									networkManager.network.assignConnectionID(),
									networkManager.network));

							deselectAllNodes();
						}
						return;
					}
				}

				deselectAllNodes();

				// If the click was on empty space, attempt to create a node there

				// Randomise nodeName
				Random r = new Random();
				String nodeName = Node.NODE_NAMES[r.nextInt(Node.NODE_NAMES.length)];

				networkManager.network.addNode(new Node(mouseX,
						mouseY,
						nodeName,
						networkManager.network.assignNodeID(),
						networkManager.network));
				break;
			}
			case PATHFIND:
			{
				List<Node> nodeList = networkManager.network.getNodeList();
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
				break;
			}
			case DELETE:
			{
				// If a node is clicked, delete it
				List<Node> nodeList = networkManager.network.getNodeList();
				for (int i1 = nodeList.size() - 1; i1 >= 0; i1--)
				{
					Node node = nodeList.get(i1);
					if (node.containsPoint(mouseX, mouseY))
					{
						// Delete all connected paths
						List<Connection> connections = node.getConnections();
						for (int i = connections.size() - 1; i >= 0; i--)
						{
							Connection connection = connections.get(i);

							// Delete connection from other connected node
							connection.follow(node).removeConnection(connection);

							// Delete connection from connectionList
							networkManager.network.getConnectionList().remove(connection);
						}

						networkManager.network.getNodeList().remove(node);
					}
				}
				break;
			}
			case EDIT:
				for (int i = networkManager.network.getConnectionList().size() - 1; i >= 0; i--)
				{
					Connection connection = networkManager.network.getConnectionList().get(i);

					if (connection.containsPoint(mouseX, mouseY))
					{
						String input = JOptionPane.showInputDialog("Input a new weight for connection " + connection.id,
								connection.getWeight());

						try
						{
							connection.setWeight(Integer.parseInt(input));
						}
						catch (NumberFormatException e1)
						{
							JOptionPane.showMessageDialog(null,
									"Could not parse \"" + input + "\"",
									"Parse error",
									JOptionPane.WARNING_MESSAGE);
						}

						return;
					}
				}
				break;
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
			List<Node> nodeList = networkManager.network.getNodeList();
			for (int i = nodeList.size() - 1; i >= 0; i--)
			{
				Node node = nodeList.get(i);
				if (node.containsPoint(mouseX, mouseY))
				{
					// If the selected node was dragged, move the node
					if (selectedNode == node)
					{
						node.setX(mouseX);
						node.setY(mouseY);
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
		List<Node> nodeList = networkManager.network.getNodeList();
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
		List<Connection> connectionList = networkManager.network.getConnectionList();
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
		ADD("Add"), MOVE("Move"), EDIT("Edit"), PATHFIND("Pathfind"), DELETE("Delete");

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
