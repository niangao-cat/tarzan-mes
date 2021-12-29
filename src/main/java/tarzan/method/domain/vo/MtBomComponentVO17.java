package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2020/1/7 15:11
 * @Author: ${yiyang.xie}
 */
public class MtBomComponentVO17 implements Serializable {
    private static final long serialVersionUID = -8801811319422934614L;
    @ApiModelProperty("装配清单组件ID")
    private String bomComponentId;
    @ApiModelProperty("工艺ID")
    private String operationId;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
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
        MtBomComponentVO17 that = (MtBomComponentVO17) o;
        return Objects.equals(getBomComponentId(), that.getBomComponentId())
                && Objects.equals(getOperationId(), that.getOperationId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBomComponentId(), getOperationId());
    }
}
