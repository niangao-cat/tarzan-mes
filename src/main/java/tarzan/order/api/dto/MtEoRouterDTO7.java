package tarzan.order.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/1/7 8:09 下午
 */
public class MtEoRouterDTO7 implements Serializable {
    private static final long serialVersionUID = 2584666686198256461L;
    @ApiModelProperty(value = "执行作业ID", required = true)
    private String eoId;
    @ApiModelProperty(value = "步骤识别码")
    private String stepName;
    @ApiModelProperty(value = "排序方向 DESC/ASC")
    private String sortDirection;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }


}
