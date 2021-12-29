package tarzan.modeling.api.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModWorkcellDTO2 implements Serializable {
    private static final long serialVersionUID = -1476069345726722709L;

    @ApiModelProperty(value = "工作单元编码")
    private String workcellCode;

    @ApiModelProperty(value = "工作单元短描述")
    private String workcellName;

    @ApiModelProperty(value = "工作单元长描述")
    private String workcellDesc;

    @ApiModelProperty(value = "启用状态")
    private String enableFlag;

    @ApiModelProperty(value = "工作单元类型")
    private String workcellType;

    @ApiModelProperty(value = "工作单元位置")
    private String workcellLocation;

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    public String getWorkcellDesc() {
        return workcellDesc;
    }

    public void setWorkcellDesc(String workcellDesc) {
        this.workcellDesc = workcellDesc;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getWorkcellType() {
        return workcellType;
    }

    public void setWorkcellType(String workcellType) {
        this.workcellType = workcellType;
    }

    public String getWorkcellLocation() {
        return workcellLocation;
    }

    public void setWorkcellLocation(String workcellLocation) {
        this.workcellLocation = workcellLocation;
    }

}
