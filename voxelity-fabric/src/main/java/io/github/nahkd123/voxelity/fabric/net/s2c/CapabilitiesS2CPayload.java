package io.github.nahkd123.voxelity.fabric.net.s2c;

import java.util.HashSet;
import java.util.Set;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.net.VoxelityPayload;
import io.github.nahkd123.voxelity.fabric.permission.Capability;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

/**
 * <p>
 * This payload describe what user can do. Currently limited to editor mode for
 * now.
 * </p>
 * <p>
 * When user's permission is changed, the server must send this payload,
 * informing user that their capabilities has been modified. If user is using
 * removed capability, the function associated with that capability will be
 * discarded (for example, removing {@link Capability#EDITOR} will also close
 * Editor mode on client).
 * </p>
 */
public record CapabilitiesS2CPayload(Set<Capability> capabilities) implements VoxelityPayload<ClientVoxelityPayloadListener> {
	public static final Id<CapabilitiesS2CPayload> ID = new Id<>(VoxelityFabric.id("capabilities"));
	public static final PacketCodec<PacketByteBuf, CapabilitiesS2CPayload> CODEC = Capability.PACKET_CODEC
		.collect(PacketCodecs.toCollection(size -> (Set<Capability>) new HashSet<Capability>(size)))
		.xmap(CapabilitiesS2CPayload::new, CapabilitiesS2CPayload::capabilities)
		.cast();

	@Override
	public Id<CapabilitiesS2CPayload> getId() { return ID; }

	@Override
	public void apply(ClientVoxelityPayloadListener listener) {
		listener.onCapabilities(this);
	}
}
