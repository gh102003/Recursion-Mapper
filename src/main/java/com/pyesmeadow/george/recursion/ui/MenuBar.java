package com.pyesmeadow.george.recursion.ui;

import com.pyesmeadow.george.recursion.network.io.NetworkManager;
import com.pyesmeadow.george.recursion.theme.ThemeManager;
import com.pyesmeadow.george.recursion.util.FileUtils;

import javax.swing.*;
import java.io.IOException;

public class MenuBar extends JMenuBar {

	public JMenuItem menuItemSave;
	private JMenu menuFile;
	private JMenuItem menuItemNew;
	private JMenuItem menuItemOpen;
	private JMenuItem menuItemSaveAs;

	private JMenu menuTheme;
	private JMenuItem menuItemOpenThemesFolder;
	private JMenu subMenuChooseTheme;

	public MenuBar(NetworkManager networkManager, ThemeManager themeManager)
	{
		//region File Menu
		menuFile = new JMenu("File");

		menuItemNew = new JMenuItem("New", UIManager.getIcon("FileView.fileIcon"));
		menuItemNew.addActionListener(e -> networkManager.resetNetwork());

		menuItemOpen = new JMenuItem("Open...", UIManager.getIcon("FileView.directoryIcon"));
		menuItemOpen.addActionListener(e -> networkManager.openNetwork());

		menuItemSave = new JMenuItem("Save", UIManager.getIcon("FileView.floppyDriveIcon"));
		menuItemSave.addActionListener(e -> networkManager.saveNetworkAtLastSaveLocation());
		menuItemSave.setEnabled(false);

		menuItemSaveAs = new JMenuItem("Save as...", UIManager.getIcon("FileView.floppyDriveIcon"));
		menuItemSaveAs.addActionListener(e -> networkManager.saveNetworkAs());

		menuFile.add(menuItemNew);
		menuFile.add(menuItemOpen);
		menuFile.addSeparator();
		menuFile.add(menuItemSave);
		menuFile.add(menuItemSaveAs);
		//endregion

		//region Theme Menu
		menuTheme = new JMenu("Theme");

		menuItemOpenThemesFolder = new JMenuItem("Open themes folder", UIManager.getIcon("FileView.directoryIcon"));
		menuItemOpenThemesFolder.addActionListener(e ->
		{
			try
			{
				FileUtils.openDirectoryInExplorer(FileUtils.getThemesFolder());
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		});

		menuTheme.add(menuItemOpenThemesFolder);

		// Generate theme choices
		subMenuChooseTheme = new JMenu("Choose theme");
		ButtonGroup themeChoices = new ButtonGroup();
		themeManager.getLoadedThemes().forEach(theme ->
		{
			ThemeRadioButtonMenuItem themeChoice = new ThemeRadioButtonMenuItem(theme, themeManager);

			themeChoices.add(themeChoice);
			subMenuChooseTheme.add(themeChoice);
		});

		// Selecr first (deafult) theme
		themeChoices.getElements().nextElement().setSelected(true);

		menuTheme.add(subMenuChooseTheme);
		//endregion

		add(menuFile);
		add(menuTheme);

		setVisible(true);
	}
}
