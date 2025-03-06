package io.github.nahkd123.voxelity.editor.view;

import io.github.nahkd123.voxelity.math.Vec3d;
import io.github.nahkd123.voxelity.world.VoxelView;
import io.github.nahkd123.voxelity.world.World;

/**
 * <p>
 * Represent a view of editor. The view must be recreated when user changed the
 * dimension.
 * </p>
 * <p>
 * Due to restriction of Minecraft, users are not allowed to view the dimension
 * that they are not in currently (I mean we can since we are modifying the
 * server, but writing code for that is messy).
 * </p>
 */
public interface EditorView {
	/**
	 * <p>
	 * Get the reference of the world this editor view is in.
	 * </p>
	 * 
	 * @return The world reference.
	 */
	World getWorld();

	/**
	 * <p>
	 * Get the read-only view of the world that this view is in.
	 * </p>
	 * 
	 * @return The read-only view of the world.
	 */
	VoxelView getVoxelView();

	/**
	 * <p>
	 * Get the camera (or eye) position in world coordinates.
	 * </p>
	 * 
	 * @return The camera position.
	 */
	Vec3d getCameraPosition();
}
