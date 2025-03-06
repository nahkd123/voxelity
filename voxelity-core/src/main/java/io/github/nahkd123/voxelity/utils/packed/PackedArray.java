package io.github.nahkd123.voxelity.utils.packed;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface PackedArray {
	/**
	 * <p>
	 * Get the number of possible values this packed array can store.
	 * </p>
	 * 
	 * @return The number of possible values.
	 */
	int getPossibleValues();

	int size();

	int get(int index) throws ArrayIndexOutOfBoundsException;

	/**
	 * <p>
	 * Set a value at specified index in this packed array.
	 * </p>
	 * 
	 * @param index The index in this packed array.
	 * @param value The value to set in this packed array.
	 * @throws IllegalArgumentException       if value is outside of bounds (lower
	 *                                        than 0 or greater than or equals to
	 *                                        number of possible values value).
	 * @throws ArrayIndexOutOfBoundsException if index is outside of bounds.
	 */
	void set(int index, int value) throws IllegalArgumentException, ArrayIndexOutOfBoundsException;

	static PackedLongArray create(int preferredPossibleValues, int preferredSize) {
		int bitsPerElement = computeBitsPerElement(preferredPossibleValues);
		int elementsPerSegment = 64 / bitsPerElement;
		long[] data = new long[Math.ceilDiv(preferredSize, elementsPerSegment)];
		return new PackedLongArray(bitsPerElement, data);
	}

	static int computeBitsPerElement(int preferredPossibleValues) {
		return (int) Math.floor(Math.log(preferredPossibleValues) / Math.log(2));
	}

	// static int SERIALIZE_TYPE_INTARRAY = 0x00;
	static int SERIALIZE_TYPE_LONG = 0x01;

	void serialize(DataOutput stream) throws IOException;

	static PackedArray deserialize(DataInput stream) throws IOException {
		int type = stream.readUnsignedByte();
		return switch (type) {
		case SERIALIZE_TYPE_LONG -> PackedLongArray.deserializeNoHeader(stream);
		default -> throw new IOException("Unknown packed array type ID: 0x%02x".formatted(type));
		};
	}
}
