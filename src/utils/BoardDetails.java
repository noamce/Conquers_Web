package utils;
import java.util.List;

public class BoardDetails {
    private int cols;
    private int rows;
    List<OneTerritoryDetails> territoryMapToSend;
    public BoardDetails(int cols, int rows, List<OneTerritoryDetails> territoryMapToSend) {
        this.cols=cols;
        this.rows=rows;
        this.territoryMapToSend=territoryMapToSend;
    }
}
