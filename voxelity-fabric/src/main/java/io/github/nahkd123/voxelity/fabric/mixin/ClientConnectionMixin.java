package io.github.nahkd123.voxelity.fabric.mixin;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import io.github.nahkd123.voxelity.fabric.bridge.ClientConnectionBridge;
import io.github.nahkd123.voxelity.fabric.net.c2s.ServerVoxelityPayloadListener;
import io.github.nahkd123.voxelity.fabric.permission.Capability;
import net.minecraft.network.ClientConnection;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin implements ClientConnectionBridge {
	@Unique
	private ServerVoxelityPayloadListener voxelity$serverListener;

	@Unique
	private Set<Capability> voxelity$capabilities;

	@Override
	public Set<Capability> voxelity$getCapabilities() {
		if (voxelity$capabilities == null) voxelity$capabilities = new HashSet<>();
		return voxelity$capabilities;
	}

	@Override
	public void voxelity$setServerListener(ServerVoxelityPayloadListener listener) {
		voxelity$serverListener = listener;
	}

	@Override
	public ServerVoxelityPayloadListener voxelity$getServerListener() {
		return voxelity$serverListener;
	}
}
