package io.github.bananalang.common;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.bananalang.common.constants.BBCConstant;

public final class ByteCodeFile {
    public static final short VERSION_MAJOR = 0, VERSION_MINOR = 0;

    private static final ByteBuffer EMPTY_BYTEBUFFER = ByteBuffer.allocate(0);

    private final List<BBCConstant> constantTable = new ArrayList<>();
    private ByteBuffer bytecode = EMPTY_BYTEBUFFER;

    public final int versionMajor, versionMinor;

    public ByteCodeFile() {
        this.versionMajor = VERSION_MAJOR;
        this.versionMinor = VERSION_MINOR;
    }

    public ByteCodeFile(int versionMajor, int versionMinor) {
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
    }

    public List<BBCConstant> getConstantTable() {
        return constantTable;
    }

    public ByteBuffer getBytecodeBuffer() {
        return bytecode;
    }

    public byte[] getBytecode() {
        return bytecode.array();
    }

    public void write(FileDescriptor fd) throws IOException {
        try (OutputStream out = new FileOutputStream(fd)) {
            write(out);
        }
    }

    public void write(File file) throws IOException {
        try (OutputStream out = new FileOutputStream(file)) {
            write(out);
        }
    }

    public void write(String fileName) throws IOException {
        try (OutputStream out = new FileOutputStream(fileName)) {
            write(out);
        }
    }

    public void write(OutputStream out) throws IOException {
        ByteBuffer bb = EMPTY_BYTEBUFFER;
        bb = ensureCapacity(bb, 10).put((byte)0xBA)
                                   .put((byte)0xBA)
                                   .putShort(VERSION_MAJOR)
                                   .putShort(VERSION_MINOR)
                                   .putInt(constantTable.size());
        for (BBCConstant constant : constantTable) {
            bb = constant.write(bb);
        }
        out.write(bb.array(), 0, bb.position());
        out.write(bytecode.array(), 0, bytecode.position());
        out.write(0);
        out.write(0); // EOF marker
    }

    public static ByteCodeFile read(FileDescriptor fd) throws IOException {
        try (InputStream in = new FileInputStream(fd)) {
            return read(in);
        }
    }

    public static ByteCodeFile read(File file) throws IOException {
        try (InputStream in = new FileInputStream(file)) {
            return read(in);
        }
    }

    public static ByteCodeFile read(String fileName) throws IOException {
        try (InputStream in = new FileInputStream(fileName)) {
            return read(in);
        }
    }

    public static ByteCodeFile read(InputStream in) throws IOException {
        if (in.read() != 0xBA || in.read() != 0xBA) {
            throw new ByteCodeFormatException("Magic mismatch!");
        }
        int versionMajor, versionMinor;
        versionMajor = in.read() | (in.read() << 8);
        versionMinor = in.read() | (in.read() << 8);
        if (versionMajor > VERSION_MAJOR || (versionMajor == VERSION_MAJOR && versionMinor > VERSION_MINOR)) {
            throw new ByteCodeFormatException("Bytecode format " + versionMajor + "." + versionMinor + " is too new! This only supports up to " + VERSION_MAJOR + "." + VERSION_MINOR + ".");
        }
        ByteCodeFile bbc = new ByteCodeFile(versionMajor, versionMinor);
        int constantTableLen = in.read() | (in.read() << 8) | (in.read() << 16) | (in.read() << 24);
        for (int i = 0; i < constantTableLen; i++) {
            bbc.constantTable.add(BBCConstant.read(in));
        }
        byte[] data = new byte[6]; // Max bytecode width
        int byteCodeType;
        while ((byteCodeType = in.read() | (in.read() << 8)) != ByteCodes.EOF) {
            int width;
            switch (byteCodeType) {
                case ByteCodes.DEBUG_PRINT:
                case ByteCodes.ADD:
                case ByteCodes.SUB:
                case ByteCodes.MUL:
                case ByteCodes.DIV:
                case ByteCodes.POP:
                case ByteCodes.LOAD_0:
                case ByteCodes.LOAD_1:
                case ByteCodes.LOAD_2:
                    width = 0;
                    break;
                case ByteCodes.LOAD_BYTE:
                case ByteCodes.LOAD_SBYTE:
                    width = 1;
                    break;
                case ByteCodes.LOAD_CONSTANT:
                    width = 4;
                    break;
                default:
                    throw new ByteCodeFormatException("Unknown bytecode type" + byteCodeType);
            }
            in.read(data, 0, width);
            bbc.ensureCapacity(width + 2)
               .putShort((short)byteCodeType)
               .put(data, 0, width);
        }
        return bbc;
    }

