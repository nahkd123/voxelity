package io.github.nahkd123.voxelity.server.editor.history;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.github.nahkd123.voxelity.server.VoxelityServer;

public interface ServerActionSerializer<T extends ServerAction> {
	void serialize(T action, DataOutput stream, VoxelityServer server) throws IOException;

	T deserialize(ServerHistoryQueue queue, T before, String label, DataInput stream, VoxelityServer server) throws IOException;

	@SuppressWarnings("rawtypes")
	static Map<String, ServerActionSerializer> ID_TO_SERIALIZER = new HashMap<>();
	static Map<Class<?>, String> CLASS_TO_ID = new HashMap<>();

	static <T extends ServerAction> void registerSerializer(String id, Class<T> type, ServerActionSerializer<T> serializer) {
		ID_TO_SERIALIZER.put(id, serializer);
		CLASS_TO_ID.put(type, id);
	}
}
