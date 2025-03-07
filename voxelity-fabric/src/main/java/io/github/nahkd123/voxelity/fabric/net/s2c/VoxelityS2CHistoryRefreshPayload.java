package io.github.nahkd123.voxelity.fabric.net.s2c;

import java.util.List;

import io.github.nahkd123.voxelity.editor.history.Action;
import io.github.nahkd123.voxelity.editor.history.HistoryQueue;
import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record VoxelityS2CHistoryRefreshPayload(List<Entry> entries) implements CustomPayload {
	public static record Entry(String label, boolean reverted) {
		public static Entry fromAction(Action action) {
			return new Entry(action.getLabel(), action.isReverted());
		}

		public static final PacketCodec<PacketByteBuf, Entry> PACKET_CODEC = PacketCodec
			.tuple(
				PacketCodecs.STRING, Entry::label,
				PacketCodecs.BOOLEAN, Entry::reverted,
				Entry::new)
			.cast();
	}

	public static VoxelityS2CHistoryRefreshPayload fromHistory(HistoryQueue history) {
		Entry[] entries = new Entry[history.count()];
		for (int i = 0; i < entries.length; i++) entries[i] = Entry.fromAction(history.get(i));
		return new VoxelityS2CHistoryRefreshPayload(List.of(entries));
	}

	public static final Id<VoxelityS2CHistoryRefreshPayload> ID = new Id<>(VoxelityFabric.id("history_refresh"));
	public static final PacketCodec<PacketByteBuf, VoxelityS2CHistoryRefreshPayload> PACKET_CODEC = PacketCodec
		.tuple(
			Entry.PACKET_CODEC.collect(PacketCodecs.toList()), VoxelityS2CHistoryRefreshPayload::entries,
			VoxelityS2CHistoryRefreshPayload::new)
		.cast();

	@Override
	public Id<VoxelityS2CHistoryRefreshPayload> getId() { return ID; }
}
