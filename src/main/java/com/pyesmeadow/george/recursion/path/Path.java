package com.pyesmeadow.george.recursion.path;

import java.util.LinkedList;
import java.util.function.Function;

public class Path {

	private LinkedList<ITraversable> components = new LinkedList<>();

	public int calculateCost(Function<Path, Integer> costCalculator)
	{
		return costCalculator.apply(this);
	}

	public boolean add(ITraversable iTraversable)
	{
		return components.add(iTraversable);
	}
}
