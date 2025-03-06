package io.github.nahkd123.voxelity.registry;

import java.io.DataOutput;
import java.io.IOException;

public interface RegistryKey {
	String namespace();

	String id();

	default void serialize(DataOutput stream) throws IOException {
		stream.writeUTF(namespace());
		stream.writeUTF(id());
	}
}
