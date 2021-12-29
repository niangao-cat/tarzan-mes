package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/11/7 19:41
 * @Author: ${yiyang.xie}
 */
public class MtRouterStepVO14 implements Serializable {
    private static final long serialVersionUID = 1849562352279144034L;
    @ApiModelProperty("工艺路线步骤ID")
    private String routerStepId;
    @ApiModelProperty("工艺路线步骤类型")
    private String routerStepType;
    @ApiModelProperty("工艺路线步骤组内步骤")
    private List<MtRouterStepVO14> routerGroupStepId;
    @ApiModelProperty("嵌套工艺路线内步骤")
    private List<MtRouterStepVO14> routerLinkStepId;
    @ApiModelProperty("未完工数量")
    private Double unCompletedQty;


    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getRouterStepType() {
        return routerStepType;
    }

    public void setRouterStepType(String routerStepType) {
        this.routerStepType = routerStepType;
    }

    public List<MtRouterStepVO14> getRouterGroupStepId() {
        return routerGroupStepId;
    }

    public void setRouterGroupStepId(List<MtRouterStepVO14> routerGroupStepId) {
        this.routerGroupStepId = routerGroupStepId;
    }

    public List<MtRouterStepVO14> getRouterLinkStepId() {
        return routerLinkStepId;
    }

    public void setRouterLinkStepId(List<MtRouterStepVO14> routerLinkStepId) {
        this.routerLinkStepId = routerLinkStepId;
    }

    public Double getUnCompletedQty() {
        return unCompletedQty;
    }

    public void setUnCompletedQty(Double unCompletedQty) {
        this.unCompletedQty = unCompletedQty;
    }
}
