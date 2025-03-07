package io.github.nahkd123.voxelity.client.editor;

import io.github.nahkd123.voxelity.editor.VoxelityEditor;
import io.github.nahkd123.voxelity.editor.view.EditorView;
import io.github.nahkd123.voxelity.world.World;

public interface ClientVoxelityEditor extends VoxelityEditor {
	/**
	 * <p>
	 * Get client view of editor. If the view does not exists (usually because
	 * client is in configuration phase or world is not yet available), this method
	 * will return {@code null}.
	 * </p>
	 * 
	 * @return The view or {@code null}.
	 */
	EditorView getView();

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