    private ByteBuffer ensureCapacity(ByteBuffer bb, int bytes) {
        if (bb == EMPTY_BYTEBUFFER) {
            bb = ByteBuffer.allocate(Math.max(10, bytes)).order(ByteOrder.LITTLE_ENDIAN);
        } else {
            int minimum = bb.position() + bytes + 1;
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
        }
        return bb;
    }

    private ByteBuffer ensureCapacity(int bytes) {
        if (bytecode == EMPTY_BYTEBUFFER) {
            bytecode = ByteBuffer.allocate(Math.max(10, bytes)).order(ByteOrder.LITTLE_ENDIAN);
            return bytecode;
        }
        int minimum = bytecode.position() + bytes;
        if (minimum > bytecode.capacity()) {
            int newCapacity = Math.max(bytecode.capacity() * 2 + 2, minimum);
            if (newCapacity < 0) {
                newCapacity = Integer.MAX_VALUE;
            }
            byte[] newBytecode = Arrays.copyOf(bytecode.array(), newCapacity);
            int pos = bytecode.position();
            bytecode = ByteBuffer.wrap(newBytecode).order(ByteOrder.LITTLE_ENDIAN);
            bytecode.position(pos);
        }
        return bytecode;
    }

    public void putLoadConstant(int index) {
        ensureCapacity(6);
        bytecode.putShort(ByteCodes.LOAD_CONSTANT)
                .putInt(index);
    }

    public void putByte(byte value) {
        ensureCapacity(3);
        bytecode.putShort(ByteCodes.LOAD_BYTE)
                .put(value);
    }

    public void putSByte(byte value) {
        ensureCapacity(3);
        bytecode.putShort(ByteCodes.LOAD_SBYTE)
                .put(value);
    }

    public void putCode(short code) {
        ensureCapacity(2);
        bytecode.putShort(code);
    }

    public void disassemble() {
        disassemble(System.out);
    }

    public void disassemble(PrintStream out) {
        out.println("Top-level:");
        out.println("---------------------");
        ByteBuffer reader = bytecode.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        for (reader.position(0); reader.position() < bytecode.position();) {
            String s = Integer.toString(reader.position());
            out.print(s);
            for (int i = 0; i < 8 - s.length(); i++) {
                out.print(" ");
            }
            short byteCodeType = reader.getShort();
            switch (byteCodeType) {
                case ByteCodes.LOAD_CONSTANT: {
                    int index = reader.getInt();
                    out.print("LOAD_CONSTANT  " + index + " (" + constantTable.get(index) + ")");
                    break;
                }
                case ByteCodes.LOAD_BYTE: {
                    byte value = reader.get();
                    out.print("LOAD_BYTE      " + (((int)(value >>> 1) << 1) | (value & 1)));
                    break;
                }
                case ByteCodes.LOAD_SBYTE: {
                    byte value = reader.get();
                    out.print("LOAD_SBYTE     " + value);
                    break;
                }
                default:
                    s = ByteCodes.getName(byteCodeType);
                    out.print(s);
                    for (int i = 0; i < 15 - s.length(); i++) {
                        out.print(" ");
                    }
                    break;
            }
            out.println();
        }
    }
}
