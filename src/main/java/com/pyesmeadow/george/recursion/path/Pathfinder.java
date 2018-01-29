package com.pyesmeadow.george.recursion.path;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Pathfinder {

	private final Node node1, node2;

	public Pathfinder(Node node1, Node node2)
	{
		this.node1 = node1;
		this.node2 = node2;
	}

	public List<LinkedList<ITraversable>> pathfind()
	{
		List<LinkedList<ITraversable>> possiblePaths = new ArrayList<>();
		new Recursor(node1, node2, new LinkedList<>()).calculatePaths(0, possiblePaths);
		System.out.println("Calculated paths (" + possiblePaths.size() + "):");

		possiblePaths.forEach(path ->
		{
			String s = "";
			for (ITraversable component : path)
			{
				if (component instanceof Node)
				{
					s = s.concat(String.valueOf(((Node) component).id));
				}
				else if (component instanceof Connection)
				{
					s = s.concat(String.valueOf(((Connection) component).id));
				}
			}

			System.out.println(s);
		});

		List<LinkedList<ITraversable>> shortestPaths = determineShortestPaths(possiblePaths);
		System.out.println("Calculated shortest paths (" + shortestPaths.size() + "):");

		shortestPaths.forEach(path ->
		{
			String s = "";
			for (ITraversable component : path)
			{
				if (component instanceof Node)
				{
					s = s.concat(String.valueOf(((Node) component).id));
				}
				else if (component instanceof Connection)
				{
					s = s.concat(String.valueOf(((Connection) component).id));
				}
			}

			System.out.println(s);
		});

		// Highlight the first shortest path
		if (shortestPaths.size() > 0)
		{
			LinkedList<ITraversable> highlightedPath = shortestPaths.get(0);
			highlightedPath.forEach(iTraversable -> iTraversable.setSelected(true));
		}

		return shortestPaths;
	}


	private List<LinkedList<ITraversable>> determineShortestPaths(List<LinkedList<ITraversable>> possiblePaths)
	{
		List<LinkedList<ITraversable>> shortestPaths = new ArrayList<>();

		possiblePaths.forEach(path ->
		{
			// If there are no paths shorter, delete all current shortest paths and add the new shortest path
			if (shortestPaths.isEmpty() || shortestPaths.get(0).size() > path.size())
			{
				shortestPaths.clear();
				shortestPaths.add(path);
			}
			// If the shortest path is the same length, add the path to the list of shortest paths
			else if (shortestPaths.get(0).size() == path.size())
			{
				shortestPaths.add(path);
			}
		});

		return shortestPaths;
	}

	private class Recursor {

		private final Node node1, node2;
		private LinkedList<ITraversable> currentPath;

		/**
		 * @param node1       the node to start from
		 * @param node2       the target node
		 * @param currentPath the components already traversed in this path
		 */
		public Recursor(Node node1, Node node2, LinkedList<ITraversable> currentPath)
		{
			this.node1 = node1;
			this.node2 = node2;
			this.currentPath = currentPath;
		}

		/**
		 * @param pathList the list to add possible paths to
		 */
		private void calculatePaths(int level, List<LinkedList<ITraversable>> pathList)
		{
			for (int i = 0; i < level; i++)
			{
				System.out.print(" ");
			}
			System.out.print(node1.id + "\n");

			currentPath.add(node1);

			if (node1.equals(node2))
			{
				currentPath.add(node1);
				pathList.add(currentPath);
			}
			else
			{
				for (Connection connection : node1.connections)
				{
					LinkedList<ITraversable> currentPathClone = (LinkedList<ITraversable>) currentPath.clone();

					if (currentPathClone.contains(connection)) continue;

					Node next = connection.follow(node1);
					if (currentPathClone.contains(next)) continue;

					currentPathClone.add(connection);

					Recursor recursor = new Recursor(next, node2, currentPathClone);
					recursor.calculatePaths(level + 1, pathList);

				}
			}
		}
	}
}
