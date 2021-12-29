package tarzan.method.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtNcSecondaryCodeDTO implements Serializable {
    private static final long serialVersionUID = 5017192765259354415L;

    @ApiModelProperty("顺序")
    private Long sequence;
    @ApiModelProperty("次级不良代码Id")
    private String ncSecondaryCodeId;
    @ApiModelProperty("不良代码Id")
    private String ncCodeId;
    @ApiModelProperty("次级不良代码")
    private String secondaryNcCode;
    @ApiModelProperty("次级不良代码描述")
    private String secondaryNcDesc;
    @ApiModelProperty("关闭是否需要")
    private String requiredFlag;

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getNcSecondaryCodeId() {
        return ncSecondaryCodeId;
    }

    public void setNcSecondaryCodeId(String ncSecondaryCodeId) {
        this.ncSecondaryCodeId = ncSecondaryCodeId;
    }

    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }

    public String getSecondaryNcCode() {
        return secondaryNcCode;
    }

    public void setSecondaryNcCode(String secondaryNcCode) {
        this.secondaryNcCode = secondaryNcCode;
    }

    public String getSecondaryNcDesc() {
        return secondaryNcDesc;
    }

    public void setSecondaryNcDesc(String secondaryNcDesc) {
        this.secondaryNcDesc = secondaryNcDesc;
    }

    public String getRequiredFlag() {
        return requiredFlag;
    }

    public void setRequiredFlag(String requiredFlag) {
        this.requiredFlag = requiredFlag;
    }
}
