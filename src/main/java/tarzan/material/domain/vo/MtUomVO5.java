package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtUomVO5 implements Serializable {

    private static final long serialVersionUID = -680985939650766499L;
    @ApiModelProperty("单位ID")
    private String uomId;
    @ApiModelProperty("单位类型编码")
    private String uomType;
    @ApiModelProperty("单位类型描述")
    private String uomTypeDesc;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("单位名称")
    private String uomName;
    @ApiModelProperty("主单位换算关系")
    private String conversionValue;
    @ApiModelProperty("主单位标识")
    private String primaryFlag;
    @ApiModelProperty("小数位数")
    private Long decimalNumber;
    @ApiModelProperty("尾数处理方式")
    private String processMode;
    @ApiModelProperty("尾数处理方式描述")
    private String processModeDesc;
    @ApiModelProperty("是否有效")
    private String enableFlag;

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getUomType() {
        return uomType;
    }

    public void setUomType(String uomType) {
        this.uomType = uomType;
    }

    public String getUomTypeDesc() {
        return uomTypeDesc;
    }

    public void setUomTypeDesc(String uomTypeDesc) {
        this.uomTypeDesc = uomTypeDesc;
    }

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getConversionValue() {
        return conversionValue;
    }

    public void setConversionValue(String conversionValue) {
        this.conversionValue = conversionValue;
    }

    public String getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(String primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    public Long getDecimalNumber() {
        return decimalNumber;
    }

    public void setDecimalNumber(Long decimalNumber) {
        this.decimalNumber = decimalNumber;
    }

    public String getProcessMode() {
        return processMode;
    }

    public void setProcessMode(String processMode) {
        this.processMode = processMode;
    }

    public String getProcessModeDesc() {
        return processModeDesc;
    }

    public void setProcessModeDesc(String processModeDesc) {
        this.processModeDesc = processModeDesc;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
