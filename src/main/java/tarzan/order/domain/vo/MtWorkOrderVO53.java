package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月20日 下午6:38:45
 *
 */
public class MtWorkOrderVO53 implements Serializable {
    
    private static final long serialVersionUID = 8672507281571662286L;
    
    @ApiModelProperty(value = "生产指令ID")
    private String workOrderId;
    @ApiModelProperty(value = "工单编码")
    private String workOrderNum;
    @ApiModelProperty(value = "物料编码")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "WO类型")
    private String workOrderType;
    @ApiModelProperty(value = "WO类型描述")
    private String workOrderTypeDesc;
    @ApiModelProperty(value = "WO状态")
    private String status;
    @ApiModelProperty(value = "WO状态描述")
    private String statusDesc;
    @ApiModelProperty(value = "数量")
    private Double qty;
    @ApiModelProperty(value = "工单关系")
    private String rel;
    @ApiModelProperty(value = "工单关系描述")
    private String relDesc;
    @ApiModelProperty(value = "工单关系类型")
    private String relType;
    @ApiModelProperty(value = "工单关系类型描述")
    private String relTypeDesc;
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    public String getWorkOrderNum() {
        return workOrderNum;
    }
    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
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
    public String getWorkOrderType() {
        return workOrderType;
    }
    public void setWorkOrderType(String workOrderType) {
        this.workOrderType = workOrderType;
    }
    public String getWorkOrderTypeDesc() {
        return workOrderTypeDesc;
    }
    public void setWorkOrderTypeDesc(String workOrderTypeDesc) {
        this.workOrderTypeDesc = workOrderTypeDesc;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatusDesc() {
        return statusDesc;
    }
    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
    public Double getQty() {
        return qty;
    }
    public void setQty(Double qty) {
        this.qty = qty;
    }
    public String getRel() {
        return rel;
    }
    public void setRel(String rel) {
        this.rel = rel;
    }
    public String getRelDesc() {
        return relDesc;
    }
    public void setRelDesc(String relDesc) {
        this.relDesc = relDesc;
    }
    public String getRelType() {
        return relType;
    }
    public void setRelType(String relType) {
        this.relType = relType;
    }
    public String getRelTypeDesc() {
        return relTypeDesc;
    }
    public void setRelTypeDesc(String relTypeDesc) {
        this.relTypeDesc = relTypeDesc;
    }
    
}
