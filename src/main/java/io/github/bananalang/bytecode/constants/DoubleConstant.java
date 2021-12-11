package io.github.bananalang.bytecode.constants;

import java.nio.ByteBuffer;

public final class DoubleConstant extends BBCConstant {
    public final double value;

    public DoubleConstant(double value) {
        this.value = value;
    }

    @Override
    public ByteBuffer write(ByteBuffer bb) {
        return ensureCapacity(bb, 10).putShort(DOUBLE)
                                     .putDouble(value);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleConstant)) return false;
        DoubleConstant other = (DoubleConstant)o;
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }
}
