package tarzan.order.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/1/7 4:23 下午
 */
public class MtEoBomDTO4 implements Serializable {

    private static final long serialVersionUID = -7820929340626608436L;
    @ApiModelProperty(value = "执行作业ID", required = true)
    private String eoId;
    @ApiModelProperty(value = "组件编码")
    private String bomComponentCode;
    @ApiModelProperty(value = "组件描述")
    private String bomComponentName;
    @ApiModelProperty(value = "步骤识别码")
    private String step;
    @ApiModelProperty(value = "步骤描述")
    private String stepDesc;

    @ApiModelProperty(value = "排序方向 DESC/ASC")
    private String sortDirection;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getBomComponentCode() {
        return bomComponentCode;
    }

    public void setBomComponentCode(String bomComponentCode) {
        this.bomComponentCode = bomComponentCode;
    }

    public String getBomComponentName() {
        return bomComponentName;
    }

    public void setBomComponentName(String bomComponentName) {
        this.bomComponentName = bomComponentName;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStepDesc() {
        return stepDesc;
    }

    public void setStepDesc(String stepDesc) {
        this.stepDesc = stepDesc;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
