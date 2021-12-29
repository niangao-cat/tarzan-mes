package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


public class MtEoStepActualVO49 implements Serializable {
    private static final long serialVersionUID = -5957498736415283085L;
    @ApiModelProperty("执行作业步骤实绩唯一标识")
    private String eoStepActualId;
    @ApiModelProperty("工作单元Id")
    private String workcellId;
    @ApiModelProperty("执行作业在制品ID")
    private String eoStepWipId;
    @ApiModelProperty("执行作业步骤实绩ID")
    private String eoStepWorkcellActualId;
    @ApiModelProperty("执行作业步骤实绩历史ID")
    private String eoStepWorkcellActualHisId;

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

    public String getEoStepWipId() {
        return eoStepWipId;
    }

    public void setEoStepWipId(String eoStepWipId) {
        this.eoStepWipId = eoStepWipId;
    }

    public String getEoStepWorkcellActualId() {
        return eoStepWorkcellActualId;
    }

    public void setEoStepWorkcellActualId(String eoStepWorkcellActualId) {
        this.eoStepWorkcellActualId = eoStepWorkcellActualId;
    }

    public String getEoStepWorkcellActualHisId() {
        return eoStepWorkcellActualHisId;
    }

    public void setEoStepWorkcellActualHisId(String eoStepWorkcellActualHisId) {
        this.eoStepWorkcellActualHisId = eoStepWorkcellActualHisId;
    }

}
