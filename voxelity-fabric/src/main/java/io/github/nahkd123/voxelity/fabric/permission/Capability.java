package io.github.nahkd123.voxelity.fabric.permission;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import io.github.nahkd123.voxelity.fabric.bridge.ClientConnectionBridge;
import io.github.nahkd123.voxelity.fabric.net.VoxelityPayloads;
import io.github.nahkd123.voxelity.fabric.net.s2c.CapabilitiesS2CPayload;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;

/**
 * <p>
 * Capability indicates what connection can request to server, like requesting
 * to enable editor mode for example. Some capabilities, when absent, may remove
 * functionality on client, while others do not because of availability of
 * alternative solution. For example, removing {@link #EDITOR} will prevent user
 * from using Editor mode, while removing {@link #SERVER_SCRIPT} still let user
 * use edit script, but the results will be sent to server in a form of
 * schematic.
 * </p>
 * 
 * @see #setCapability(ClientConnection, boolean)
 * @see #hasCapability(ClientConnection)
 * @see #addCapabilities(ClientConnection, Collection)
 * @see #removeCapabilities(ClientConnection, Collection)
 */
public enum Capability {
	/**
	 * <p>
	 * Allows user to make edits in Editor mode.
	 * </p>
	 */
	EDITOR,
	/**
	 * <p>
	 * Allows user to execute any script on server. If user don't have this
	 * capability, the script will execute locally on user's client. The calculated
	 * edits will then submit to server inside edit queue.
	 * </p>
	 * 
	 * @deprecated Not implemented.
	 */
	@Deprecated
	SERVER_SCRIPT;

	public static final PacketCodec<ByteBuf, Capability> PACKET_CODEC = VoxelityPayloads.createEnumCodec(List.of(
		EDITOR));

	/**
	 * <p>
	 * Check whether the connection have the capability. The capabilities set are
	 * controlled by server.
	 * </p>
	 * 
	 * @param connection The connection on either server or client.
	 * @return Whether connection have capability.
	 */
	public boolean hasCapability(ClientConnection connection) {
		return ((ClientConnectionBridge) connection).voxelity$getCapabilities().contains(this);
	}

	/**
	 * <p>
	 * Modify the capability state of the connection. This will also call
	 * {@link #sync(ClientConnection, Set)}. Note that this method can only be
	 * called on the server.
	 * </p>
	 * 
	 * @param connection The connection on server.
	 * @param state      The capability state.
	 * @throws IllegalArgumentException if connection is not serverbound.
	 */
	public void setCapability(ClientConnection connection, boolean state) throws IllegalArgumentException {
		if (connection.getSide() != NetworkSide.SERVERBOUND)
			throw new IllegalArgumentException("Can only set capability state on server");
		Set<Capability> capabilities = ((ClientConnectionBridge) connection).voxelity$getCapabilities();
		boolean prevState = capabilities.contains(this);

		if (prevState ^ state) {
			if (state) capabilities.add(this);
			else capabilities.remove(this);
			sync(connection);
		}
	}

	/**
	 * <p>
	 * Sync capabilities to client. This will send {@link CapabilitiesS2CPayload} to
	 * client.
	 * </p>
	 * 
	 * @param connection The connection on server.
	 * @throws IllegalArgumentException if connection is not serverbound.
	 */
	public static void sync(ClientConnection connection) throws IllegalArgumentException {
		if (connection.getSide() != NetworkSide.SERVERBOUND)
			throw new IllegalArgumentException("Can only sync capability states on server");
		Set<Capability> capabilities = ((ClientConnectionBridge) connection).voxelity$getCapabilities();
		connection.send(new CustomPayloadS2CPacket(new CapabilitiesS2CPayload(Set.copyOf(capabilities))));
	}

	/**
	 * <p>
	 * Get the capability set of connection. The returned set is unmodifiable. To
	 * control the capabilities, use
	 * {@link #setCapability(ClientConnection, boolean)} on server.
	 * </p>
	 * 
	 * @param connection The connection on either server or client.
	 * @return An unmodifiable {@link Set} view of connection's current
	 *         capabilities.
	 */
	public static Set<Capability> getCapabilities(ClientConnection connection) {
		return Collections.unmodifiableSet(((ClientConnectionBridge) connection).voxelity$getCapabilities());
	}

	public static void addCapabilities(ClientConnection connection, Collection<Capability> capabilities) throws IllegalArgumentException {
		if (connection.getSide() != NetworkSide.SERVERBOUND)
			throw new IllegalArgumentException("Can only modify capabilities on server");
		((ClientConnectionBridge) connection).voxelity$getCapabilities().addAll(capabilities);
		sync(connection);
	}

	public static void removeCapabilities(ClientConnection connection, Collection<Capability> capabilities) throws IllegalArgumentException {
		if (connection.getSide() != NetworkSide.SERVERBOUND)
			throw new IllegalArgumentException("Can only modify capabilities states on server");
		((ClientConnectionBridge) connection).voxelity$getCapabilities().removeAll(capabilities);
		sync(connection);
	}
}
