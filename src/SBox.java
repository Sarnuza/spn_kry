public class SBox {
    private final int[] sBoxTable;

    public SBox(int... sBoxTable) {
        this.sBoxTable = sBoxTable;
    }

    public int[] getSBoxTable() {
        return sBoxTable;
    }

}