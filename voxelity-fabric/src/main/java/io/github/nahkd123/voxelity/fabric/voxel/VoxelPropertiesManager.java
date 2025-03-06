package io.github.nahkd123.voxelity.fabric.voxel;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.state.property.Property;

public class VoxelPropertiesManager {
	public static final VoxelPropertiesManager MANAGER = new VoxelPropertiesManager();
	private Map<Property<?>, VoxelPropertyWrapper<?>> map = new HashMap<>();

	private VoxelPropertiesManager() {}

	@SuppressWarnings("unchecked")
	public <T extends Comparable<T>> VoxelPropertyWrapper<T> wrap(Property<T> property) {
		return (VoxelPropertyWrapper<T>) map.computeIfAbsent(property, VoxelPropertyWrapper::new);
	}
}
