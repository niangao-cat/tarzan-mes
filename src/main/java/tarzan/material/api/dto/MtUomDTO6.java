package tarzan.material.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtUomDTO6 implements Serializable {
    private static final long serialVersionUID = 762189668547567892L;

    @ApiModelProperty("主键ID,标识唯一一条记录")
    private String uomId;
    @ApiModelProperty(value = "单位类型(修改时必输)")
    @NotBlank
    private String uomType;
    @ApiModelProperty(value = "单位编码(修改时必输)")
    @NotBlank
    private String uomCode;
    @ApiModelProperty(value = "单位描述(修改时必输)")
    @NotBlank
    private String uomName;
    @ApiModelProperty(value = "同类别主单位标识")
    private String primaryFlag;
    @ApiModelProperty(value = "与主单位换算关系，主单位/单位(修改时必输)")
    @NotNull
    private Double conversionValue;
    @ApiModelProperty(value = "小数位数(修改时必输)")
    @NotNull
    private Long decimalNumber;
    @ApiModelProperty(value = "尾数处理方式，包括进一法、四舍五入、去尾法(修改时必输)")
    @NotBlank
    private String processMode;
    @ApiModelProperty(value = "是否有效(修改时必输)")
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls = Collections.emptyMap();

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

    public String getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(String primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    public Double getConversionValue() {
        return conversionValue;
    }

    public void setConversionValue(Double conversionValue) {
        this.conversionValue = conversionValue;
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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
}
