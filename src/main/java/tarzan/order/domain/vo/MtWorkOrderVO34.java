package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtWorkOrderVO34
 * @description
 * @date 2019年11月21日 10:06
 */
public class MtWorkOrderVO34 implements Serializable {
    private static final long serialVersionUID = -3394933939145494759L;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    @ApiModelProperty(value = "生产线编码")
    private String productionLineCode;
    @ApiModelProperty(value = "订单编码")
    private String makeOrderNum;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "订单类型")
    private String workOrderType;
    @ApiModelProperty(value = "物料名称")
    private String bomName;
    @ApiModelProperty(value = "路由名称")
    private String routerName;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getProductionLineCode() {
        return productionLineCode;
    }

    public void setProductionLineCode(String productionLineCode) {
        this.productionLineCode = productionLineCode;
    }

    public String getMakeOrderNum() {
        return makeOrderNum;
    }

    public void setMakeOrderNum(String makeOrderNum) {
        this.makeOrderNum = makeOrderNum;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getWorkOrderType() {
        return workOrderType;
    }

    public void setWorkOrderType(String workOrderType) {
        this.workOrderType = workOrderType;
    }

    public String getBomName() {
        return bomName;
    }

    public void setBomName(String bomName) {
        this.bomName = bomName;
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }
}
