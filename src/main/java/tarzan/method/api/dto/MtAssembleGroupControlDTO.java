package tarzan.method.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author yuan.yuan@hand-china.com
 */
public class MtAssembleGroupControlDTO implements Serializable {
    private static final long serialVersionUID = -8126650378375339985L;

    private String assembleGroupControlId;
    @ApiModelProperty(value = "装配控制ID")
    private String assembleControlId;
    @ApiModelProperty(value = "装配组ID")
    private String assembleGroupId;
    @ApiModelProperty(value = "WKC")
    private String workcellId;
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;


    /**
     * @return 主键ID,表示唯一一条记录
     */
    public String getAssembleGroupControlId() {
        return assembleGroupControlId;
    }

    public void setAssembleGroupControlId(String assembleGroupControlId) {
        this.assembleGroupControlId = assembleGroupControlId;
    }

    /**
     * @return 装配控制ID
     */
    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
    }

    /**
     * @return 装配组ID
     */
    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    /**
     * @return WKC
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
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
