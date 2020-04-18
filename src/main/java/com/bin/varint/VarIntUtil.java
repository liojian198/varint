package com.bin.varint;

public class VarIntUtil {

    private static int MAX_SIZE = 50; // 暂定

    public static Byte[] encodeVarint (Long value) {
        if(value < 0) {
            return null;
        }
        Byte[] result = new Byte[MAX_SIZE];
        int n = 0;
        // 1.每个字节的最高位是保留位, 如果是1说明后面的字节还是属于当前数据的,如果是0,那么这是当前数据的最后一个字节数据
        //  看下面代码,因为一个字节最高位是保留位,那么这个字节中只有下面7bits可以保存数据
        //  所以,如果x>127,那么说明这个数据还需大于一个字节保存,所以当前字节最高位是1,看下面的result[n] = 0x80 | ...
        //  0x80说明将这个字节最高位置为1, 后面的x&0x7F是取得x的低7位数据, 那么0x80 | uint8(x&0x7F)整体的意思就是
        //  这个字节最高位是1表示这不是最后一个字节,后面7为是正式数据! 注意操作下一个字节之前需要将x>>=7
        // 2.看如果x<=127那么说明x现在使用7bits可以表示了,那么最高位没有必要是1,直接是0就ok!所以最后直接是result[n] = uint8(x)
        //
        // 如果数据大于一个字节(127是一个字节最大数据), 那么继续, 即: 需要在最高位加上1
        // 转换成字节数组的时候，是小端的模式/

        for(; value > 127; n ++) {
            byte temp = (byte) (value & 0x7F);
            result [n] = (byte)(0x80 | temp);
            value = value >> 7;
        }

        result[n] = (byte) (value & 0x7F);

        // 暂时的办法
        Byte[] result1 = new Byte[n + 1];

        for(int i = 0; i <= n ; i++){
            result1[i] = result[i];
        }

        return result1;
    }

    public static Long decodeVarint (Byte[] values) {
        long result = 0;
        for(int step = 0, i = 0; step < 64; step += 7, i++) {
            if (i >= values.length ) {
                return null;
            }
            Byte t = values[i];
            // 1: b & 0x7F 获取下7bits有效数据
            // 2: (b & 0x7F) << step 由于是小端序, 所以每次处理一个Byte数据, 都需要向高位移动7bits
            // 3: 将数据x和当前的这个字节数据 | 在一起
            result |= (t & 0x7F) << step;
            if ((t & 0x80) == 0) {
                return result;
            }
        }
        return null;
    }
}
