package com.pyesmeadow.george.recursion.network;

import com.pyesmeadow.george.recursion.Main;
import com.pyesmeadow.george.recursion.Renderer;

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
		char ID = alphabet.charAt(nodeIDCounter);
		nodeIDCounter++;

		// If counter at end, loop back to start
		if (nodeIDCounter >= alphabet.length()) nodeIDCounter = 0;

		return ID;
	}

	public synchronized char assignConnectionID()
	{
		char ID = alphabet.toUpperCase().charAt(connectionIDCounter);
		connectionIDCounter++;

		// If counter at end, loop back to start
		if (connectionIDCounter >= alphabet.length()) connectionIDCounter = 0;

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

	public void render(Graphics2D g, Renderer.RenderMode renderMode)
	{
		for (int i = connectionList.size() - 1; i >= 0; i--)
		{
			Connection connection = connectionList.get(i);
			connection.render(g, renderMode);
		}

		for (int i = nodeList.size() - 1; i >= 0; i--)
		{
			Node node = nodeList.get(i);
			node.render(g, renderMode);
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
