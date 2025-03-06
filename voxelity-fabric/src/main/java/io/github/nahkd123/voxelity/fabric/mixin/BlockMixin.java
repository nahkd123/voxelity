package io.github.nahkd123.voxelity.fabric.mixin;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import io.github.nahkd123.voxelity.fabric.registry.FabricRegistries;
import io.github.nahkd123.voxelity.fabric.voxel.VoxelPropertiesManager;
import io.github.nahkd123.voxelity.registry.ReadonlyRegistry;
import io.github.nahkd123.voxelity.voxel.Voxel;
import io.github.nahkd123.voxelity.voxel.VoxelProperty;
import io.github.nahkd123.voxelity.voxel.VoxelType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;

@Mixin(Block.class)
public abstract class BlockMixin implements VoxelType {
	@Unique
	private Set<VoxelProperty<?>> voxelity$properties = null;

	@Override
	public Voxel createDefault() {
		return (Voxel) ((Block) (Object) this).getDefaultState();
	}

	@Override
	public ReadonlyRegistry<VoxelType> getRegistry() { return FabricRegistries.REGISTRIES.getVoxelTypes(); }

	@Override
	public Set<VoxelProperty<?>> getProperties() {
		if (voxelity$properties != null) return voxelity$properties;

		voxelity$properties = new HashSet<>();
		Block block = (Block) (Object) this;
		BlockState defaultStates = block.getDefaultState();

		for (Property<?> vanillaProp : defaultStates.getProperties()) {
			VoxelProperty<?> prop = VoxelPropertiesManager.MANAGER.wrap(vanillaProp);
			voxelity$properties.add(prop);
		}

		return voxelity$properties;
	}
}
