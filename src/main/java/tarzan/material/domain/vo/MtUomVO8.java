package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author chuang.yang
 * @date 2021/4/22
 */
public class MtUomVO8 implements Serializable {

    private static final long serialVersionUID = -2375620864251299835L;
    @ApiModelProperty("来源单位计量值")
    private Double sourceValue;

    @ApiModelProperty("来源单位ID")
    private String sourceUomId;

    @ApiModelProperty("目标单位计量值")
    private Double targetValue;


    public Double getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(Double sourceValue) {
        this.sourceValue = sourceValue;
    }

    public String getSourceUomId() {
        return sourceUomId;
    }

    public void setSourceUomId(String sourceUomId) {
        this.sourceUomId = sourceUomId;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

}
