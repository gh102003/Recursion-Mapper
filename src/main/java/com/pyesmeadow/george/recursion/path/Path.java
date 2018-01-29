package com.pyesmeadow.george.recursion.path;

import java.util.LinkedList;
import java.util.function.Function;

public class Path implements Cloneable {

	private LinkedList<ITraversable> components = new LinkedList<>();

	private Path(LinkedList<ITraversable> components)
	{
		this.components = components;
	}

	public Path()
	{
		this(new LinkedList<>());
	}

	public LinkedList<ITraversable> getComponents()
	{
		return components;
	}

	public int calculateCost(Function<Path, Integer> costCalculator)
	{
		return costCalculator.apply(this);
	}

	@Override
	public Object clone()
	{
		return new Path((LinkedList<ITraversable>) components.clone());
	}
}
