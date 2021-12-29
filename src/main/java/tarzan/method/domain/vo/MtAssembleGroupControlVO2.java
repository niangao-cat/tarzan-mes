package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/9 17:15
 * @Author: ${yiyang.xie}
 */
public class MtAssembleGroupControlVO2 implements Serializable {
    private static final long serialVersionUID = -869302251977264556L;
    @ApiModelProperty("装配组控制ID")
    private String assembleGroupControlId;
    @ApiModelProperty("装配控制ID")
    private String assembleControlId;
    @ApiModelProperty("装配组ID")
    private String assembleGroupId;
    @ApiModelProperty("装配组描述")
    private String assembleGroupDescription;
    @ApiModelProperty("装配组编码")
    private String assembleGroupCode;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("工作单元短描述")
    private String workcellName;
    @ApiModelProperty("工作单元编码")
    private String workcellCode;
    @ApiModelProperty("工作单元长描述")
    private String workcellDescription;
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

    public String getAssembleGroupDescription() {
        return assembleGroupDescription;
    }

    public void setAssembleGroupDescription(String assembleGroupDescription) {
        this.assembleGroupDescription = assembleGroupDescription;
    }

    public String getAssembleGroupCode() {
        return assembleGroupCode;
    }

    public void setAssembleGroupCode(String assembleGroupCode) {
        this.assembleGroupCode = assembleGroupCode;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getWorkcellDescription() {
        return workcellDescription;
    }

    public void setWorkcellDescription(String workcellDescription) {
        this.workcellDescription = workcellDescription;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
