package tarzan.stocktake.domain.vo;

import java.util.List;

import tarzan.stocktake.domain.entity.MtStocktakeDoc;

/**
 * @author Leeloing
 * @date 2019/5/16 9:22
 */
public class MtStocktakeDocVO extends MtStocktakeDoc {

    private static final long serialVersionUID = -2592624483303247904L;

    private List<String> stocktakeStatusList;
    private List<String> stocktakeLastStatusList;

    public List<String> getStocktakeStatusList() {
        return stocktakeStatusList;
    }

    public void setStocktakeStatusList(List<String> stocktakeStatusList) {
        this.stocktakeStatusList = stocktakeStatusList;
    }

    public List<String> getStocktakeLastStatusList() {
        return stocktakeLastStatusList;
    }

    public void setStocktakeLastStatusList(List<String> stocktakeLastStatusList) {
        this.stocktakeLastStatusList = stocktakeLastStatusList;
    }
}
