package io.github.nahkd123.voxelity.client;

import io.github.nahkd123.voxelity.editor.VoxelityEditor;
import io.github.nahkd123.voxelity.editor.view.EditorView;

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
}
