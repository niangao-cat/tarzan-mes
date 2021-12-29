package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/11/3 20:13
 */
public class MtEoStepWipVO21 implements Serializable {
    private static final long serialVersionUID = 4884390410708690197L;
    @ApiModelProperty(value = "执行作业工艺路线实绩唯一标识")
    private String eoStepActualId;

    @ApiModelProperty(value = "wkc")
    private String workcellId;

    public MtEoStepWipVO21(String eoStepActualId, String workcellId) {
        this.eoStepActualId = eoStepActualId;
        this.workcellId = workcellId;
    }

    public MtEoStepWipVO21() {}

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEoStepWipVO21 that = (MtEoStepWipVO21) o;
        return Objects.equals(getEoStepActualId(), that.getEoStepActualId())
                        && Objects.equals(getWorkcellId(), that.getWorkcellId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEoStepActualId(), getWorkcellId());
    }
}
