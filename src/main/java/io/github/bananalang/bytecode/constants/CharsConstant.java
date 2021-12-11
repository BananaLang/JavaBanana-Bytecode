package io.github.bananalang.common.constants;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class CharsConstant extends BBCConstant {
    public final String value;
    public final int hash;

    public CharsConstant(String value) {
        this.value = value;
        this.hash = value.hashCode();
    }

    public CharsConstant(String value, int hash) {
        this.value = value;
        this.hash = hash;
    }

    @Override
    public ByteBuffer write(ByteBuffer bb) {
        bb = ensureCapacity(bb, 6).putShort(CHARS)
                                  .putInt(hash);
        byte[] data = value.getBytes(StandardCharsets.UTF_8);
        return ensureCapacity(bb, 4 + data.length)
               .putInt(data.length)
               .put(data);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharsConstant)) return false;
        CharsConstant other = (CharsConstant)o;
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
