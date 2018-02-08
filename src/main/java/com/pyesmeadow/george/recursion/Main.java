package com.pyesmeadow.george.recursion;

import com.pyesmeadow.george.recursion.network.Network;
import com.pyesmeadow.george.recursion.network.Pathfinder;
import com.pyesmeadow.george.recursion.ui.CanvasButton;
import com.pyesmeadow.george.recursion.ui.CanvasDropdown;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class Main extends Canvas implements Runnable {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	public Network network = new Network();
	public List<CanvasButton> buttonList = new ArrayList<>();
	public List<CanvasDropdown> dropdownList = new ArrayList<>();
	private Thread thread;
	private boolean running = false;

	private Main()
	{
		new Window(WIDTH, HEIGHT, "Recursion Mapper", this);

		MouseInput mouseInputListener = new MouseInput(network, buttonList, dropdownList);
		addMouseListener(mouseInputListener);
		addMouseMotionListener(mouseInputListener);

		dropdownList.add(new CanvasDropdown(10,
				10,
				150,
				40,
				30,
				"Mode",
				MouseInput.InteractionMode.values(),
				choice -> mouseInputListener.interactionMode = (MouseInput.InteractionMode) choice));

		dropdownList.add(new CanvasDropdown(170,
				10,
				150,
				40,
				30,
				"Pathfind mode",
				Pathfinder.PathfindMode.values(),
				choice -> Pathfinder.PATHFIND_MODE = (Pathfinder.PathfindMode) choice) {
			@Override
			public void render(Graphics2D g)
			{
				if (mouseInputListener.interactionMode == MouseInput.InteractionMode.PATHFIND) super.render(g);
			}

			@Override
			public boolean containsPoint(int pointX, int pointY)
			{
				if (mouseInputListener.interactionMode == MouseInput.InteractionMode.PATHFIND) return super.containsPoint(pointX, pointY);
				return false;
			}
		});
	}

	public static void main(String args[])
	{
		new Main();
	}

	private void render()
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

		network.render(g);

		for (int i = buttonList.size() - 1; i >= 0; i--)
		{
			buttonList.get(i).render(g);
		}

		for (int i = dropdownList.size() - 1; i >= 0; i--)
		{
			dropdownList.get(i).render(g);
		}

		g.dispose();
		bs.show();
	}

	private void tick()
	{
		network.tick();
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
