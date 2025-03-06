package io.github.nahkd123.voxelity.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;

import io.github.nahkd123.voxelity.voxel.Voxel;
import io.github.nahkd123.voxelity.world.VoxelView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Mixin(BlockView.class)
public interface BlockViewMixin extends VoxelView {
	@Override
	default Voxel get(int x, int y, int z) {
		return (Voxel) ((BlockView) this).getBlockState(new BlockPos(x, y, z));
	}
}
