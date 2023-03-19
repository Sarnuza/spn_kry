public class Main {
    private static final int r = 4;
    private static final int n = 4;
    private static final int m = 4;
    private static final SBox sBox = new SBox(0xE, 0x4, 0xD, 0x1, 0x2, 0xF, 0xB, 0x8, 0x3, 0xA, 0x6, 0xC, 0x5, 0x9, 0x0, 0x7);
    private static final int[] pBox = new int[]{0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};
    private static final int key = 0b0011_1010_1001_0100_1101_0110_0011_1111;

    public static void main(String[] args) {
        String bitString = FileUtil.readFirstLineFromFile("/chiffre.txt");
        byte[] bytes = BitUtil.readBitString(bitString);

        SPN spn = new SPN(r, n, m, sBox, pBox, key);
        CTR ctr = new CTR(spn);

        byte[] plainTextBytes = ctr.decrypt(bytes);
        System.out.println(new String(plainTextBytes));
    }
}