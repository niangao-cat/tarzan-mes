package tarzan.actual.api.dto;

import java.io.Serializable;

public class MtStocktakeActualDTO2 implements Serializable {
    private static final long serialVersionUID = 6604414642310787873L;
    private String stocktakeId;
    private String eventRequestId;

    public String getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(String stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }
}
