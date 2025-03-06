package io.github.nahkd123.voxelity.voxel;

import java.io.DataInput;
import java.io.IOException;

import io.github.nahkd123.voxelity.registry.Registries;
import io.github.nahkd123.voxelity.registry.RegistryKey;

public interface Voxels {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	default Voxel deserialize(DataInput stream, Registries registries) throws IOException {
		RegistryKey key = registries.deserializeKey(stream);
		VoxelType type = registries.getVoxelTypes().get(key);
		if (type == null) throw new IOException("Unknown type: %s".formatted(key));

		Voxel voxel = type.createDefault();
		int propertiesCount = stream.readInt();

		for (int i = 0; i < propertiesCount; i++) {
			String propertyName = stream.readUTF();
			int propertyIdx = stream.readInt();
			VoxelProperty<?> property = type.getPropertyFromName(propertyName);
			if (property == null) throw new IOException("Unknown property: %s/%s".formatted(key, propertyName));
			Object value = property.get(propertyIdx);
			voxel = voxel.with((VoxelProperty) property, (Comparable) value);
		}

		return voxel;
	}
}
