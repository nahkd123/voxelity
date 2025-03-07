package io.github.nahkd123.voxelity.fabric.client.editor;

import java.util.Set;

import io.github.nahkd123.voxelity.client.editor.ClientVoxelityEditor;
import io.github.nahkd123.voxelity.client.editor.EditRequest;
import io.github.nahkd123.voxelity.editor.history.HistoryQueue;
import io.github.nahkd123.voxelity.fabric.client.editor.view.ClientEditorView;
import io.github.nahkd123.voxelity.fabric.net.s2c.VoxelityS2CEditorStatePayload;
import io.github.nahkd123.voxelity.fabric.world.WorldReference;
import io.github.nahkd123.voxelity.world.World;
import net.minecraft.client.MinecraftClient;

public class FabricClientVoxelityEditor implements ClientVoxelityEditor {
	private MinecraftClient client;
	private Set<WorldReference> worlds = Set.of();
	private ClientEditorView view = null;

	public FabricClientVoxelityEditor(MinecraftClient client) {
		this.client = client;
	}

	public void updateState(VoxelityS2CEditorStatePayload state) {
		worlds = state.worlds();
		view = new ClientEditorView(client, state.currentWorld());
	}

	@Override
	public HistoryQueue getHistory() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<? extends World> getWorlds() { return worlds; }

	@Override
	public ClientEditorView getView() { return view; }

	@Override
	public EditRequest createEditRequest(World target) {
		if (!(target instanceof WorldReference reference))
			throw new IllegalArgumentException("Invalid target argument");
		return new EditRequestImpl(this, reference);
	}
}
