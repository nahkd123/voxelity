package io.github.nahkd123.voxelity.fabric.client.editor.view;

import io.github.nahkd123.voxelity.editor.view.EditorCamera;
import io.github.nahkd123.voxelity.editor.view.EditorView;
import io.github.nahkd123.voxelity.fabric.world.WorldReference;
import io.github.nahkd123.voxelity.voxel.view.VoxelView;
import net.minecraft.client.MinecraftClient;

public class ClientEditorView implements EditorView {
	private MinecraftClient client;
	private ClientEditorCamera camera;
	private WorldReference world;

	public ClientEditorView(MinecraftClient client, WorldReference world) {
		this.client = client;
		this.world = world;
		this.camera = new ClientEditorCamera(client);
	}

	@Override
	public WorldReference getWorld() { return world; }

	@Override
	public VoxelView getVoxelView() { return (VoxelView) client.world; }

	@Override
	public EditorCamera getCamera() { return camera; }
}
