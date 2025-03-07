package io.github.nahkd123.voxelity.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;

import io.github.nahkd123.voxelity.voxel.Voxel;
import io.github.nahkd123.voxelity.voxel.view.MutableVoxelView;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableWorld;

@Mixin(ModifiableWorld.class)
public interface ModifiableWorldMixin extends MutableVoxelView {
	@Override
	default void set(int x, int y, int z, Voxel voxel) {
		((ModifiableWorld) (Object) this).setBlockState(
			new BlockPos(x, y, z),
			(BlockState) (Object) voxel,
			Block.NOTIFY_ALL);
	}
}
