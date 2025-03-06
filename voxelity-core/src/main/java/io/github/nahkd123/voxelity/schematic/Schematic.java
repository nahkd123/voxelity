package io.github.nahkd123.voxelity.schematic;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.nahkd123.voxelity.Voxelity;
import io.github.nahkd123.voxelity.math.Box3i;
import io.github.nahkd123.voxelity.math.Vec3i;
import io.github.nahkd123.voxelity.registry.Registries;
import io.github.nahkd123.voxelity.utils.packed.PackedArray;
import io.github.nahkd123.voxelity.voxel.Voxel;
import io.github.nahkd123.voxelity.voxel.Voxels;
import io.github.nahkd123.voxelity.world.MutableVoxelView;
import io.github.nahkd123.voxelity.world.VoxelBuffer;
import io.github.nahkd123.voxelity.world.VoxelView;

/**
 * <p>
 * Schematic is immutable and cannot be modified by assigning {@link Voxel} like
 * your typical {@link MutableVoxelView}. To modify a schematic, you first load
 * the schematic to any {@link MutableVoxelView} (usually {@link VoxelBuffer}),
 * then apply modifications and create new schematic from that
 * {@link MutableVoxelView}).
 * </p>
 * <p>
 * Schematic always have the origin at {@code (0, 0, 0)}. To shift the origin in
 * schematic, simply translate the bounding box with
 * {@link #translateBounds(int, int, int)}.
 * </p>
 * 
 * @see #copyFrom(VoxelView, Box3i, Vec3i)
 * @see #serialize(DataOutput)
 * @see #deserialize(DataInput, Voxels, Registries)
 */
public interface Schematic extends VoxelView {
	/**
	 * <p>
	 * Get the bounds of this schematic. The minimum corner can be anywhere, but the
	 * origin of schematic is always at {@code (0, 0, 0)}.
	 * </p>
	 * 
	 * @return The bounds.
	 */
	default Box3i getBounds() {
		Vec3i boundsMin = getBoundsMin();
		Vec3i size = getSize();
		return new Box3i(boundsMin.x(), boundsMin.y(), boundsMin.z(), size.x(), size.y(), size.z());
	}

	Vec3i getBoundsMin();

	Vec3i getSize();

	List<Voxel> getPalette();

	/**
	 * <p>
	 * Create new schematic with its entire bounding box moved by some amount.
	 * </p>
	 * 
	 * @param dx Translation amount in X axis.
	 * @param dy Translation amount in Y axis.
	 * @param dz Translation amount in Z axis.
	 * @return The new schematic with new bounds.
	 */
	Schematic translateBounds(int dx, int dy, int dz);

	/**
	 * <p>
	 * Compact this schematic into another schematic with packed integer array as
	 * representation for each voxel. When working with large schematic, compacting
	 * helps with memory usage.
	 * </p>
	 * 
	 * @return
	 */
	default PackedArraySchematic compact() {
		List<Voxel> palette = getPalette();
		Map<Voxel, Integer> reversePalette = new HashMap<>();
		for (int i = 0; i < palette.size(); i++) reversePalette.put(palette.get(i), i + 1);
		Vec3i min = getBoundsMin();
		Vec3i max = getBounds().max();
		Vec3i size = getSize();
		PackedArray array = PackedArray.create(palette.size() + 1, size.x() * size.y() * size.z());

		for (int y = min.x(); y <= max.x(); y++) {
			for (int z = min.z(); z <= max.z(); z++) {
				for (int x = min.x(); x <= max.x(); x++) {
					int i = reversePalette.getOrDefault(get(x, y, z), 0);
					array.set(y * size.x() * size.z() + z * size.x() + x, i);
				}
			}
		}

		return new PackedArraySchematic(palette, array, min, size);
	}

	default void serialize(DataOutput stream) throws IOException {
		compact().serialize(stream);
	}

	static PackedArraySchematic deserialize(DataInput stream, Voxels voxels, Registries registries) throws IOException {
		return PackedArraySchematic.deserialize(stream, voxels, registries);
	}

	static PackedArraySchematic deserialize(DataInput stream, Voxelity platform) throws IOException {
		return deserialize(stream, platform.getVoxels(), platform.getRegistries());
	}

	static PackedArraySchematic copyFrom(VoxelView view, Box3i bounds, Vec3i origin) {
		Vec3i min = bounds.min().sub(origin);
		Vec3i max = bounds.max().sub(origin);
		Vec3i size = bounds.size();
		List<Voxel> palette = new ArrayList<>();
		Map<Voxel, Integer> reversePalette = new HashMap<>();
		int volume = size.x() * size.y() * size.z();
		PackedArray array = PackedArray.create(volume, volume);

		for (int y = min.x(); y <= max.x(); y++) {
			for (int z = min.z(); z <= max.z(); z++) {
				for (int x = min.x(); x <= max.x(); x++) {
					Voxel voxel = view.get(x + origin.x(), y + origin.y(), z + origin.z());
					int i = voxel == null ? 0 : reversePalette.computeIfAbsent(voxel, v -> {
						palette.add(v);
						return palette.size();
					});
					array.set(y * size.x() * size.z() + z * size.x() + x, i);
				}
			}
		}

		return new PackedArraySchematic(palette, array, min, size);
	}

	default void pasteTo(MutableVoxelView view, Vec3i translate) {
		Box3i bounds = getBounds();
		Vec3i min = bounds.min();
		Vec3i max = bounds.max();

		for (int y = min.x(); y <= max.x(); y++) {
			for (int z = min.z(); z <= max.z(); z++) {
				for (int x = min.x(); x <= max.x(); x++) {
					Voxel voxel = get(x, y, z);
					if (voxel != null) view.set(x + translate.x(), y + translate.y(), z + translate.z(), voxel);
				}
			}
		}
	}
}
