package tarzan.order.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/24 3:01 下午
 */
public class MtEoDTO7 implements Serializable {
    private static final long serialVersionUID = -7778785274284120429L;

    @ApiModelProperty("EOID")
    private String eoId;
    @ApiModelProperty("EO编码")
    private String eoNum;
    @ApiModelProperty("EO数量")
    private Double qty;

    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("EO关系")
    private String eoRelationStatus;

    @ApiModelProperty("EO关系描述")
    private String eoRelationStatusDesc;


    @ApiModelProperty("操作类型")
    private String reason;
    @ApiModelProperty("操作类型描述")
    private String reasonDesc;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    public String getEoRelationStatus() {
        return eoRelationStatus;
    }

    public void setEoRelationStatus(String eoRelationStatus) {
        this.eoRelationStatus = eoRelationStatus;
    }

    public String getEoRelationStatusDesc() {
        return eoRelationStatusDesc;
    }

    public void setEoRelationStatusDesc(String eoRelationStatusDesc) {
        this.eoRelationStatusDesc = eoRelationStatusDesc;
    }
}
