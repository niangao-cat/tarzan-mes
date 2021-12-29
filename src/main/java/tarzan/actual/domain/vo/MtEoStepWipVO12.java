package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MtEoStepWipVO12 implements Serializable {


    private static final long serialVersionUID = 2349391676574967676L;
    @ApiModelProperty(value = "eo步骤实绩ID")
    private String eoStepActualId;

    @ApiModelProperty(value = "工作单元ID")
    private String workcellId;

    @ApiModelProperty(value = "执行作业在制品ID")
    private String eoStepWipId;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getEoStepWipId() {
        return eoStepWipId;
    }

    public void setEoStepWipId(String eoStepWipId) {
        this.eoStepWipId = eoStepWipId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public MtEoStepWipVO12() {
    }

    public MtEoStepWipVO12(String eoStepActualId, String workcellId, String eoStepWipId) {
        this.eoStepActualId = eoStepActualId;
        this.workcellId = workcellId;
        this.eoStepWipId = eoStepWipId;
    }
}


