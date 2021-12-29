package tarzan.order.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2020/5/8 11:08
 * @Description:
 */
public class MtWorkOrderVO58 implements Serializable {
    private static final long serialVersionUID = -6364060944380880403L;
    @ApiModelProperty("生产指令类型")
    private String workOrderType;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("制造订单编码")
    private String makeOrderNum;
    @ApiModelProperty("生产线ID")
    private String productionLineId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("装配清单ID")
    private String bomId;
    @ApiModelProperty("工艺路线ID")
    private String routerId;

    public String getWorkOrderType() {
        return workOrderType;
    }

    public void setWorkOrderType(String workOrderType) {
        this.workOrderType = workOrderType;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getMakeOrderNum() {
        return makeOrderNum;
    }

    public void setMakeOrderNum(String makeOrderNum) {
        this.makeOrderNum = makeOrderNum;
    }

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }
}
