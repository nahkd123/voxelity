package io.github.nahkd123.voxelity.world;

import io.github.nahkd123.voxelity.voxel.Voxel;

public interface MutableVoxelView extends VoxelView {
	void set(int x, int y, int z, Voxel voxel);
}
