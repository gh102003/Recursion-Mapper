package com.pyesmeadow.george.recursion.network.io;

import com.pyesmeadow.george.recursion.Main;
import com.pyesmeadow.george.recursion.network.Network;
import com.pyesmeadow.george.recursion.util.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

public class NetworkManager {

	private static final String NETWORK_FILE_EXTENSION = "rmnetwork";
	private final JFileChooser fileChooser;
	public Network network;

	public NetworkManager()
	{
		fileChooser = new JFileChooser();
		network = new Network();

		fileChooser.setFileFilter(new FileNameExtensionFilter("Network files for Recursion Mapper", NETWORK_FILE_EXTENSION));

		try
		{
			fileChooser.setCurrentDirectory(FileUtils.getDataFolder().toFile());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void resetNetwork()
	{
		if (network.isDirty())
		{
			int saveChoice = JOptionPane.showConfirmDialog(Main.main,
					"Do you want to save the current network?",
					"Do you want to save?",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);

			if (saveChoice == JOptionPane.CANCEL_OPTION)
			{
				return;
			}
			else if (saveChoice == JOptionPane.YES_OPTION)
			{
				saveNetworkAs();
			}
		}

		network = new Network();
	}

	public void openNetwork()
	{
		if (network.isDirty())
		{
			int saveChoice = JOptionPane.showConfirmDialog(Main.main,
					"Do you want to save the current network?",
					"Do you want to save?",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);

			if (saveChoice == JOptionPane.CANCEL_OPTION)
			{
				return;
			}
			else if (saveChoice == JOptionPane.YES_OPTION)
			{
				saveNetworkAs();
			}
		}

		int result = fileChooser.showOpenDialog(Main.main);

		if (result == JFileChooser.APPROVE_OPTION)
		{
			Path filePath = fileChooser.getSelectedFile().toPath();
			System.out.println("Loading network: " + filePath.toString());

			try (InputStream inputStream = Files.newInputStream(filePath))
			{
				network = NetworkLoader.readNetwork(inputStream, filePath);
			}
			catch (IOException | ClassNotFoundException e)
			{
				JOptionPane.showMessageDialog(Main.main,
						"Error loading network " + filePath.toString(),
						"Error loading network",
						JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	private void saveNetwork(Path filePath)
	{
		System.out.println("Saving to: " + filePath.toString());

		try (OutputStream outputStream = Files.newOutputStream(filePath))
		{
			NetworkLoader.writeNetwork(network, outputStream);
			network.lastSaveLocation = filePath;
			network.markDirty(false);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void saveNetworkAtLastSaveLocation()
	{
		if (network.lastSaveLocation == null || !network.isDirty()) return;

		saveNetwork(network.lastSaveLocation);
	}

	public void saveNetworkAs()
	{
		int result = fileChooser.showSaveDialog(Main.main);

		if (result == JFileChooser.APPROVE_OPTION)
		{
			// Check file extension
			Path filePath = fileChooser.getSelectedFile().toPath();
			PathMatcher matcher = filePath.getFileSystem().getPathMatcher("glob:**." + NETWORK_FILE_EXTENSION);
			if (!matcher.matches(filePath))
			{
				filePath = filePath.resolveSibling(filePath.getFileName() + "." + NETWORK_FILE_EXTENSION);
			}

			saveNetwork(filePath);
		}
	}
}
