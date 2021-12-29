package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/14 18:56
 * @Author: ${yiyang.xie}
 */
public class MtInstructionActualDetailVO implements Serializable {
    private static final long serialVersionUID = -5643197976791520798L;
    @ApiModelProperty("指令单据ID")
    private String instructionDocId;
    @ApiModelProperty("指令ID")
    private String instructionId;
    @ApiModelProperty("实绩id")
    private String actualDetailId;
    @ApiModelProperty(value = "汇总id", required = true)
    private String actualId;
    @ApiModelProperty(value = "物料批ID", required = true)
    private String materialLotId;
    @ApiModelProperty(value = "单位", required = true)
    private String uomId;
    @ApiModelProperty(value = "实绩数量", required = true)
    private Double actualQty;
    @ApiModelProperty(value = "器具id")
    private String containerId;
    @ApiModelProperty(value = "指令实际明细创建时间")
    private Date creationDate;

    public String getInstructionDocId() {
        return instructionDocId;
    }

    public void setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getActualDetailId() {
        return actualDetailId;
    }

    public void setActualDetailId(String actualDetailId) {
        this.actualDetailId = actualDetailId;
    }

    public String getActualId() {
        return actualId;
    }

    public void setActualId(String actualId) {
        this.actualId = actualId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public Double getActualQty() {
        return actualQty;
    }

    public void setActualQty(Double actualQty) {
        this.actualQty = actualQty;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
