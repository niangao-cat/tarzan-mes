package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-12 19:34
 */
public class MtEoRouterActualVO36 implements Serializable {
    private static final long serialVersionUID = 3490665869250946833L;

    @ApiModelProperty("执行作业工艺路线实绩唯一标识")
    private String eoStepActualId;

    @ApiModelProperty("工作单元唯一标识")
    private String workcellId;

    @ApiModelProperty("完成更新数量")
    private Double qty;

    @ApiModelProperty("事件组ID")
    private String eventRequestId;

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

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }
}
