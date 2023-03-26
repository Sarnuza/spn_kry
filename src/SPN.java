public class SPN {

    private final int r; // r
    private final int n; // n
    private final int m; // m
    private final SBox sBox;
    private final int[] pBox;
    private final int key;
    private final int[] encryptionRoundKeys;

    public SPN(int r, int n, int m, SBox sBox, int[] pBox, int key) {
        this.r = r;
        this.n = n;
        this.m = m;

        this.sBox = sBox;
        this.pBox = pBox;

        this.key = key;
        encryptionRoundKeys = new int[this.r + 1];
        initRoundKeys();
    }

    private void initRoundKeys() {
        for (int i = 0; i <= r; i++) {
            encryptionRoundKeys[i] = getRoundKey(i);
        }
    }

    //Get round key from key by round
    public int getRoundKey(int round) {
        int start = 4 * (r - round);
        int shift = key >> start;
        int BLOCK_MASK = (1 << 16) - 1;
        return shift & BLOCK_MASK;
    }

    public int encrypt(int text) {
        int[] sBox = this.sBox.getSBoxTable();

        // first round
        int chiffre = text ^ encryptionRoundKeys[0];

        // normal rounds
        for (int i = 1; i < r; i++) {
            chiffre = substitute(chiffre, sBox);
            chiffre = permute(chiffre);
            chiffre ^= encryptionRoundKeys[i];
        }

        // last round
        chiffre = substitute(chiffre, sBox);
        chiffre = chiffre ^ encryptionRoundKeys[r];
        return chiffre;
    }

    /**
     * This method substitutes a given input using a substitution table
     * @param input The input to be substituted
     * @param substitutionTable The substitution table to be used
     * @return The substituted output
     */
    public int substitute(int input, int[] substitutionTable) {
        int output = 0;
        for (int i = 0; i < m; i++) { // iterate over 8 nibbles (4-bit chunks)
            int nibble = (input >> (i * n)) & ((1 << n) - 1); // extract the nibble
            output |= substitutionTable[nibble] << (i * n); // substitute the nibble and shift it back into place
        }
        return output;
    }

    /**
     * This method permutes a given input using a permutation table
     * @param input The input to be permuted
     * @return The permuted output
     */
    public int permute(int input) {
        int output = 0;
        int mask = 0x1; // binary 00000001

        // For each bit in the SPN permutation
        for (int i = 0; i < n * m; i++) {
            // Get the input bit corresponding to the current SPN bit
            int inputBit = (input >> pBox[i]) & mask;

            // Set the output bit corresponding to the current SPN bit
            output |= (inputBit << i);
        }

        return output;
    }
}