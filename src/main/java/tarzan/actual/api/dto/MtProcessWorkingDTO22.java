package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 0018, 2020-02-18 14:28
 */
public class MtProcessWorkingDTO22 implements Serializable {

    private static final long serialVersionUID = 7157462480846450152L;
    @ApiModelProperty("执行作业ID-卡片")
    private String eoId;
    @ApiModelProperty("工艺路线步骤ID-卡片")
    private String 	routerStepId;

    @ApiModelProperty("物料ID-选择行")
    private String materialId;
    @ApiModelProperty("组件ID-选择行")
    private String bomComponentId;
    @ApiModelProperty("组件类型-选择行")
    private String bomComponentType;
    @ApiModelProperty("装配方式-选择行")
    private String assembleMethod;
    @ApiModelProperty("强制装配标识-选择行")
    private String assembleExcessFlag;

    @ApiModelProperty("物料批编码-输入")
    private String materialLotCode;
    @ApiModelProperty("装配数量-输入")
    private Double trxAssembleQty;

    @ApiModelProperty("站点ID-登入信息")
    private String	siteId;
    @ApiModelProperty("工艺ID-登入信息")
    private String	operationId;
    @ApiModelProperty("工作单元ID-登入信息")
    private String	workcellId;
    @ApiModelProperty("班次日期-登入信息")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    @ApiModelProperty("班次编码-登入信息")
    private String shiftCode;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomComponentType() {
        return bomComponentType;
    }

    public void setBomComponentType(String bomComponentType) {
        this.bomComponentType = bomComponentType;
    }

    public String getAssembleMethod() {
        return assembleMethod;
    }

    public void setAssembleMethod(String assembleMethod) {
        this.assembleMethod = assembleMethod;
    }

    public String getAssembleExcessFlag() {
        return assembleExcessFlag;
    }

    public void setAssembleExcessFlag(String assembleExcessFlag) {
        this.assembleExcessFlag = assembleExcessFlag;
    }

    public String getMaterialLotCode() {
        return materialLotCode;
    }

    public void setMaterialLotCode(String materialLotCode) {
        this.materialLotCode = materialLotCode;
    }

    public Double getTrxAssembleQty() {
        return trxAssembleQty;
    }

    public void setTrxAssembleQty(Double trxAssembleQty) {
        this.trxAssembleQty = trxAssembleQty;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Date getShiftDate() {
        if (shiftDate != null) {
            return (Date) shiftDate.clone();
        } else {
            return null;
        }
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }
}
