package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-12 19:08
 */
public class MtProcessWorkingBackStepDTO implements Serializable {

    private static final long serialVersionUID = 310070049922569228L;

    @ApiModelProperty("执行作业ID-卡片")
    private String eoId;

    @ApiModelProperty("EO步骤实绩ID")
    private String eoStepActualId;

    @ApiModelProperty("工作单元ID")
    private String workcellId;

    @ApiModelProperty("数量")
    private Double qty;

    @ApiModelProperty("当前状态")
    private String sourceStatus;

    @ApiModelProperty("上一状态")
    private String previousStatus;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }
}
