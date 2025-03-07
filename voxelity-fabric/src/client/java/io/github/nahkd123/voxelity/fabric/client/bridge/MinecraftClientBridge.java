package io.github.nahkd123.voxelity.fabric.client.bridge;

import io.github.nahkd123.voxelity.fabric.client.editor.FabricClientVoxelityEditor;
import io.github.nahkd123.voxelity.fabric.net.VoxelityFeature;

public interface MinecraftClientBridge {
	/**
	 * <p>
	 * Reset everything to initial state.
	 * </p>
	 */
	void voxelity$reset();

	boolean voxelity$isFeatureEnabled(VoxelityFeature feature);

	void voxelity$setFeature(VoxelityFeature feature, boolean enabled);

	FabricClientVoxelityEditor voxelity$getEditor();

	void voxelity$setEditor(FabricClientVoxelityEditor editor);
}
