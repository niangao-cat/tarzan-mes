package tarzan.method.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author yy
 */
public class MtAssembleGroupDTO implements Serializable {
    private static final long serialVersionUID = 3112824176875498815L;

    @ApiModelProperty("主键ID，表示唯一一条记录")
    private String assembleGroupId;
    @ApiModelProperty(value = "生产站点")
    private String siteId;
    @ApiModelProperty(value = "装配组代码")
    private String assembleGroupCode;
    @ApiModelProperty(value = "装配组描述")
    private String description;
    @ApiModelProperty(value = "每次上料自动安装装配点，如果启用则ASSEMBLE_CONTROL_FLAG和ASSEMBLE_SEQUENCE_FLAG无效")
    private String autoInstallPointFlag;
    @ApiModelProperty(value = "装配限制标识，如果启用则ASSEMBLE_SEQUENCE_FLAG无效，如果装配控制明细为空，则不校验")
    private String assembleControlFlag;
    @ApiModelProperty(value = "如果为是，则严格按照装配点在装配组的顺序装载物料")
    private String assembleSequenceFlag;
    @ApiModelProperty(value = "状态，包括新建NEW、下达RELEASED、运行WORKING、保留HOLD、关闭CLOSED")
    private String assembleGroupStatus;

    /**
     * @return 主键ID，表示唯一一条记录
     */
    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    /**
     * @return 生产站点
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 装配组代码
     */
    public String getAssembleGroupCode() {
        return assembleGroupCode;
    }

    public void setAssembleGroupCode(String assembleGroupCode) {
        this.assembleGroupCode = assembleGroupCode;
    }

    /**
     * @return 装配组描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 每次上料自动安装装配点，如果启用则ASSEMBLE_CONTROL_FLAG和ASSEMBLE_SEQUENCE_FLAG无效
     */
    public String getAutoInstallPointFlag() {
        return autoInstallPointFlag;
    }

    public void setAutoInstallPointFlag(String autoInstallPointFlag) {
        this.autoInstallPointFlag = autoInstallPointFlag;
    }

    /**
     * @return 装配限制标识，如果启用则ASSEMBLE_SEQUENCE_FLAG无效，如果装配控制明细为空，则不校验
     */
    public String getAssembleControlFlag() {
        return assembleControlFlag;
    }

    public void setAssembleControlFlag(String assembleControlFlag) {
        this.assembleControlFlag = assembleControlFlag;
    }

    /**
     * @return 如果为是，则严格按照装配点在装配组的顺序装载物料
     */
    public String getAssembleSequenceFlag() {
        return assembleSequenceFlag;
    }

    public void setAssembleSequenceFlag(String assembleSequenceFlag) {
        this.assembleSequenceFlag = assembleSequenceFlag;
    }

    /**
     * @return 状态，包括新建NEW、下达RELEASED、运行WORKING、保留HOLD、关闭CLOSED
     */
    public String getAssembleGroupStatus() {
        return assembleGroupStatus;
    }

    public void setAssembleGroupStatus(String assembleGroupStatus) {
        this.assembleGroupStatus = assembleGroupStatus;
    }
}
