package tarzan.stocktake.api.dto;

import java.io.Serializable;

public class MtStocktakeRangeDTO implements Serializable {
    private static final long serialVersionUID = -6136865894293456641L;

    private String stocktakeRangeId;
    private String stocktakeId;
    private String rangeObjectType;
    private String rangeObjectId;

    public String getStocktakeRangeId() {
        return stocktakeRangeId;
    }

    public void setStocktakeRangeId(String stocktakeRangeId) {
        this.stocktakeRangeId = stocktakeRangeId;
    }

    public String getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(String stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    public String getRangeObjectType() {
        return rangeObjectType;
    }

    public void setRangeObjectType(String rangeObjectType) {
        this.rangeObjectType = rangeObjectType;
    }

    public String getRangeObjectId() {
        return rangeObjectId;
    }

    public void setRangeObjectId(String rangeObjectId) {
        this.rangeObjectId = rangeObjectId;
    }
}
