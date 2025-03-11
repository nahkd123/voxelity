package io.github.nahkd123.voxelity.fabric.net;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import io.github.nahkd123.voxelity.math.Box3i;
import io.github.nahkd123.voxelity.math.Vec3i;
import io.github.nahkd123.voxelity.schematic.PackedArraySchematic;
import io.github.nahkd123.voxelity.schematic.Schematic;
import io.github.nahkd123.voxelity.utils.packed.PackedArray;
import io.github.nahkd123.voxelity.utils.packed.PackedLongArray;
import io.github.nahkd123.voxelity.voxel.Volume;
import io.github.nahkd123.voxelity.voxel.Voxel;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.encoding.VarInts;

public class VoxelityPayloads {
	public static <E extends Enum<E>> PacketCodec<ByteBuf, E> createEnumCodec(Collection<E> possibleValues) {
		if (possibleValues.size() == 0) throw new IllegalArgumentException("Must have at least 1 value");
		BiMap<String, E> mapping = HashBiMap.create();
		int maxLength = 0;

		for (E value : possibleValues) {
			String name = value.name();
			maxLength = Math.max(maxLength, name.length());
			mapping.put(name, value);
		}

		// TODO throw error when parsed to null
		return PacketCodecs
			.string(maxLength)
			.xmap(mapping::get, mapping.inverse()::get);
	}

	public static final PacketCodec<ByteBuf, Voxel> VOXEL = PacketCodecs
		.codec(BlockState.CODEC)
		.xmap(bs -> (Voxel) bs, v -> (BlockState) v);

	public static final PacketCodec<ByteBuf, PackedLongArray> PACKED_ARRAY_LONG = PacketCodec.ofStatic(
		(buf, value) -> {
			VarInts.write(buf, value.getBitsPerElement());
			VarInts.write(buf, value.getRawData().length);
			for (long l : value.getRawData()) buf.writeLong(l);
		},
		buf -> {
			int bpe = VarInts.read(buf);
			int length = VarInts.read(buf);
			long[] data = new long[length];
			for (int i = 0; i < data.length; i++) data[i] = buf.readLong();
			return new PackedLongArray(bpe, data);
		});

	public static final PacketCodec<ByteBuf, PackedArray> PACKED_ARRAY = PACKED_ARRAY_LONG.xmap(
		array -> array,
		array -> {
			if (!(array instanceof PackedLongArray longArray))
				throw new RuntimeException("Not a packed long array");
			return longArray;
		});

	public static final PacketCodec<ByteBuf, Vec3i> VEC3I = PacketCodec.tuple(
		PacketCodecs.VAR_INT, Vec3i::x,
		PacketCodecs.VAR_INT, Vec3i::y,
		PacketCodecs.VAR_INT, Vec3i::z,
		Vec3i::of);

	public static final PacketCodec<ByteBuf, Box3i> BOX3I = PacketCodec.tuple(
		PacketCodecs.VAR_INT, Box3i::x,
		PacketCodecs.VAR_INT, Box3i::y,
		PacketCodecs.VAR_INT, Box3i::z,
		PacketCodecs.VAR_INT, Box3i::sizeX,
		PacketCodecs.VAR_INT, Box3i::sizeY,
		PacketCodecs.VAR_INT, Box3i::sizeZ,
		Box3i::new);

	public static final PacketCodec<ByteBuf, PackedArraySchematic> PACKED_ARRAY_SCHEMATIC = PacketCodec.tuple(
		VEC3I, PackedArraySchematic::getBoundsMin,
		VEC3I, PackedArraySchematic::getSize,
		VOXEL.collect(PacketCodecs.toList()), PackedArraySchematic::getPalette,
		PACKED_ARRAY, PackedArraySchematic::getArray,
		(min, size, palette, array) -> new PackedArraySchematic(palette, array, min, size));

	public static final PacketCodec<ByteBuf, Schematic> SCHEMATIC = PACKED_ARRAY_SCHEMATIC.xmap(
		schematic -> schematic,
		schematic -> schematic.compact());

	public static final PacketCodec<ByteBuf, Map.Entry<Vec3i, long[]>> VOLUME_CHUNK = PacketCodec.<ByteBuf, Map.Entry<Vec3i, long[]>, Vec3i, long[]>tuple(
		VEC3I, Map.Entry::getKey,
		PacketCodec.<ByteBuf, long[]>ofStatic(
			(buf, value) -> {
				for (int i = 0; i < Volume.SEGMENTS_PER_CHUNK; i++) buf.writeLong(value[i]);
			},
			buf -> {
				long[] chunk = new long[Volume.SEGMENTS_PER_CHUNK];
				for (int i = 0; i < chunk.length; i++) chunk[i] = buf.readLong();
				return chunk;
			}),
		Map.Entry::getValue,
		Map::entry);

	public static final PacketCodec<ByteBuf, Volume> VOLUME = PacketCodec.tuple(
		VEC3I, Volume::getTranslation,
		VOLUME_CHUNK.collect(PacketCodecs.toCollection(HashSet::new)), vol -> vol.getChunks().entrySet(),
		Volume::new);
}
