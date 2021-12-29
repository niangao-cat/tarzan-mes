package tarzan.dispatch.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/6/26 16:09
 * @Description:
 */
public class MtOpWkcDispatchRelVO7 implements Serializable {
    private static final long serialVersionUID = -6598762615624407282L;

    private String workcellId; // 工作单元
    private String productionLineId; // 生产线
    private String siteId; // 站点

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
