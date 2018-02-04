package com.pyesmeadow.george.recursion;

import com.pyesmeadow.george.recursion.network.Connection;
import com.pyesmeadow.george.recursion.network.Node;
import com.pyesmeadow.george.recursion.network.Pathfinder;
import com.pyesmeadow.george.recursion.ui.CanvasButton;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class Main extends Canvas implements Runnable {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	// For debugging
	private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
	private static int nodeIDCounter = 0;
	private static int connectionIDCounter = 0;
	public List<CanvasButton> buttonList = new ArrayList<CanvasButton>();
	public List<Node> nodeList = new ArrayList<Node>();
	public List<Connection> connectionList = new ArrayList<Connection>();
	private Thread thread;
	private boolean running = false;

	public Main()
	{
		new Window(WIDTH, HEIGHT, "Recursion Map", this);

		MouseInput mouseInputListener = new MouseInput(this);
		addMouseListener(mouseInputListener);
		addMouseMotionListener(mouseInputListener);

		nodeList.add(new Node(200, 300, assignNodeID()));
		nodeList.add(new Node(600, 480, assignNodeID()));

		connectionList.add(new Connection(nodeList.get(0), nodeList.get(1), assignConnectionID()));

		buttonList.add(new CanvasButton(WIDTH - 60,
				HEIGHT - 60,
				50,
				"Mode: " + mouseInputListener.interactionMode.getName(),
				button ->
				{
					mouseInputListener.deselectAllNodes();

					MouseInput.InteractionMode[] values = MouseInput.InteractionMode.values();

					for (int i = 0; i < values.length; i++)
					{
						MouseInput.InteractionMode interactionMode = values[i];
						if (mouseInputListener.interactionMode == interactionMode)
						{
							mouseInputListener.interactionMode = values[(i + 1) % values.length];

							button.setText("Mode: " + mouseInputListener.interactionMode.getName());
							return;
						}
					}
				}));

		buttonList.add(new CanvasButton(WIDTH - 60,
				HEIGHT - 200,
				50,
				"PM: " + Pathfinder.PATHFIND_MODE.getName(),
				button ->
				{
					Pathfinder.PathfindMode[] values = Pathfinder.PathfindMode.values();

					for (int i = 0; i < values.length; i++)
					{
						Pathfinder.PathfindMode pathfindMode = values[i];
						if (Pathfinder.PATHFIND_MODE == pathfindMode)
						{
							Pathfinder.PATHFIND_MODE = values[(i + 1) % values.length];

							button.setText("PM: " + Pathfinder.PATHFIND_MODE.getName());
							return;
						}
					}
				}));
	}

	public static synchronized char assignNodeID()
	{
		char ID = alphabet.charAt(nodeIDCounter);
		nodeIDCounter++;
		return ID;
	}

	public static synchronized char assignConnectionID()
	{
		char ID = alphabet.toUpperCase().charAt(connectionIDCounter);
		connectionIDCounter++;
		return ID;
	}

	public static void main(String args[])
	{
		new Main();
	}

	public void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}

		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);

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

		for (int i = buttonList.size() - 1; i >= 0; i--)
		{
			CanvasButton button = buttonList.get(i);
			button.render(g);
		}

		g.dispose();
		bs.show();
	}

	public void tick()
	{
		nodeList.forEach(node -> node.tick());
		connectionList.forEach(path -> path.tick());
		buttonList.forEach(button -> button.tick());
	}

	public synchronized void start()
	{
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop()
	{
		try
		{
			thread.join();
			running = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{

		long lastTickTime = System.nanoTime();
		double ticksPerSecond = 60.0;
		double nsBetweenTicks = 1000000000 / ticksPerSecond; // Measured in ticks
		double sinceLastTick = 0;

		// Determine refresh rate
		int fpsLimit = 60;
		try
		{
			fpsLimit = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
		}
		catch (Exception e)
		{
			System.err.println("Cannot determine refresh rate. Setting FPS limit to 60");
		}

		System.out.println("FPS limit: " + fpsLimit);

		long lastFrameTime = System.nanoTime();
		double nsBetweenFrames = 1000000000 / fpsLimit; // Measured in frames
		double sinceLastFrame = 0;
		int frameCounter = 0;

		long timer = System.currentTimeMillis();

		while (running)
		{
			long now = System.nanoTime();

			sinceLastTick += (now - lastTickTime) / nsBetweenTicks;
			lastTickTime = now;

			sinceLastFrame += (now - lastFrameTime) / nsBetweenFrames;
			lastFrameTime = now;

			while (sinceLastTick >= 1)
			{
				tick();
				sinceLastTick--;
			}

			if (sinceLastFrame >= 1 && running)
			{
				sinceLastFrame = 0;
				render();
				frameCounter++;
			}

			if (System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				System.out.println("FPS: " + frameCounter);
				frameCounter = 0;
			}
		}
		stop();
	}
}
