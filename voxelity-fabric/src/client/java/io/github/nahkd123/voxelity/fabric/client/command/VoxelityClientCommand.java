package io.github.nahkd123.voxelity.fabric.client.command;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import io.github.nahkd123.voxelity.fabric.net.VoxelityFeature;
import io.github.nahkd123.voxelity.fabric.net.c2s.VoxelityC2SFeatureSetPayload;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;

public class VoxelityClientCommand {
	public static final LiteralArgumentBuilder<FabricClientCommandSource> SET_FEATURE = literal("setfeature")
		.then(argument("feature", StringArgumentType.string())
			.suggests((context, builder) -> {
				for (VoxelityFeature feature : VoxelityFeature.values()) builder.suggest(feature.name());
				return builder.buildFuture();
			})
			.then(argument("state", BoolArgumentType.bool()).executes(ctx -> {
				VoxelityFeature feature = VoxelityFeature.valueOf(StringArgumentType.getString(ctx, "feature"));
				boolean state = BoolArgumentType.getBool(ctx, "state");
				ClientPlayNetworkHandler networkHandler = ctx.getSource().getPlayer().networkHandler;
				networkHandler.sendPacket(new CustomPayloadC2SPacket(new VoxelityC2SFeatureSetPayload(feature, state)));
				return 1;
			})));

	public static final LiteralArgumentBuilder<FabricClientCommandSource> ROOT = literal("voxelityc")
		.then(SET_FEATURE);
}
