package com.pyesmeadow.george.recursion.network.io;

import com.pyesmeadow.george.recursion.network.Network;

import java.io.*;
import java.nio.file.Path;

public class NetworkLoader {

	/**
	 * @param inputStream an input stream containing a Network
	 * @param path a path strictly for setting a last save location (this is never read from)
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	static Network readNetwork(InputStream inputStream, Path path) throws ClassNotFoundException, IOException
	{
		try (inputStream; ObjectInputStream objectInputStream = new ObjectInputStream(inputStream))
		{
			Network network = (Network) objectInputStream.readObject();
			network.lastSaveLocation = path;
			return network;
		}
	}

	static void writeNetwork(Network network, OutputStream outputStream) throws IOException
	{
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream))
		{
			objectOutputStream.writeObject(network);
		}
	}
}
