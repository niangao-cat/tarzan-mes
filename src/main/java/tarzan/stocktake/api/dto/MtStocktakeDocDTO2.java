package tarzan.stocktake.api.dto;

import java.io.Serializable;

public class MtStocktakeDocDTO2 implements Serializable {
    private static final long serialVersionUID = -6226600117698313594L;

    private String eventRequestId;
    private String stocktakeId;

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(String stocktakeId) {
        this.stocktakeId = stocktakeId;
    }
}
