package tarzan.modeling.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/7/12 20:55
 * @Description:
 */
public class MtModSiteManufacturingVO implements Serializable {
    private static final long serialVersionUID = 164187357315189263L;

    private String siteId; // 站点ID
    private String attritionCalculateStrategy; // 损耗计算策略

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
}
