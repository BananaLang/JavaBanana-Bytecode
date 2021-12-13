package io.github.bananalang.bytecode.constants;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public final class IntegerConstant extends BBCConstant {
    public static final IntegerConstant ZERO = new IntegerConstant(BigInteger.ZERO);

    public final BigInteger value;

    public IntegerConstant(BigInteger value) {
        this.value = value;
    }

    @Override
    public ByteBuffer write(ByteBuffer bb) {
        bb = ensureCapacity(bb, 2).putShort(INTEGER);
        if (value.signum() == 0) {
            return ensureCapacity(bb, 4).putInt(0);
        }
        byte[] data = new byte[(value.bitLength() + 7) / 8];
        BigInteger x = value.signum() == 1 ? value : value.negate();
        for (int i = 0; i < data.length; i++) {
            data[i] = x.byteValue();
            x = x.shiftRight(8);
        }
        return ensureCapacity(bb, 4 + data.length)
               .putInt(data.length * value.signum())
               .put(data);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntegerConstant)) return false;
        IntegerConstant other = (IntegerConstant)o;
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
