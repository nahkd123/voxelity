package io.github.nahkd123.voxelity.fabric.world;

import io.github.nahkd123.voxelity.world.World;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record WorldReference(String name) implements World {
	public static final PacketCodec<ByteBuf, WorldReference> PACKET_CODEC = PacketCodec
		.tuple(
			PacketCodecs.STRING, WorldReference::name,
			WorldReference::new);

	@Override
	public String getName() { return name; }
}
