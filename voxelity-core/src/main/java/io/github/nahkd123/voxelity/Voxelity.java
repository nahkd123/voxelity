package io.github.nahkd123.voxelity;

import io.github.nahkd123.voxelity.registry.Registries;
import io.github.nahkd123.voxelity.voxel.Voxel;
import io.github.nahkd123.voxelity.voxel.Voxels;

/**
 * <p>
 * Represent Voxelity platform, which is usually accessible as static getter
 * method.
 * </p>
 */
public interface Voxelity {
	/**
	 * <p>
	 * Get the registries manager, which contains access to registries and method
	 * for creating keys.
	 * </p>
	 * 
	 * @return The registries manager.
	 */
	Registries getRegistries();

	/**
	 * <p>
	 * Get the voxels manager, which contains methods for dealing with
	 * {@link Voxel}.
	 * </p>
	 * 
	 * @return The voxels manager.
	 */
	Voxels getVoxels();
}
