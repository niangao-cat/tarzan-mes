package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/11/3 11:36
 */
public class MtEoStepActualVO63 implements Serializable {
    private static final long serialVersionUID = 9184452975893474118L;
    @ApiModelProperty("工艺路线实绩唯一标识")
    private String eoRouterActualId;

    @ApiModelProperty("步骤唯一标识")
    private String routerStepId;

    @ApiModelProperty("工作单元")
    private String workcellId;

    @ApiModelProperty("目标步骤ID")
    private String targetRouterStepId;

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getTargetRouterStepId() {
        return targetRouterStepId;
    }

    public void setTargetRouterStepId(String targetRouterStepId) {
        this.targetRouterStepId = targetRouterStepId;
    }

    public MtEoStepActualVO63(String eoRouterActualId, String routerStepId, String workcellId, String targetRouterStepId) {
        this.eoRouterActualId = eoRouterActualId;
        this.routerStepId = routerStepId;
        this.workcellId = workcellId;
        this.targetRouterStepId = targetRouterStepId;
    }

    public MtEoStepActualVO63() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEoStepActualVO63 that = (MtEoStepActualVO63) o;
        return Objects.equals(eoRouterActualId, that.eoRouterActualId)
                        && Objects.equals(routerStepId, that.routerStepId)
                        && Objects.equals(workcellId, that.workcellId)
                        && Objects.equals(targetRouterStepId, that.targetRouterStepId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eoRouterActualId, routerStepId, workcellId, targetRouterStepId);
    }
}
