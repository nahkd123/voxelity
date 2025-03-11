package io.github.nahkd123.voxelity.server.editor;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Set;

import io.github.nahkd123.voxelity.client.editor.EditRequest;
import io.github.nahkd123.voxelity.editor.VoxelityEditor;
import io.github.nahkd123.voxelity.server.VoxelityServer;
import io.github.nahkd123.voxelity.server.editor.history.ServerHistoryQueue;
import io.github.nahkd123.voxelity.server.world.ServerWorld;
import io.github.nahkd123.voxelity.world.World;

/**
 * <p>
 * Represent server-side editor. The methods in this editor object are usually
 * remotely called by client (with exception of editor script, which evaluates
 * on server).
 * </p>
 * <p>
 * Each editor instance is bounds to one connection between client and server.
 * In the case of server editor, it is attached directly to connection object.
 * </p>
 * 
 * @see #loadEditorData(EditorPaths)
 * @see #saveEditorData(EditorPaths)
 */
public abstract class ServerVoxelityEditor implements VoxelityEditor {
	private ServerHistoryQueue history = new ServerHistoryQueue();

	/**
	 * <p>
	 * Get the server that this editor is targeting.
	 * </p>
	 * 
	 * @return The server.
	 */
	public abstract VoxelityServer getServer();

	@Override
	public ServerHistoryQueue getHistory() { return history; }

	@Override
	public Set<ServerWorld> getWorlds() { return getServer().getWorlds(); }

	@Override
	public EditRequest createEditRequest(World target) {
		// TODO Auto-generated method stub
		return null;
	}

	public void loadEditorData(EditorPaths paths) throws IOException {
		ServerHistoryQueue history = this.history;

		if (Files.exists(paths.getHistoryPath())) {
			try (InputStream stream = Files.newInputStream(paths.getHistoryPath())) {
				DataInput din = new DataInputStream(stream);
				history = ServerHistoryQueue.deserialize(din, getServer());
			}
		}

		this.history = history;
	}

	public void saveEditorData(EditorPaths paths) throws IOException {
		if (Files.notExists(paths.getHistoryPath().getParent()))
			Files.createDirectories(paths.getHistoryPath().getParent());
		try (OutputStream stream = Files.newOutputStream(paths.getHistoryPath())) {
			DataOutput dout = new DataOutputStream(stream);
			history.serialize(dout, getServer(), true);
		}
	}
}
