package com.pyesmeadow.george.recursion.ui;

import com.pyesmeadow.george.recursion.Main;

import javax.swing.*;
import java.awt.*;

public class Window {

	public JFrame frame;

	public Window(int width, int height, String title, Main main)
	{
		frame = new JFrame(title);

		frame.setMinimumSize(new Dimension(1280, 720));

		Dimension windowSize = new Dimension(width, height);

		frame.getContentPane().setPreferredSize(windowSize);
		frame.getContentPane().setMaximumSize(windowSize);
		frame.getContentPane().setMinimumSize(windowSize);

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e)
		{
			e.printStackTrace();
		}
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(main);
		main.start();
	}
}
