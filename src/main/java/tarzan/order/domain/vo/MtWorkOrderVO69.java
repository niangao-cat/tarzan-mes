package tarzan.order.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: chuang.yang
 * @Date: 2020/11/3 17:31
 * @Description:
 */
public class MtWorkOrderVO69 implements Serializable {
    private static final long serialVersionUID = -7505104730758622485L;

    @ApiModelProperty("组件ID")
    private String bomComponentId;
    @ApiModelProperty("组件行号")
    private Long lineNumber;

    public MtWorkOrderVO69() {}

    public MtWorkOrderVO69(String bomComponentId, Long lineNumber) {
        this.bomComponentId = bomComponentId;
        this.lineNumber = lineNumber;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtWorkOrderVO69 that = (MtWorkOrderVO69) o;
        return Objects.equals(bomComponentId, that.bomComponentId) && Objects.equals(lineNumber, that.lineNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bomComponentId, lineNumber);
    }
}
