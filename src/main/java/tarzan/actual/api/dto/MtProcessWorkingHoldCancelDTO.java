package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-12 19:08
 */
public class MtProcessWorkingHoldCancelDTO implements Serializable {

    private static final long serialVersionUID = 5103864765305486658L;
    @ApiModelProperty("EO步骤实绩ID")
    private String eoStepActualId;

    @ApiModelProperty("工作单元ID")
    private String workcellId;

    @ApiModelProperty("数量")
    private Double qty;

    @ApiModelProperty("执行作业ID")
    private String eoId;

    @ApiModelProperty("来源状态")
    private String sourceStatus;

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

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }
}
