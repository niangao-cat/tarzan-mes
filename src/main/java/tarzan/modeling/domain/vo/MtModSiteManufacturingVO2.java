package tarzan.modeling.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/7/12 20:55
 * @Description:
 */
public class MtModSiteManufacturingVO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8483485010774762785L;
    private String siteManufacturingId;
    private String siteId; // 站点ID
    private String attritionCalculateStrategy; // 损耗计算策略
    private String attritionCalculateStrategyDesc;

    public String getSiteManufacturingId() {
        return siteManufacturingId;
    }

    public void setSiteManufacturingId(String siteManufacturingId) {
        this.siteManufacturingId = siteManufacturingId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getAttritionCalculateStrategy() {
        return attritionCalculateStrategy;
    }

    public void setAttritionCalculateStrategy(String attritionCalculateStrategy) {
        this.attritionCalculateStrategy = attritionCalculateStrategy;
    }

    public String getAttritionCalculateStrategyDesc() {
        return attritionCalculateStrategyDesc;
    }

    public void setAttritionCalculateStrategyDesc(String attritionCalculateStrategyDesc) {
        this.attritionCalculateStrategyDesc = attritionCalculateStrategyDesc;
    }


}
