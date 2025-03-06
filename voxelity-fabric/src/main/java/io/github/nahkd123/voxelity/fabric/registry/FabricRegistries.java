package io.github.nahkd123.voxelity.fabric.registry;

import io.github.nahkd123.voxelity.registry.Registries;
import io.github.nahkd123.voxelity.registry.RegistryKey;
import io.github.nahkd123.voxelity.voxel.VoxelType;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

public class FabricRegistries implements Registries {
	public static final FabricRegistries REGISTRIES = new FabricRegistries();

	private final RegistryWrapper<Block, VoxelType> voxelTypes;

	private FabricRegistries() {
		voxelTypes = new RegistryWrapper<>(net.minecraft.registry.Registries.BLOCK);
	}

	@Override
	public RegistryKey createKey(String namespace, String id) {
		return (RegistryKey) (Object) Identifier.of(namespace, id);
	}

	@Override
	public RegistryWrapper<Block, VoxelType> getVoxelTypes() { return voxelTypes; }
}
