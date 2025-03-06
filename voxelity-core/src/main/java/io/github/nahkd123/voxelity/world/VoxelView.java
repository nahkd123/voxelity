package io.github.nahkd123.voxelity.world;

import io.github.nahkd123.voxelity.voxel.Voxel;

public interface VoxelView {
	/**
	 * <p>
	 * Get voxel at given coordinates. This method returns {@code null} if the voxel
	 * at specified coordinates is a void. Voids are usually used to indicate the
	 * voxel is "transparent".
	 * </p>
	 * 
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @return The voxel or {@code null}.
	 */
	Voxel get(int x, int y, int z);
}
