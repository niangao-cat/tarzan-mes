package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/27 9:46
 * @Description:
 */
public class MtEoRouterActualVO44 implements Serializable {
    private static final long serialVersionUID = -5618245870996854502L;

    @ApiModelProperty("执行作业ID-用于匹配")
    private String eoId;
    @ApiModelProperty("工艺路线ID-用于匹配")
    private String routerId;
    @ApiModelProperty("来源步骤实绩-用于匹配")
    private String sourceEoStepActualId;

    public MtEoRouterActualVO44() {}

    public MtEoRouterActualVO44(String eoId, String routerId, String sourceEoStepActualId) {
        this.eoId = eoId;
        this.routerId = routerId;
        this.sourceEoStepActualId = sourceEoStepActualId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEoRouterActualVO44 that = (MtEoRouterActualVO44) o;
        return Objects.equals(eoId, that.eoId) && Objects.equals(routerId, that.routerId)
                        && Objects.equals(sourceEoStepActualId, that.sourceEoStepActualId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eoId, routerId, sourceEoStepActualId);
    }
}
