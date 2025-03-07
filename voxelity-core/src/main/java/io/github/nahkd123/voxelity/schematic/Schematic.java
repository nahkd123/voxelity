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
import io.github.nahkd123.voxelity.voxel.Volume;
import io.github.nahkd123.voxelity.voxel.Voxel;
import io.github.nahkd123.voxelity.voxel.Voxels;
import io.github.nahkd123.voxelity.voxel.view.MutableVoxelView;
import io.github.nahkd123.voxelity.voxel.view.VoxelBuffer;
import io.github.nahkd123.voxelity.voxel.view.VoxelView;

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
 * @see #copyFrom(VoxelView, Box3i, Vec3i, Volume)
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

	// Variable naming conventions:
	// - sx/sy/sz: Schematic coords
	// - wx/wy/wz: World/view coords

	/**
	 * <p>
	 * Copy an area in {@link VoxelView} into a schematic.
	 * </p>
	 * 
	 * @param view   The voxel view.
	 * @param bounds The volume in voxel view to copy.
	 * @param origin The origin in the voxel view.
	 * @param mask   Selection mask. Use {@code null} to select everything in
	 *               bounds, or a mask to only retain voxels occupied by the mask.
	 *               If the voxel is not occupied by the mask, the schematic will
	 *               represent it as void voxel.
	 * @return The schematic, ready to be serialized or paste to
	 *         {@link MutableVoxelView}.
	 */
	static PackedArraySchematic copyFrom(VoxelView view, Box3i bounds, Vec3i origin, Volume mask) {
		Vec3i wMin = bounds.min();
		Vec3i wMax = bounds.max();
		Vec3i size = bounds.size();
		List<Voxel> palette = new ArrayList<>();
		Map<Voxel, Integer> reversePalette = new HashMap<>();
		int volume = size.x() * size.y() * size.z();
		PackedArray array = PackedArray.create(volume, volume);

		for (int wy = wMin.x(); wy <= wMax.x(); wy++) {
			for (int wz = wMin.z(); wz <= wMax.z(); wz++) {
				for (int wx = wMin.x(); wx <= wMax.x(); wx++) {
					int sx = wx - origin.x();
					int sy = wy - origin.y();
					int sz = wz - origin.z();
					int arrayIdx = sy * size.x() * size.z() + sz * size.x() + sx;

					if (mask != null && !mask.isOccupied(wx, wy, wz)) {
						array.set(arrayIdx, 0);
						continue;
					}

					Voxel voxel = view.get(wx, wy, wz);
					int i = voxel == null ? 0 : reversePalette.computeIfAbsent(voxel, v -> {
						palette.add(v);
						return palette.size();
					});
					array.set(arrayIdx, i);
				}
			}
		}

		return new PackedArraySchematic(palette, array, wMin, size);
	}

	/**
	 * <p>
	 * Paste this schematic to mutable voxel view.
	 * </p>
	 * 
	 * @param view      The mutable voxel view.
	 * @param translate Translate the schematic by some amount.
	 * @param mask      Selection mask. See
	 *                  {@link #copyFrom(VoxelView, Box3i, Vec3i, Volume)} for more
	 *                  info.
	 */
	default void pasteTo(MutableVoxelView view, Vec3i translate, Volume mask) {
		Box3i bounds = getBounds();
		Vec3i sMin = bounds.min();
		Vec3i sMax = bounds.max();

		for (int sy = sMin.x(); sy <= sMax.x(); sy++) {
			for (int sz = sMin.z(); sz <= sMax.z(); sz++) {
				for (int sx = sMin.x(); sx <= sMax.x(); sx++) {
					Voxel voxel = get(sx, sy, sz);
					if (voxel == null) continue;

					if (mask != null) {
						int wx = sx + translate.x();
						int wy = sy + translate.y();
						int wz = sz + translate.z();
						if (mask.isOccupied(wx, wy, wz)) continue;
					}

					view.set(sx + translate.x(), sy + translate.y(), sz + translate.z(), voxel);
				}
			}
		}
	}
}
