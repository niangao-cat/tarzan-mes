package tarzan.stocktake.domain.vo;

import tarzan.stocktake.domain.entity.MtStocktakeDoc;

public class MtStocktakeDocVO4 extends MtStocktakeDoc {
    private static final long serialVersionUID = -7728538666222902411L;
    private String eventRequestId;

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }
}
