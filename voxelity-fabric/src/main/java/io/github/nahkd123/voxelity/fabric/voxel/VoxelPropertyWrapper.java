package io.github.nahkd123.voxelity.fabric.voxel;

import io.github.nahkd123.voxelity.voxel.VoxelProperty;

public class VoxelPropertyWrapper<T extends Comparable<T>> implements VoxelProperty<T> {
	private net.minecraft.state.property.Property<T> minecraft;

	public VoxelPropertyWrapper(net.minecraft.state.property.Property<T> minecraft) {
		this.minecraft = minecraft;
	}

	@Override
	public String getTypeName() { return minecraft.getName(); }

	@Override
	public int getValuesCount() { return minecraft.getValues().size(); }

	@Override
	public T get(int index) {
		return minecraft.getValues().get(index);
	}

	@Override
	public int indexOf(T value) {
		return minecraft.ordinal(value);
	}

	public net.minecraft.state.property.Property<T> getMinecraft() { return minecraft; }
}
