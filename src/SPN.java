public class SPN {

    private static final int SIZE_OF_BLOCK = 16;
    private static final int BLOCK_MASK = (1 << SIZE_OF_BLOCK) - 1;

    private final int roundCount; // r
    private final int substitutionBlockLength; // n
    private final int substitutionBlockCount; // m

    private final SBox sBox;
    private final int[] pBox;

    // int has a length of 32 bits -> s = 32
    private final int key;
    private final int[] encryptionRoundKeys;

    public SPN(int r, int n, int m, SBox sBox, int[] pBox, int key) {
        roundCount = r;
        substitutionBlockLength = n;
        substitutionBlockCount = m;

        this.sBox = sBox;
        this.pBox = pBox;

        this.key = key;
        encryptionRoundKeys = new int[roundCount + 1];
        initRoundKeys();
    }

    private void initRoundKeys() {
        initEncryptionRoundKeys();
    }

    private void initEncryptionRoundKeys() {
        for (int i = 0; i <= roundCount; i++) {
            encryptionRoundKeys[i] = getRoundKey(i);
        }
    }

    public int getRoundKey(int round) {
        int start = 4 * (roundCount - round);
        return (key >> start) & BLOCK_MASK;
    }

    public int encrypt(int clearText) {
        return encryptInternal(clearText, encryptionRoundKeys, sBox.get());
    }

    private int encryptInternal(int clearText, int[] keys, int[] sBox) {
        int cipher;
        // initial round
        cipher = clearText ^ keys[0];
        // r -1 regular rounds
        for (int i = 1; i < roundCount; i++) {
            cipher = substitute(cipher, sBox);
            cipher = permute(cipher);
            cipher ^= keys[i];
        }
        // final shortened round
        cipher = substitute(cipher, sBox);
        cipher ^= keys[roundCount];
        return cipher;
    }

    public int substitute(int number, int[] sBox) {
        int result = 0;
        for (int i = 0; i < substitutionBlockCount; i++) {
            int extract = (number >>> substitutionBlockLength * i) & ((1 << substitutionBlockLength) - 1);
            int substitution = sBox[extract];
            result |= substitution << substitutionBlockLength * i;
        }
        return result;
    }

    public int permute(int number) {
        int result = 0;
        for (int i = 0; i < substitutionBlockLength * substitutionBlockCount; i++) {
            result |= ((number >>> i) & 1) << pBox[i];
        }
        return result;
    }
}