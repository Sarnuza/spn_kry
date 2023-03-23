public class CTR {

    private final static int SIZE_OF_BYTE = 8;
    private final static int BYTE_MASK = (1 << SIZE_OF_BYTE) - 1;

    private static final int SIZE_OF_BLOCK = 16;
    private static final int BLOCK_MASK = (1 << SIZE_OF_BLOCK) - 1;

    private final SPN spn;

    public CTR(SPN spn) {
        this.spn = spn;
    }

    public byte[] decrypt(byte[] cipher) {
        int nonce = getBlock(cipher[0], cipher[1]);

        // plain text does not contain the nonce -> 2 bytes shorter
        byte[] paddedPlainTextBytes = new byte[cipher.length - 2];

        int counter = 0;
        for (int i = 2; i < cipher.length; i = i + 2) {
            int increasedNonce = (nonce + counter++) & BLOCK_MASK;
            int encryptedNonce = spn.encrypt(increasedNonce);

            int block = getBlock(cipher[i], cipher[i + 1]);
            int decryptedBlock = encryptedNonce ^ block;

            // shifted by two indices because nonce is no longer included
            paddedPlainTextBytes[i - 2] = (byte) ((decryptedBlock >>> SIZE_OF_BYTE) & BYTE_MASK);
            paddedPlainTextBytes[i - 1] = (byte) (decryptedBlock & BYTE_MASK);
        }

        return removePadding(paddedPlainTextBytes);
    }

    private byte[] removePadding(byte[] paddedBytes) {
        int unpaddedLength = paddedBytes.length;
        while (paddedBytes[unpaddedLength - 1] == Byte.MIN_VALUE || paddedBytes[unpaddedLength - 1] == 0) {
            unpaddedLength--;
        }

        byte[] bytesWithoutPadding = new byte[unpaddedLength];
        System.arraycopy(paddedBytes, 0, bytesWithoutPadding, 0, unpaddedLength);
        return bytesWithoutPadding;
    }

    private int getBlock(byte first, byte second) {
        // byte -> int cast retains sign bit, therefore we use Byte.toUnsignedInt
        return (first << SIZE_OF_BYTE) | Byte.toUnsignedInt(second);
    }
}