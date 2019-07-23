package GameObjects;

public class unitDataTable {

    private final String type;
    private final int price;
    private final int fp;
    private final int subduction;
    private final int rank;

    public unitDataTable(String type, int fp, int price, int subduction, int rank) {

        this.type = type;
        this.fp = fp;
        this.price = price;
        this.subduction = subduction;
        this.rank = rank;
    }


}
