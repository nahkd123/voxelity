package io.github.nahkd123.voxelity.server.editor;

import java.nio.file.Path;

/**
 * <p>
 * A collection of file paths for
 * {@link ServerVoxelityEditor#loadEditorData(EditorPaths)} and
 * {@link ServerVoxelityEditor#saveEditorData(EditorPaths)} to load or save
 * editor data. The paths can be modified to redirect to different location.
 * </p>
 */
@FunctionalInterface
public interface EditorPaths {
	Path getRoot();

	default Path getHistoryPath() { return getRoot().resolve("history-file"); }

	static EditorPaths ofRoot(Path root) {
		return () -> root;
	}
}
