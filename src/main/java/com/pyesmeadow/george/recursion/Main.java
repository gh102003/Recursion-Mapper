package com.pyesmeadow.george.recursion;

import com.pyesmeadow.george.recursion.network.Pathfinder;
import com.pyesmeadow.george.recursion.network.io.NetworkManager;
import com.pyesmeadow.george.recursion.theme.Theme;
import com.pyesmeadow.george.recursion.theme.ThemeLoader;
import com.pyesmeadow.george.recursion.theme.ThemeManager;
import com.pyesmeadow.george.recursion.ui.*;
import com.pyesmeadow.george.recursion.ui.MenuBar;
import com.pyesmeadow.george.recursion.ui.Window;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Canvas implements Runnable {

	public static Main main;
	private static int initialWidth = 1280;
	private static int initialHeight = 720;
	public MouseInput mouseInputListener;
	public ThemeManager themeManager;
	public MenuBar menuBar;
	private NetworkManager networkManager = new NetworkManager();
	private Renderer renderer;
	private List<CanvasButton> buttonList = new ArrayList<>();
	private List<CanvasDropdown> dropdownList = new ArrayList<>();
	private Thread thread;
	private boolean running = false;

	@SuppressWarnings("unchecked") // TODO Remove
	private Main()
	{
		// Initialise themes
		List<Theme> themes = new ArrayList<>();
		try
		{
			themes.add(ThemeLoader.loadDefaultTheme());
		}
		catch (IOException e)
		{
			System.err.println("Could not load default theme");
			e.printStackTrace();
		}
		try
		{
			themes.addAll(ThemeLoader.loadCustomThemes());
		}
		catch (IOException e)
		{
			System.err.println("Could not load custom themes");
			e.printStackTrace();
		}

		themeManager = new ThemeManager(themes);

		Window window = new Window(initialWidth, initialHeight, "Recursion Mapper", this);

		menuBar = new MenuBar(networkManager, themeManager);
		window.frame.setJMenuBar(menuBar);

		window.frame.setVisible(true);

		mouseInputListener = new MouseInput(networkManager, buttonList, dropdownList);
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
				"Render mode",
				Renderer.RenderMode.values(),
				choice -> renderer.renderMode = (Renderer.RenderMode) choice));

		dropdownList.add(new CanvasDropdown(330,
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
				if (mouseInputListener.interactionMode == MouseInput.InteractionMode.PATHFIND)
				{
					return super.containsPoint(pointX, pointY);
				}
				return false;
			}
		});
	}

	public static void main(String args[])
	{
		main = new Main();
	}

	private void tick()
	{
		networkManager.network.tick();
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
		// Init renderer
		renderer = new Renderer(this, themeManager, networkManager, buttonList, dropdownList);

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
				renderer.render();
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
