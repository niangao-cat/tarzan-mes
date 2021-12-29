package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/1/6 12:40 下午
 */
public class MtEoVO42 implements Serializable {
    private static final long serialVersionUID = 40531388982925215L;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("只限定为投料装配方法")
    private String onlyIssueAssembleFlag;
    @ApiModelProperty("工艺路线步骤ID")
    private String routerStepId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getOnlyIssueAssembleFlag() {
        return onlyIssueAssembleFlag;
    }

    public void setOnlyIssueAssembleFlag(String onlyIssueAssembleFlag) {
        this.onlyIssueAssembleFlag = onlyIssueAssembleFlag;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }
}
