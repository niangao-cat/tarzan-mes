package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/11/3 19:59
 * @Description:
 */
public class MtEoStepWipVO10 implements Serializable {

    private static final long serialVersionUID = 707880422367812894L;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("执行作业步骤ID")
    private String eoStepActualId;

    public MtEoStepWipVO10() {}

    public MtEoStepWipVO10(String eoStepActualId, String workcellId) {
        this.workcellId = workcellId;
        this.eoStepActualId = eoStepActualId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEoStepWipVO10 that = (MtEoStepWipVO10) o;
        return Objects.equals(workcellId, that.workcellId) && Objects.equals(eoStepActualId, that.eoStepActualId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workcellId, eoStepActualId);
    }
}
