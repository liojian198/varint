package com.bin.varint;

public class VarIntUtilTest {
    public static void main(String[] args) {
        Byte[] result = VarIntUtil.encodeVarint(999L);
        System.out.println(result.length);
        System.out.println(VarIntUtil.decodeVarint(result));
    }
}
