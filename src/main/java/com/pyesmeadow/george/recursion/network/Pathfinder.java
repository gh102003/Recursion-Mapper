package com.pyesmeadow.george.recursion.network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Pathfinder {

	public static PathfindMode PATHFIND_MODE = PathfindMode.COUNT;
	private final Node node1, node2;

	public Pathfinder(Node node1, Node node2)
	{
		this.node1 = node1;
		this.node2 = node2;
	}

	public List<Path> pathfind()
	{
		List<Path> possiblePaths = new ArrayList<>();
		new Recursor(node1, node2, new Path()).calculatePaths(0, possiblePaths);
		System.out.println("Calculated paths (" + possiblePaths.size() + "):");

		possiblePaths.forEach(path ->
		{
			String s = "";
			for (ITraversable component : path.getComponents())
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

		List<Path> shortestPaths = determineShortestPaths(possiblePaths);
		System.out.println("Calculated shortest paths (" + shortestPaths.size() + "):");

		shortestPaths.forEach(path ->
		{
			String s = "";
			for (ITraversable component : path.getComponents())
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
			Path highlightedPath = shortestPaths.get(0);
			highlightedPath.getComponents().forEach(iTraversable -> iTraversable.setSelected(true));
		}

		return shortestPaths;
	}


	private List<Path> determineShortestPaths(List<Path> possiblePaths)
	{
		List<Path> shortestPaths = new ArrayList<>();
		Function<Path, Integer> costCalculator = PATHFIND_MODE.getCostCalculator();

		possiblePaths.forEach(path ->
		{
			// If there are no paths shorter, delete all current shortest paths and add the new shortest path
			if (shortestPaths.isEmpty() || shortestPaths.get(0).calculateCost(costCalculator) > path.calculateCost(costCalculator))
			{
				shortestPaths.clear();
				shortestPaths.add(path);
			}
			// If the shortest path is the same length, add the path to the list of shortest paths
			else if (shortestPaths.get(0).calculateCost(costCalculator) == path.calculateCost(costCalculator))
			{
				shortestPaths.add(path);
			}
		});

		return shortestPaths;
	}

	public enum PathfindMode {
		COUNT("Count", path ->
		{
			int cost = 0;
			for (ITraversable component : path.getComponents())
			{
				if (component instanceof Connection)
				{
					cost++;
				}
			}
			return cost;
		}), PYTHAGOREAN_DISTANCE("Pythagorean distance", path ->
		{
			int cost = 0;
			for (ITraversable component : path.getComponents())
			{
				if (component instanceof Connection)
				{
					Connection connection = (Connection) component;

					Node node1 = connection.node1;
					Node node2 = connection.node2;

					int lengthX = node1.x - node2.x;
					int lengthY = node1.y - node2.y;

					// Calculate distance from centre to point using Pythagoras
					double length = Math.sqrt(Math.pow(lengthX, 2) + Math.pow(lengthY, 2));
					cost += length;
				}
			}
			return Double.valueOf(cost).intValue();
		});

		private final String name;
		private final Function<Path, Integer> costCalculator;

		PathfindMode(String name, Function<Path, Integer> costCalculator)
		{
			this.name = name;
			this.costCalculator = costCalculator;
		}

		public Function<Path, Integer> getCostCalculator()
		{
			return costCalculator;
		}

		public String toString()
		{
			return name;
		}
	}

	private class Recursor {

		private final Node node1, node2;
		private Path currentPath;

		/**
		 * @param node1       the node to start from
		 * @param node2       the target node
		 * @param currentPath the components already traversed in this path
		 */
		public Recursor(Node node1, Node node2, Path currentPath)
		{
			this.node1 = node1;
			this.node2 = node2;
			this.currentPath = currentPath;
		}

		/**
		 * @param pathList the list to add possible paths to
		 */
		private void calculatePaths(int level, List<Path> pathList)
		{
			for (int i = 0; i < level; i++)
			{
				System.out.print(" ");
			}
			System.out.print(node1.id + "\n");

			currentPath.getComponents().add(node1);

			if (node1.equals(node2))
			{
				currentPath.getComponents().add(node1);
				pathList.add(currentPath);
			}
			else
			{
				for (Connection connection : node1.connections)
				{
					Path currentPathClone = (Path) currentPath.clone();

					if (currentPathClone.getComponents().contains(connection)) continue;

					Node next = connection.follow(node1);
					if (currentPathClone.getComponents().contains(next)) continue;

					currentPathClone.getComponents().add(connection);

					Recursor recursor = new Recursor(next, node2, currentPathClone);
					recursor.calculatePaths(level + 1, pathList);

				}
			}
		}
	}
}
