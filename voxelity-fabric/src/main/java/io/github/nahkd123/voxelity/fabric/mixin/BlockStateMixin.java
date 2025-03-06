package io.github.nahkd123.voxelity.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;

import io.github.nahkd123.voxelity.fabric.voxel.VoxelPropertyWrapper;
import io.github.nahkd123.voxelity.voxel.Voxel;
import io.github.nahkd123.voxelity.voxel.VoxelProperty;
import io.github.nahkd123.voxelity.voxel.VoxelType;
import net.minecraft.block.BlockState;

@Mixin(BlockState.class)
public abstract class BlockStateMixin implements Voxel {
	@Override
	public VoxelType getType() { return (VoxelType) ((BlockState) (Object) this).getBlock(); }

	@Override
	public <T extends Comparable<T>> T get(VoxelProperty<T> type) throws IllegalArgumentException {
		if (!(type instanceof VoxelPropertyWrapper<T> wrapper))
			throw new IllegalArgumentException("The property is not a vanilla wrapper");
		return ((BlockState) (Object) this).get(wrapper.getMinecraft());
	}

	@Override
	public <T extends Comparable<T>> Voxel with(VoxelProperty<T> type, T value) throws IllegalArgumentException {
		if (!(type instanceof VoxelPropertyWrapper<T> wrapper))
			throw new IllegalArgumentException("The property is not a vanilla wrapper");
		return (Voxel) ((BlockState) (Object) this).with(wrapper.getMinecraft(), value);
	}
}
