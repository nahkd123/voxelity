package io.github.nahkd123.voxelity.fabric.bridge;

import io.github.nahkd123.voxelity.fabric.net.VoxelityFeature;
import io.github.nahkd123.voxelity.fabric.server.editor.FabricServerVoxelityEditor;

public interface ClientConnectionBridge {
	boolean voxelity$isFeatureEnabled(VoxelityFeature feature);

	void voxelity$setFeature(VoxelityFeature feature, boolean state);

	FabricServerVoxelityEditor voxelity$getServerEditor();

	void voxelity$setServerEditor(FabricServerVoxelityEditor editor);
}
