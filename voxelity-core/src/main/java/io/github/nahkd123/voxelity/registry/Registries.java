package io.github.nahkd123.voxelity.registry;

import java.io.DataInput;
import java.io.IOException;

import io.github.nahkd123.voxelity.voxel.VoxelType;

public interface Registries {
	RegistryKey createKey(String namespace, String id);

	default RegistryKey deserializeKey(DataInput stream) throws IOException {
		String namespace = stream.readUTF();
		String id = stream.readUTF();
		return createKey(namespace, id);
	}

	ReadonlyRegistry<VoxelType> getVoxelTypes();
}
