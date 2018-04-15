package com.pyesmeadow.george.recursion.network;

import com.pyesmeadow.george.recursion.Main;
import com.pyesmeadow.george.recursion.Renderer;
import com.pyesmeadow.george.recursion.input.MouseInput;
import com.pyesmeadow.george.recursion.theme.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Network implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
	private static int nodeIDCounter = 0;
	private static int connectionIDCounter = 0;
	public transient Path lastSaveLocation = null;
	private List<Node> nodeList;
	private List<Connection> connectionList;
	private boolean dirty = false;

	public Network()
	{
		nodeList = new ArrayList<>();
		connectionList = new ArrayList<>();

		if (Main.main != null && Main.main.menuBar != null)
		{
			Main.main.menuBar.menuItemSave.setEnabled(false);
		}
	}

	public synchronized char assignNodeID()
	{
		// Warn when reaching end of alphabet
		if (nodeIDCounter == alphabet.length())
		{
			JOptionPane.showMessageDialog(null,
					"You have reached the end of the alphabet, so node IDs will now be repeated.",
					"Node IDs will now be repeated",
					JOptionPane.WARNING_MESSAGE);
		}

		char ID = alphabet.charAt(nodeIDCounter % alphabet.length());
		nodeIDCounter++;

		return ID;
	}

	public synchronized char assignConnectionID()
	{
		// Warn when reaching end of alphabet
		if (connectionIDCounter == alphabet.length())
		{
			JOptionPane.showMessageDialog(null,
					"You have reached the end of the alphabet, so connection IDs will now be reapeated.",
					"Connection IDs will now be repeated",
					JOptionPane.WARNING_MESSAGE);
		}

		char ID = alphabet.toUpperCase().charAt(connectionIDCounter % alphabet.length());
		connectionIDCounter++;

		return ID;
	}

	public List<Node> getNodeList()
	{
		return nodeList;
	}

	public List<Connection> getConnectionList()
	{
		return connectionList;
	}

	public void render(Graphics2D g, Renderer.RenderMode renderMode, MouseInput.InteractionMode interactionMode, ThemeManager themeManager)
	{
		for (int i = connectionList.size() - 1; i >= 0; i--)
		{
			Connection connection = connectionList.get(i);
			connection.render(g, renderMode, interactionMode, themeManager);
		}

		for (int i = nodeList.size() - 1; i >= 0; i--)
		{
			Node node = nodeList.get(i);
			node.render(g, renderMode, interactionMode, themeManager);
		}
	}

	public void tick()
	{
		for (int i = nodeList.size() - 1; i >= 0; i--)
		{
			Node node = nodeList.get(i);
			node.tick();
		}

		for (int i = connectionList.size() - 1; i >= 0; i--)
		{
			Connection path = connectionList.get(i);
			path.tick();
		}
	}

	public void addNode(Node node)
	{
		markDirty(true);
		nodeList.add(node);
	}

	public void addConnection(Connection connection)
	{
		markDirty(true);
		connectionList.add(connection);
	}

	public void markDirty(boolean isDirty)
	{
		Main.main.menuBar.menuItemSave.setEnabled(isDirty && this.lastSaveLocation != null);
		dirty = isDirty;
	}

	public boolean isDirty()
	{
		return dirty;
	}
}
