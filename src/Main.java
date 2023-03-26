import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Main {
    private static final int r = 4;
    private static final int n = 4;
    private static final int m = 4;
    private static final SBox sBox = new SBox(0xE, 0x4, 0xD, 0x1, 0x2, 0xF, 0xB, 0x8, 0x3, 0xA, 0x6, 0xC, 0x5, 0x9, 0x0, 0x7);
    private static final int[] pBox = new int[]{0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};
    private static final int key = 0b0011_1010_1001_0100_1101_0110_0011_1111;

    public static void main(String[] args) {
        SPN spn = new SPN(r, n, m, sBox, pBox, key);
        CTR ctr = new CTR(spn);
        byte[] bytes = readChiffreTextFromFile();
        if (bytes != null) {
            System.out.println(new String(ctr.decrypt(bytes)));
        }
    }

    //Read BitString from file and then convert BitString to byte array
    private static byte[] readChiffreTextFromFile() {
        try {
            System.out.println(Objects.requireNonNull(Main.class.getResource("/chiffre.txt")).getPath());
            var inputStreamReader = new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream("/chiffre.txt")));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String bitString = bufferedReader.readLine();
            return convertBitStringToByteArray(bitString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Convert BitString to byte array
    private static byte[] convertBitStringToByteArray(String bitString) {
        byte[] bytes = new byte[bitString.length() / 8];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(bitString.substring(i * 8, (i + 1) * 8), 2);
        }
        return bytes;
    }
}