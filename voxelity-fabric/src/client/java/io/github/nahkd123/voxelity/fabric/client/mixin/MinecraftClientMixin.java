package io.github.nahkd123.voxelity.fabric.client.mixin;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import io.github.nahkd123.voxelity.fabric.client.bridge.MinecraftClientBridge;
import io.github.nahkd123.voxelity.fabric.client.editor.FabricClientVoxelityEditor;
import io.github.nahkd123.voxelity.fabric.net.VoxelityFeature;
import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements MinecraftClientBridge {
	@Unique
	private Set<VoxelityFeature> voxelity$features = null;

	@Unique
	private FabricClientVoxelityEditor voxelity$editor = null;

	@Override
	public void voxelity$reset() {
		voxelity$features = null;
		voxelity$editor = null;
	}

	@Override
	public boolean voxelity$isFeatureEnabled(VoxelityFeature feature) {
		return voxelity$features != null && voxelity$features.contains(feature);
	}

	@Override
	public void voxelity$setFeature(VoxelityFeature feature, boolean enabled) {
		if (voxelity$features == null) voxelity$features = new HashSet<>();
		if (enabled) voxelity$features.add(feature);
		else voxelity$features.remove(feature);
	}

	@Override
	public FabricClientVoxelityEditor voxelity$getEditor() {
		return voxelity$editor;
	}

	@Override
	public void voxelity$setEditor(FabricClientVoxelityEditor editor) {
		voxelity$editor = editor;
	}
}
