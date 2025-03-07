package io.github.nahkd123.voxelity.server.editor;

import io.github.nahkd123.voxelity.math.Box3i;
import io.github.nahkd123.voxelity.server.world.ServerWorld;
import io.github.nahkd123.voxelity.voxel.Volume;
import io.github.nahkd123.voxelity.voxel.view.MutableVoxelView;

public interface EditRequestHandler {
	ServerWorld getTarget();

	/**
	 * <p>
	 * Handle region update, such as applying permission/region manager or modify
	 * the voxels server-side.
	 * </p>
	 * <p>
	 * User may not confirm the edit, so you may not want to copy voxels from edit
	 * layer to {@link #getTarget()} in this method.
	 * </p>
	 * 
	 * @param updatedRegion The region that has been updated by user.
	 * @param updateMask    The mask that indicates which voxels has been updated
	 *                      since last call to this method.
	 * @param editLayer     The edit layer. Assigning any voxel will send
	 *                      server-side update back to user. Edit layer persists its
	 *                      data across subsequent calls to this method.
	 */
	default void handleRegionUpdate(Box3i updatedRegion, Volume updateMask, MutableVoxelView editLayer) {}

	/**
	 * <p>
	 * Handle edit submit. At this stage, user has confirmed the changes, but you
	 * may not copy the voxels from edit layer to {@link #getTarget()}, as that part
	 * will be handled by history manager (commit the changes to history first, then
	 * apply changes).
	 * </p>
	 * 
	 * @param editRegion The region that has contains the edits made by user.
	 * @param editLayer  The edit layer. Assigning any voxel will affect the result.
	 */
	default void handleSubmit(Box3i editRegion, MutableVoxelView editLayer) {}

	/**
	 * <p>
	 * Handle request destruction. Request on server will be destroyed when user
	 * requested the edit request to be destroyed, or the request is timed out.
	 * Override this method to free resources, like handle to permission manager for
	 * example.
	 * </p>
	 */
	default void handleDestroy() {}
}
