package io.github.nahkd123.voxelity.voxel;

import java.util.Set;

import io.github.nahkd123.voxelity.registry.RegistryEntry;

public interface VoxelType extends RegistryEntry<VoxelType> {
	Set<VoxelProperty<?>> getProperties();

	Voxel createDefault();

	default <T extends Comparable<T>> T getDefault(VoxelProperty<T> property) {
		return createDefault().get(property);
	}

	default VoxelProperty<?> getPropertyFromName(String name) {
		for (VoxelProperty<?> property : getProperties()) if (property.getTypeName().equals(name)) return property;
		return null;
	}
}
