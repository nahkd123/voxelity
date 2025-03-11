package io.github.nahkd123.voxelity.fabric.net;

import net.minecraft.network.packet.CustomPayload;

public interface VoxelityPayload<L extends VoxelityPayloadListener> extends CustomPayload {
	@Override
	Id<? extends VoxelityPayload<L>> getId();

	/**
	 * <p>
	 * Pass payload to listener.
	 * </p>
	 * 
	 * @param listener The listener to receive and process the payload.
	 */
	void apply(L listener);
}
