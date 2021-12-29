package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/12/19 16:17
 * @Author: ${yiyang.xie}
 */
public class MtWorkOrderVO45 implements Serializable {
    private static final long serialVersionUID = 195576869882923982L;

    @ApiModelProperty("生产指令ID，唯一标识")
    private String workOrderId;
    @ApiModelProperty("只限定为投料装配方法")
    private String onlyIssueAssembleFlag;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getOnlyIssueAssembleFlag() {
        return onlyIssueAssembleFlag;
    }

    public void setOnlyIssueAssembleFlag(String onlyIssueAssembleFlag) {
        this.onlyIssueAssembleFlag = onlyIssueAssembleFlag;
    }
}
