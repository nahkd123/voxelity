package io.github.nahkd123.voxelity.fabric.id;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import io.github.nahkd123.voxelity.fabric.bridge.ServerCommonNetworkHandlerBridge;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerCommonNetworkHandler;

/**
 * <p>
 * The identity object for identifying user. This is usually a wrapper to UUID.
 * </p>
 */
public interface UserIdentity {
	/**
	 * <p>
	 * A string representation of this user identity. This string representation is
	 * unique to each user, matching {@code [A-Za-z0-9-_]} regular expression and
	 * was meant to be used as key in database (file name, relational database key,
	 * etc).
	 * </p>
	 * 
	 * @return The string database key.
	 */
	String getDatabaseKey();

	static ServerIdentity server() {
		return ServerIdentity.SERVER;
	}

	static PlayerIdentity player(UUID uuid) {
		return new PlayerIdentity(uuid);
	}

	static PlayerIdentity player(PlayerEntity player) {
		return new PlayerIdentity(player.getUuid());
	}

	static PlayerIdentity player(GameProfile profile) {
		return new PlayerIdentity(profile.getId());
	}

	static PlayerIdentity player(ServerCommonNetworkHandler handler) {
		return player(((ServerCommonNetworkHandlerBridge) handler).voxelity$getGameProfile());
	}

	static PlayerIdentity player(ClientConnection connection) {
		return player((ServerCommonNetworkHandler) connection.getPacketListener());
	}
}
