public final class BitUtil {

    private final static char ONE = '1';

    private BitUtil() {
    }

    public static byte[] readBitString(String bitString) {
        if (bitString == null || bitString.isEmpty()) {
            return null;
        }

        byte[] bytes = new byte[bitString.length() / 8];

        int index = 0;
        while (index < bitString.length()) {
            if (bitString.charAt(index) == ONE) {
                bytes[index / 8] |= 1 << 7 - (index % 8);
            }

            index++;
        }

        return bytes;
    }
}
