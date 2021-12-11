package io.github.bananalang.common.constants;

import java.nio.ByteBuffer;

public final class StringConstant extends BBCConstant {
    public final int index;

    public StringConstant(int index) {
        this.index = index;
    }

    @Override
    public ByteBuffer write(ByteBuffer bb) {
        return ensureCapacity(bb, 6).putShort(STRING)
                                    .putInt(index);
    }

    @Override
    public String toString() {
        return Integer.toString(index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringConstant)) return false;
        StringConstant other = (StringConstant)o;
        return this.index == other.index;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(index);
    }
}
