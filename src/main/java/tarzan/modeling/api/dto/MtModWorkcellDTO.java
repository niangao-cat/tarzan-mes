package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModWorkcellDTO implements Serializable {
    private static final long serialVersionUID = -1476069345726722709L;

    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String workcellId;
    @ApiModelProperty(value = "工作单元编号", required = true)
    private String workcellCode;
    @ApiModelProperty(value = "工作单元名称", required = true)
    private String workcellName;
    @ApiModelProperty(value = "工作单元类型")
    private String workcellType;
    @ApiModelProperty(value = "是否有效", required = true)
    private String enableFlag;


    /**
     * @return 主键ID，标识唯一一条记录
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 工作单元编号
     */
    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    /**
     * @return 工作单元名称
     */
    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    /**
     * @return 工作单元类型
     */
    public String getWorkcellType() {
        return workcellType;
    }

    public void setWorkcellType(String workcellType) {
        this.workcellType = workcellType;
    }


    /**
     * @return 是否有效
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
