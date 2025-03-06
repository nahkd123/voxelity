package io.github.nahkd123.voxelity.utils.packed;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class PackedLongArray implements PackedArray {
	private int bitsPerElement, possibleValues, size, elementsPerSegment;
	private long[] data;
	private long elementMask;

	public PackedLongArray(int bitsPerElement, long[] data) {
		if (bitsPerElement < 1) throw new IllegalArgumentException("Each element must claim at least 1 bit");
		if (bitsPerElement > 32) throw new IllegalArgumentException("Each element can only claim at most 32 bits");
		this.bitsPerElement = bitsPerElement;
		this.data = data;

		possibleValues = 1 << bitsPerElement;
		elementMask = possibleValues - 1;
		elementsPerSegment = 64 / bitsPerElement;
		size = data.length * elementsPerSegment;
	}

	@Override
	public int getPossibleValues() { return possibleValues; }

	@Override
	public int size() {
		return size;
	}

	@Override
	public int get(int index) throws ArrayIndexOutOfBoundsException {
		if (index < 0 || index >= size) throw new ArrayIndexOutOfBoundsException();
		int segmentIndex = index / elementsPerSegment;
		int elementIndex = index % elementsPerSegment;
		int shift = elementIndex * bitsPerElement;
		return (int) ((data[segmentIndex] & (elementMask << shift)) >>> shift);
	}

	@Override
	public void set(int index, int value) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
		if (index < 0 || index >= size) throw new ArrayIndexOutOfBoundsException();
		if (value < 0 || possibleValues != Integer.MIN_VALUE && value >= possibleValues)
			throw new IllegalArgumentException("%d outside [0; %d)".formatted(value, possibleValues));
		int segmentIndex = index / elementsPerSegment;
		int elementIndex = index % elementsPerSegment;
		int shift = elementIndex * bitsPerElement;
		data[segmentIndex] &= ~(elementMask << shift);
		data[segmentIndex] |= ((long) value) << shift;
	}

	public int getBitsPerElement() { return bitsPerElement; }

	public long[] getRawData() { return data; }

	@Override
	public void serialize(DataOutput stream) throws IOException {
		stream.writeByte(SERIALIZE_TYPE_LONG);
		stream.writeByte(bitsPerElement);
		stream.writeInt(data.length);
		for (int i = 0; i < data.length; i++) stream.writeLong(data[i]);
	}

	static PackedLongArray deserializeNoHeader(DataInput stream) throws IOException {
		int bitsPerElement = stream.readUnsignedByte();
		int length = stream.readInt();
		long[] data = new long[length];
		for (int i = 0; i < data.length; i++) data[i] = stream.readLong();
		return new PackedLongArray(bitsPerElement, data);
	}
}
