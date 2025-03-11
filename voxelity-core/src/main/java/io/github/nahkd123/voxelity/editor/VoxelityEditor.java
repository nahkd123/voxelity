package io.github.nahkd123.voxelity.editor;

import java.util.Set;

import io.github.nahkd123.voxelity.client.editor.EditRequest;
import io.github.nahkd123.voxelity.editor.history.HistoryQueue;
import io.github.nahkd123.voxelity.world.World;

/**
 * <p>
 * Represent an editor that user can interact to submit edits. An editor
 * instance will be created when requested by user and will be destroyed when
 * user logout of server. Editor instance must be retained even when user
 * changed world.
 * </p>
 */
public interface VoxelityEditor {
	HistoryQueue getHistory();

	/**
	 * <p>
	 * Get reference to all worlds on the server. Use the reference on methods that
	 * involves editing the world, like {@link #createEditQueue(World)} for example.
	 * </p>
	 * 
	 * @return A collection of world references.
	 */
	Set<? extends World> getWorlds();

	/**
	 * <p>
	 * Create new edit request.
	 * </p>
	 * 
	 * @param target The target world to edit.
	 * @return The edit request.
	 */
	EditRequest createEditRequest(World target);
}
