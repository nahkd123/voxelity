package io.github.nahkd123.voxelity.server.editor.history;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.nahkd123.voxelity.editor.history.HistoryQueue;
import io.github.nahkd123.voxelity.server.VoxelityServer;
import io.github.nahkd123.voxelity.task.ImmediateTask;
import io.github.nahkd123.voxelity.task.Task;

public class ServerHistoryQueue implements HistoryQueue {
	private List<ServerAction> actions = new ArrayList<>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ServerHistoryQueue deserialize(DataInput stream, VoxelityServer server) throws IOException {
		ServerHistoryQueue queue = new ServerHistoryQueue();
		int count = stream.readInt();
		ServerAction before = null;

		for (int i = 0; i < count; i++) {
			String id = stream.readUTF();
			String label = stream.readUTF();
			ServerActionSerializer serializer = ServerActionSerializer.ID_TO_SERIALIZER.get(id);
			if (serializer == null) throw new IOException("Unknown action ID %s".formatted(id));
			ServerAction next = serializer.deserialize(queue, before, label, stream, server);
			before = next;
			queue.actions.add(next);
		}

		return queue;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void serialize(DataOutput stream, VoxelityServer server, boolean emptyIfUnsupported) throws IOException {
		for (ServerAction action : actions) {
			String id = ServerActionSerializer.CLASS_TO_ID.get(action.getClass());

			if (id == null) {
				if (emptyIfUnsupported) {
					stream.writeInt(0);
					return;
				}

				throw new IOException("Serializer for %s is unregistered %s".formatted(action.getClass()));
			}
		}

		stream.writeInt(actions.size());
		for (ServerAction action : actions) {
			String id = ServerActionSerializer.CLASS_TO_ID.get(action.getClass());
			ServerActionSerializer serializer = ServerActionSerializer.ID_TO_SERIALIZER.get(id);
			stream.writeUTF(id);
			stream.writeUTF(action.getLabel());
			serializer.serialize(action, stream, server);
		}
	}

	public <T extends ServerAction> T enqueue(ServerActionEnqueueFactory<T> factory) {
		int beforeIndex = getLastIndex(action -> !action.isReverted());
		ServerAction before;

		if (beforeIndex != -1) {
			int removeCount = actions.size() - beforeIndex - 1;
			while (removeCount > 0) actions.removeLast();
			before = actions.get(beforeIndex);
		} else {
			before = null;
		}

		T now = factory.createAction(this, before);
		before.setAfter(now);
		actions.add(now);
		return now;
	}

	@Override
	public int count() {
		return actions.size();
	}

	@Override
	public ServerAction get(int index) {
		return actions.get(index);
	}

	@Override
	public Task clear() {
		for (ServerAction action : actions) {
			action.setQueue(null);
			action.setBefore(null);
			action.setAfter(null);
		}

		actions = new ArrayList<>();
		return ImmediateTask.FINISHED;
	}
}
