package io.github.nahkd123.voxelity.fabric.net.s2c;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.server.editor.FabricServerVoxelityEditor;
import io.github.nahkd123.voxelity.fabric.world.WorldReference;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record VoxelityS2CEditorStatePayload(Set<WorldReference> worlds, WorldReference currentWorld) implements CustomPayload {
	public static final Id<VoxelityS2CEditorStatePayload> ID = new Id<>(VoxelityFabric.id("editor_state"));
	public static final PacketCodec<PacketByteBuf, VoxelityS2CEditorStatePayload> PACKET_CODEC = PacketCodec
		.tuple(
			WorldReference.PACKET_CODEC.collect(PacketCodecs.toCollection(HashSet::new)),
			VoxelityS2CEditorStatePayload::worlds,
			WorldReference.PACKET_CODEC, VoxelityS2CEditorStatePayload::currentWorld,
			VoxelityS2CEditorStatePayload::new)
		.cast();

	public static VoxelityS2CEditorStatePayload fromEditor(FabricServerVoxelityEditor editor) {
		Set<WorldReference> worlds = editor.getWorlds().stream()
			.map(w -> new WorldReference(w.getName()))
			.collect(Collectors.toSet());
		WorldReference currentWorld = new WorldReference(editor.getCurrentWorld().getName());
		return new VoxelityS2CEditorStatePayload(worlds, currentWorld);
	}

	@Override
	public Id<VoxelityS2CEditorStatePayload> getId() { return ID; }
}
