package io.github.bananalang.common;

public final class ByteCodes {
    public static final short

    EOF = 0,

    DEBUG_PRINT = 1,
    LOAD_CONSTANT = 2,
    LOAD_BYTE = 3,
    LOAD_SBYTE = 4,
    POP = 5,
    LOAD_0 = 6,
    LOAD_1 = 7,
    LOAD_2 = 8,

    ADD = 128,
    SUB = 129,
    MUL = 130,
    DIV = 131;

    // Disable instantiation
    private ByteCodes() {
    }

    public static String getName(int code) {
        switch (code) {
            case EOF:
                return "EOF";
            case DEBUG_PRINT:
                return "DEBUG_PRINT";
            case LOAD_CONSTANT:
                return "LOAD_CONSTANT";
            case LOAD_BYTE:
                return "LOAD_BYTE";
            case LOAD_SBYTE:
                return "LOAD_SBYTE";
            case POP:
                return "POP";
            case ADD:
                return "ADD";
            case LOAD_0:
                return "LOAD_0";
            case LOAD_1:
                return "LOAD_1";
            case LOAD_2:
                return "LOAD_2";
            case SUB:
                return "SUB";
            case MUL:
                return "MUL";
            case DIV:
                return "DIV";
            default:
                return "UNKNOWN_" + Integer.toHexString(Short.toUnsignedInt((short)code));
        }
    }
}
