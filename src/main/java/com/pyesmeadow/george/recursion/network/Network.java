package com.pyesmeadow.george.recursion.network;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Network {

	private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
	private static int nodeIDCounter = 0;
	private static int connectionIDCounter = 0;
	private List<Node> nodeList;
	private List<Connection> connectionList;

	public Network()

	{
		nodeList = new ArrayList<Node>();
		connectionList = new ArrayList<Connection>();
	}

	public synchronized char assignNodeID()
	{
		char ID = alphabet.charAt(nodeIDCounter);
		nodeIDCounter++;
		return ID;
	}

	public synchronized char assignConnectionID()
	{
		char ID = alphabet.toUpperCase().charAt(connectionIDCounter);
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

	public void render(Graphics2D g)
	{
		for (int i = nodeList.size() - 1; i >= 0; i--)
		{
			Node node = nodeList.get(i);
			node.render(g);
		}

		for (int i = connectionList.size() - 1; i >= 0; i--)
		{
			Connection path = connectionList.get(i);
			path.render(g);
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
		nodeList.add(node);
	}

	public void addConnection(Connection connection)
	{
		connectionList.add(connection);
	}
}
