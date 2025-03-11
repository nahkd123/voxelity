package io.github.nahkd123.voxelity.fabric.net.c2s;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.net.VoxelityPayload;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record NotifyStatusC2SPayload(ClientStatus status) implements VoxelityPayload<ServerVoxelityPayloadListener> {
	public static final Id<NotifyStatusC2SPayload> ID = new Id<>(VoxelityFabric.id("notify_status"));
	public static final PacketCodec<PacketByteBuf, NotifyStatusC2SPayload> CODEC = ClientStatus.PACKET_CODEC
		.xmap(NotifyStatusC2SPayload::new, NotifyStatusC2SPayload::status)
		.cast();

	@Override
	public Id<NotifyStatusC2SPayload> getId() { return ID; }

	@Override
	public void apply(ServerVoxelityPayloadListener listener) {
		listener.onNotifyStatus(this);
	}
}
