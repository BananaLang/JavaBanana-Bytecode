package io.github.bananalang.bytecode;

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

    LOGICAL_OR = 132,
    LOGICAL_AND = 133,
    BITWISE_OR = 134,
    BITWISE_XOR = 135,
    BITWISE_AND = 136,
    EQUALS = 137,
    NOT_EQUALS = 138,
    LESS_THAN = 139,
    GREATER_THAN = 140,
    LESS_THAN_EQUALS = 141,
    GREATER_THAN_EQUALS = 142,
    LEFT_SHIFT = 143,
    RIGHT_SHIFT = 144,
    ADD = 128,
    SUBTRACT = 129,
    MULTIPLY = 130,
    DIVIDE = 131,
    MODULUS = 145;

    @Deprecated
    public static final short SUB = SUBTRACT, MUL = MULTIPLY, DIV = DIVIDE;

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
            case LOAD_0:
                return "LOAD_0";
            case LOAD_1:
                return "LOAD_1";
            case LOAD_2:
                return "LOAD_2";
            case LOGICAL_OR:
                return "LOGICAL_OR";
            case LOGICAL_AND:
                return "LOGICAL_AND";
            case BITWISE_OR:
                return "BITWISE_OR";
            case BITWISE_XOR:
                return "BITWISE_XOR";
            case BITWISE_AND:
                return "BITWISE_AND";
            case EQUALS:
                return "EQUALS";
            case NOT_EQUALS:
                return "NOT_EQUALS";
            case LESS_THAN:
                return "LESS_THAN";
            case GREATER_THAN:
                return "GREATER_THAN";
            case LESS_THAN_EQUALS:
                return "LESS_THAN_EQUALS";
            case GREATER_THAN_EQUALS:
                return "GREATER_THAN_EQUALS";
            case LEFT_SHIFT:
                return "LEFT_SHIFT";
            case RIGHT_SHIFT:
                return "RIGHT_SHIFT";
            case ADD:
                return "ADD";
            case SUBTRACT:
                return "SUBTRACT";
            case MULTIPLY:
                return "MULTIPLY";
            case DIVIDE:
                return "DIVIDE";
            case MODULUS:
                return "MODULUS";
            default:
                return "UNKNOWN_" + Integer.toHexString(Short.toUnsignedInt((short)code));
        }
    }
}
