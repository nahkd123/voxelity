package io.github.nahkd123.voxelity.schematic;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.nahkd123.voxelity.math.Vec3i;
import io.github.nahkd123.voxelity.registry.Registries;
import io.github.nahkd123.voxelity.utils.packed.PackedArray;
import io.github.nahkd123.voxelity.utils.packed.PackedLongArray;
import io.github.nahkd123.voxelity.voxel.Voxel;
import io.github.nahkd123.voxelity.voxel.Voxels;

public class PackedArraySchematic implements Schematic {
	private List<Voxel> palette;
	private PackedArray array;
	private Vec3i boundsMin;
	private Vec3i boundsSize;

	public PackedArraySchematic(List<Voxel> palette, PackedArray array, Vec3i boundsMin, Vec3i boundsSize) {
		this.palette = palette;
		this.array = array;
		this.boundsMin = boundsMin;
		this.boundsSize = boundsSize;
	}

	@Override
	public Voxel get(int x, int y, int z) {
		int lx = x - boundsMin.x(), ly = y - boundsMin.y(), lz = z - boundsMin.z();
		if (lx < 0 || lx >= boundsSize.x()) return null;
		if (ly < 0 || ly >= boundsSize.y()) return null;
		if (lz < 0 || lz >= boundsSize.z()) return null;
		int i = array.get(ly * boundsSize.x() * boundsSize.z() + lz * boundsSize.x() + lx);
		return i == 0 || i > palette.size() ? null : palette.get(i - 1);
	}

	@Override
	public Vec3i getBoundsMin() { return getBoundsMin(); }

	@Override
	public Vec3i getSize() { return boundsSize; }

	@Override
	public Schematic translateBounds(int dx, int dy, int dz) {
		return new PackedArraySchematic(palette, array, boundsMin.add(dx, dy, dz), boundsSize);
	}

	@Override
	public List<Voxel> getPalette() { return Collections.unmodifiableList(palette); }

	public PackedArray getArray() { return array; }

	@Override
	public PackedArraySchematic compact() {
		if (array instanceof PackedLongArray longArray) {
			int bpe = longArray.getBitsPerElement();
			int targetBpe = PackedArray.computeBitsPerElement(palette.size() + 1);
			if (bpe != targetBpe) return Schematic.super.compact();
		}

		return this;
	}

	@Override
	public void serialize(DataOutput stream) throws IOException {
		stream.writeByte(0); // Version 0 -> deserializeVersion0()
		boundsMin.serialize(stream);
		boundsSize.serialize(stream);
		stream.writeInt(palette.size());
		for (Voxel voxel : palette) voxel.serialize(stream);
		array.serialize(stream);
	}

	public static PackedArraySchematic deserialize(DataInput stream, Voxels voxels, Registries registries) throws IOException {
		int version = stream.readUnsignedByte();
		return switch (version) {
		case 0 -> deserializeVersion0(stream, voxels, registries);
		default -> throw new IOException("Unsupported Voxelity schematic version: 0x%02x".formatted(version));
		};
	}

	private static PackedArraySchematic deserializeVersion0(DataInput stream, Voxels voxels, Registries registries) throws IOException {
		Vec3i min = Vec3i.deserialize(stream);
		Vec3i size = Vec3i.deserialize(stream);
		int paletteSize = stream.readInt();
		List<Voxel> palette = new ArrayList<>();
		for (int i = 0; i < paletteSize; i++) palette.add(voxels.deserialize(stream, registries));
		PackedArray array = PackedArray.deserialize(stream);
		return new PackedArraySchematic(palette, array, min, size);
	}
}
