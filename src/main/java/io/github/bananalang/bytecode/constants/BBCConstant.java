package io.github.bananalang.bytecode.constants;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import io.github.bananalang.bytecode.ByteCodeFormatException;

public abstract class BBCConstant {
    public static final short INTEGER = 0,
                              CHARS = 1,
                              STRING = 2,
                              DOUBLE = 3;

    public abstract ByteBuffer write(ByteBuffer bb);

    protected ByteBuffer ensureCapacity(ByteBuffer bb, int bytes) {
        int minimum = bb.position() + bytes;
        if (minimum > bb.capacity()) {
            int newCapacity = Math.max(bb.capacity() * 2 + 2, minimum);
            if (newCapacity < 0) {
                newCapacity = Integer.MAX_VALUE;
            }
            byte[] newBB = Arrays.copyOf(bb.array(), newCapacity);
            int pos = bb.position();
            bb = ByteBuffer.wrap(newBB).order(ByteOrder.LITTLE_ENDIAN);
            bb.position(pos);
        }
        return bb;
    }

    public static BBCConstant read(InputStream in) throws IOException {
        int type = in.read() | (in.read() << 8);
        switch (type) {
            case INTEGER: {
                int len = in.read() | (in.read() << 8) | (in.read() << 16) | (in.read() << 24);
                byte[] data = new byte[len];
                in.read(data);
                return new IntegerConstant(new BigInteger(data));
            }
            case CHARS: {
                int hash = in.read() | (in.read() << 8) | (in.read() << 16) | (in.read() << 24);
                int len = in.read() | (in.read() << 8) | (in.read() << 16) | (in.read() << 24);
                byte[] data = new byte[len];
                in.read(data);
                return new CharsConstant(new String(data, StandardCharsets.UTF_8), hash);
            }
            case STRING: {
                int index = in.read() | (in.read() << 8) | (in.read() << 16) | (in.read() << 24);
                return new StringConstant(index);
            }
            case DOUBLE: {
                long longVal = readL(in) | (readL(in) << 8) | (readL(in) << 16) | (readL(in) << 24) |
                               (readL(in) << 32) | (readL(in) << 40) | (readL(in) << 48) | (readL(in) << 56);
                return new DoubleConstant(Double.longBitsToDouble(longVal));
            }
        }
        throw new ByteCodeFormatException("Unsupported constant type " + type);
    }

    private static long readL(InputStream in) throws IOException {
        return (long)in.read();
    }
}
