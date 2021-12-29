package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/11/2 22:01
 */
public class MtEoStepActualVO62 implements Serializable {
    private static final long serialVersionUID = 1088552523892978117L;
    @ApiModelProperty("步骤ID")
    private String routerStepId;

    @ApiModelProperty("步骤组")
    private String groupRouterStepId;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getGroupRouterStepId() {
        return groupRouterStepId;
    }

    public void setGroupRouterStepId(String groupRouterStepId) {
        this.groupRouterStepId = groupRouterStepId;
    }

    public MtEoStepActualVO62(String routerStepId, String groupRouterStepId) {
        this.routerStepId = routerStepId;
        this.groupRouterStepId = groupRouterStepId;
    }

    public MtEoStepActualVO62() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEoStepActualVO62 that = (MtEoStepActualVO62) o;
        return Objects.equals(getRouterStepId(), that.getRouterStepId())
                        && Objects.equals(getGroupRouterStepId(), that.getGroupRouterStepId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRouterStepId(), getGroupRouterStepId());
    }
}
