package tarzan.inventory.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/4/18 14:44
 * @Description:
 */
public class MtInvOnhandQuantityVO2 implements Serializable {
    private static final long serialVersionUID = 389514098240206521L;

    private Double actualChangeQty; // 实际需变更数量
    private String actualChangeLocatorId; // 实际需变更库存库位

    public Double getActualChangeQty() {
        return actualChangeQty;
    }

    public void setActualChangeQty(Double actualChangeQty) {
        this.actualChangeQty = actualChangeQty;
    }

    public String getActualChangeLocatorId() {
        return actualChangeLocatorId;
    }

    public void setActualChangeLocatorId(String actualChangeLocatorId) {
        this.actualChangeLocatorId = actualChangeLocatorId;
    }
}
