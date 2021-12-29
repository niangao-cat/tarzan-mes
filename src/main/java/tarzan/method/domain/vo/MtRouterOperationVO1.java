package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/11/2 19:46
 * @Description:
 */
public class MtRouterOperationVO1 implements Serializable {
    private static final long serialVersionUID = -7003797368486663244L;

    @ApiModelProperty("工艺路线ID")
    private String routerId;
    @ApiModelProperty("工艺ID")
    private String operationId;

    public MtRouterOperationVO1() {}

    public MtRouterOperationVO1(String routerId, String operationId) {
        this.routerId = routerId;
        this.operationId = operationId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtRouterOperationVO1 that = (MtRouterOperationVO1) o;
        return Objects.equals(routerId, that.routerId) && Objects.equals(operationId, that.operationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routerId, operationId);
    }

}
