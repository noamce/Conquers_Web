package GameObjects;

public class TerritoryDataTable {

    private final String type;
    private final int maintenance;
    private final int fp;
    private final int amount;
    private final int rank;
    private final int maintenanceCost;
    private final int totalFirePower;

    public TerritoryDataTable(String type, int maintenance, int fp, int amount, int rank, int maintenanceCost, int totalFirePower) {
        this.type = type;
        this.maintenance = maintenance;
        this.fp = fp;
        this.amount = amount;
        this.rank = rank;
        this.maintenanceCost = maintenanceCost;
        this.totalFirePower = totalFirePower;
    }
}
