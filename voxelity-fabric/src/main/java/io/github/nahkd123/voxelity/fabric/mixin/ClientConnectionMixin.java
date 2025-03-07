package io.github.nahkd123.voxelity.fabric.mixin;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import io.github.nahkd123.voxelity.fabric.bridge.ClientConnectionBridge;
import io.github.nahkd123.voxelity.fabric.net.VoxelityFeature;
import io.github.nahkd123.voxelity.fabric.server.editor.FabricServerVoxelityEditor;
import net.minecraft.network.ClientConnection;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin implements ClientConnectionBridge {
	@Unique
	private FabricServerVoxelityEditor voxelity$serverEditor;

	@Unique
	private Set<VoxelityFeature> voxelity$features;

	@Override
	public FabricServerVoxelityEditor voxelity$getServerEditor() {
		return voxelity$serverEditor;
	}

	@Override
	public void voxelity$setServerEditor(FabricServerVoxelityEditor editor) {
		voxelity$serverEditor = editor;
	}

	@Override
	public boolean voxelity$isFeatureEnabled(VoxelityFeature feature) {
		return voxelity$features != null && voxelity$features.contains(feature);
	}

	@Override
	public void voxelity$setFeature(VoxelityFeature feature, boolean state) {
		if (voxelity$features == null) voxelity$features = new HashSet<>();
		if (state) voxelity$features.add(feature);
		else voxelity$features.remove(feature);
	}
}
