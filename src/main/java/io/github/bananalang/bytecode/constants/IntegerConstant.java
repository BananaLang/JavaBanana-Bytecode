package io.github.bananalang.common.constants;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public final class IntegerConstant extends BBCConstant {
    public final BigInteger value;

    public IntegerConstant(BigInteger value) {
        this.value = value;
    }

    @Override
    public ByteBuffer write(ByteBuffer bb) {
        bb = ensureCapacity(bb, 2).putShort(INTEGER);
        byte[] data = value.toByteArray();
        return ensureCapacity(bb, 4 + data.length)
               .putInt(data.length)
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
