package io.github.nahkd123.voxelity.fabric.bridge;

import java.util.Set;

import io.github.nahkd123.voxelity.fabric.net.c2s.ServerVoxelityPayloadListener;
import io.github.nahkd123.voxelity.fabric.net.s2c.ClientVoxelityPayloadListener;
import io.github.nahkd123.voxelity.fabric.permission.Capability;

public interface ClientConnectionBridge {
	void voxelity$setServerListener(ServerVoxelityPayloadListener listener);

	ServerVoxelityPayloadListener voxelity$getServerListener();

	void voxelity$setClientListener(ClientVoxelityPayloadListener listener);

	ClientVoxelityPayloadListener voxelity$getClientListener();

	Set<Capability> voxelity$getCapabilities();
}
