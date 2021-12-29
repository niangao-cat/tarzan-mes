package tarzan.actual.api.dto;

import java.io.Serializable;

public class MtStocktakeActualHisDTO implements Serializable {
    private static final long serialVersionUID = 9000484166381620134L;
    private String stocktakeActualId;
    private String stocktakeActualHisId;
    private String eventId;

    public String getStocktakeActualId() {
        return stocktakeActualId;
    }

    public void setStocktakeActualId(String stocktakeActualId) {
        this.stocktakeActualId = stocktakeActualId;
    }

    public String getStocktakeActualHisId() {
        return stocktakeActualHisId;
    }

    public void setStocktakeActualHisId(String stocktakeActualHisId) {
        this.stocktakeActualHisId = stocktakeActualHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
