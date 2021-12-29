package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/9 17:13
 * @Author: ${yiyang.xie}
 */
public class MtAssembleGroupControlVO1 implements Serializable {
    private static final long serialVersionUID = 2170051070557785410L;
    @ApiModelProperty("装配组控制ID")
    private String assembleGroupControlId;
    @ApiModelProperty("装配控制ID")
    private String assembleControlId;
    @ApiModelProperty("装配组ID")
    private String assembleGroupId;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("有效性")
    private String enableFlag;

    public String getAssembleGroupControlId() {
        return assembleGroupControlId;
    }

    public void setAssembleGroupControlId(String assembleGroupControlId) {
        this.assembleGroupControlId = assembleGroupControlId;
    }

    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
    }

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
