public class SBox {
    private final int[] sBox;
    private final int[] inverseSBox;

    public SBox(int... substitutions) {
        sBox = substitutions;
        inverseSBox = new int[sBox.length];

        initInverseSBox();
    }

    private void initInverseSBox() {
        for (int i = 0; i < sBox.length; i++) {
            inverseSBox[sBox[i]] = i;
        }
    }

    public int[] get() {
        return sBox;
    }
}
