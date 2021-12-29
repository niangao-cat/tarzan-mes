package tarzan.stocktake.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/5/22 13:55
 */
public class MtStocktakeDocVO3 implements Serializable {
    private static final long serialVersionUID = 7457961869055088200L;

    private String stocktakeId;
    private String targetStocktakeStatus;

    public String getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(String stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    public String getTargetStocktakeStatus() {
        return targetStocktakeStatus;
    }

    public void setTargetStocktakeStatus(String targetStocktakeStatus) {
        this.targetStocktakeStatus = targetStocktakeStatus;
    }


}
