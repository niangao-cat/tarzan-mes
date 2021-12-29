package tarzan.modeling.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtModAreaDistributionVO2
 *
 * @author: {xieyiyang}
 * @date: 2020/2/4 20:54
 * @description:
 */
public class MtModAreaDistributionVO2 implements Serializable {
    private static final long serialVersionUID = -9133806208639145996L;
    @ApiModelProperty(value = "区域配送属性ID")
    private String areaDistributionId;
    @ApiModelProperty(value = "区域ID")
    private String areaId;
    @ApiModelProperty(value = "配送模式")
    private String distributionMode;
    @ApiModelProperty(value = "是否启用拉动时段")
    private String pullTimeIntervalFlag;
    @ApiModelProperty(value = "配送周期")
    private Double distributionCycle;
    @ApiModelProperty(value = "指令业务类型")
    private String businessType;
    @ApiModelProperty(value = "是否采用装配件生产速率")
    private String instructCreatedByEo;
    @ApiModelProperty(value = "来源库位ID")
    private String sourceLocatorId;
    @ApiModelProperty(value = "优先级")
    private Long sequence;
    @ApiModelProperty(value = "触发到送达的周期")
    private Double pullToArrive;

    public String getAreaDistributionId() {
        return areaDistributionId;
    }

    public void setAreaDistributionId(String areaDistributionId) {
        this.areaDistributionId = areaDistributionId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getDistributionMode() {
        return distributionMode;
    }

    public void setDistributionMode(String distributionMode) {
        this.distributionMode = distributionMode;
    }

    public String getPullTimeIntervalFlag() {
        return pullTimeIntervalFlag;
    }

    public void setPullTimeIntervalFlag(String pullTimeIntervalFlag) {
        this.pullTimeIntervalFlag = pullTimeIntervalFlag;
    }

    public Double getDistributionCycle() {
        return distributionCycle;
    }

    public void setDistributionCycle(Double distributionCycle) {
        this.distributionCycle = distributionCycle;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getInstructCreatedByEo() {
        return instructCreatedByEo;
    }

    public void setInstructCreatedByEo(String instructCreatedByEo) {
        this.instructCreatedByEo = instructCreatedByEo;
    }

    public String getSourceLocatorId() {
        return sourceLocatorId;
    }

    public void setSourceLocatorId(String sourceLocatorId) {
        this.sourceLocatorId = sourceLocatorId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Double getPullToArrive() {
        return pullToArrive;
    }

    public void setPullToArrive(Double pullToArrive) {
        this.pullToArrive = pullToArrive;
    }
}
