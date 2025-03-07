package io.github.nahkd123.voxelity.fabric.net;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.bridge.ServerCommonNetworkHandlerBridge;
import io.github.nahkd123.voxelity.fabric.hook.permission.PermissionManager;
import io.github.nahkd123.voxelity.fabric.net.c2s.VoxelityC2SFeatureGetPayload;
import io.github.nahkd123.voxelity.fabric.net.c2s.VoxelityC2SFeatureSetPayload;
import io.github.nahkd123.voxelity.fabric.net.s2c.VoxelityS2CFeaturePayload;
import io.github.nahkd123.voxelity.fabric.server.FabricVoxelityServer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.mixin.networking.accessor.ServerCommonNetworkHandlerAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class VoxelityC2SPayloadHandlers {
	public static void initialize() {
		ServerConfigurationNetworking.registerGlobalReceiver(VoxelityC2SFeatureGetPayload.ID, (payload, context) -> {
			ClientConnection conn = ((ServerCommonNetworkHandlerAccessor) context.networkHandler()).getConnection();
			handleFeatureGet(payload.feature(), conn, context.responseSender());
		});
		ServerPlayNetworking.registerGlobalReceiver(VoxelityC2SFeatureGetPayload.ID, (payload, context) -> {
			ServerPlayNetworkHandler networkHandler = context.player().networkHandler;
			ClientConnection conn = ((ServerCommonNetworkHandlerAccessor) networkHandler).getConnection();
			handleFeatureGet(payload.feature(), conn, context.responseSender());
		});

		ServerConfigurationNetworking.registerGlobalReceiver(VoxelityC2SFeatureSetPayload.ID, (payload, context) -> {
			ClientConnection conn = ((ServerCommonNetworkHandlerAccessor) context.networkHandler()).getConnection();
			handleFeatureSet(payload.feature(), payload.enable(), conn, context.responseSender());
		});
		ServerPlayNetworking.registerGlobalReceiver(VoxelityC2SFeatureSetPayload.ID, (payload, context) -> {
			ServerPlayNetworkHandler networkHandler = context.player().networkHandler;
			ClientConnection conn = ((ServerCommonNetworkHandlerAccessor) networkHandler).getConnection();
			handleFeatureSet(payload.feature(), payload.enable(), conn, context.responseSender());
		});
	}

	public static void handleFeatureGet(VoxelityFeature feature, ClientConnection connection, PacketSender sender) {
		sender.sendPacket(new VoxelityS2CFeaturePayload(feature, feature.isEnabled(connection)));
	}

	public static void handleFeatureSet(VoxelityFeature feature, boolean state, ClientConnection connection, PacketSender sender) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			String user = connection.getPacketListener() instanceof ServerCommonNetworkHandler handler
				? handler.getDebugProfile().getName()
				: connection.getAddressAsString(true);
			VoxelityFabric.LOGGER.info("{} requested feature {} = {}", user, feature, state);
		}

		if (!(connection.getPacketListener() instanceof ServerCommonNetworkHandlerBridge bridge))
			throw new RuntimeException("Network handler is not server-side");
		FabricVoxelityServer voxelity = bridge.voxelity$getVoxelityServer();
		PermissionManager permissions = voxelity.getPermissionManager();

		if (feature.hasPermission(connection, permissions)) feature.setEnable(connection, state);
		var payload = new VoxelityS2CFeaturePayload(feature, feature.isEnabled(connection));
		connection.send(new CustomPayloadS2CPacket(payload));
	}
}
