package tarzan.stocktake.api.dto;

import java.io.Serializable;

public class MtStocktakeDocHisDTO implements Serializable {
    private static final long serialVersionUID = 5380888764724671301L;

    private String stocktakeId;
    private String stocktakeHisId;
    private String eventId;

    public String getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(String stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    public String getStocktakeHisId() {
        return stocktakeHisId;
    }

    public void setStocktakeHisId(String stocktakeHisId) {
        this.stocktakeHisId = stocktakeHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
