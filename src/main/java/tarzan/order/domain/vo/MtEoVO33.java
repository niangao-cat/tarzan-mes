package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/11/21 10:31
 * @Author: ${yiyang.xie}
 */
public class MtEoVO33 implements Serializable {
    private static final long serialVersionUID = 5589698970374410952L;

    @ApiModelProperty("工单编码")
    private String workOrderNum;
    @ApiModelProperty("站点编码")
    private String siteCode;
    @ApiModelProperty("生产线编码")
    private String productionLineCode;
    @ApiModelProperty("生产订单编码")
    private String makeOrderNum;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("工作单元编码")
    private String workcellCode;
    @ApiModelProperty("eo类型")
    private String eoType;
    @ApiModelProperty("装配编码")
    private String bomName;
    @ApiModelProperty("工艺路线编码")
    private String routerName;

    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

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

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getEoType() {
        return eoType;
    }

    public void setEoType(String eoType) {
        this.eoType = eoType;
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
