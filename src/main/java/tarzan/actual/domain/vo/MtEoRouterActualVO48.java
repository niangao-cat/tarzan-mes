package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/11/3 09:45
 */
public class MtEoRouterActualVO48 implements Serializable {
    private static final long serialVersionUID = 5078599130203772314L;
    @ApiModelProperty("执行作业")
    private String eoId;

    @ApiModelProperty("前道步骤")
    private String previousStepId;

    @ApiModelProperty("来源步骤")
    private String sourceEoStepActualId;

    @ApiModelProperty("步骤ID")
    private String routerStepId;

    @ApiModelProperty("工作单元")
    private String workcellId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getPreviousStepId() {
        return previousStepId;
    }

    public void setPreviousStepId(String previousStepId) {
        this.previousStepId = previousStepId;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
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

    public MtEoRouterActualVO48(String eoId, String previousStepId, String sourceEoStepActualId, String routerStepId,
                                String workcellId) {
        this.eoId = eoId;
        this.previousStepId = previousStepId;
        this.sourceEoStepActualId = sourceEoStepActualId;
        this.routerStepId = routerStepId;
        this.workcellId = workcellId;
    }

    public MtEoRouterActualVO48() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEoRouterActualVO48 that = (MtEoRouterActualVO48) o;
        return Objects.equals(eoId, that.eoId) && Objects.equals(previousStepId, that.previousStepId)
                        && Objects.equals(sourceEoStepActualId, that.sourceEoStepActualId)
                        && Objects.equals(routerStepId, that.routerStepId)
                        && Objects.equals(workcellId, that.workcellId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eoId, previousStepId, sourceEoStepActualId, routerStepId, workcellId);
    }
}
