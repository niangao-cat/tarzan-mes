package tarzan.dispatch.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * MtDispatchStrategyOrgRelVO1
 *
 * @author: {xieyiyang}
 * @date: 2020/2/3 22:38
 * @description:
 */
public class MtDispatchStrategyOrgRelVO1 implements Serializable {
    private static final long serialVersionUID = -6038127267294124204L;

    @ApiModelProperty("主键")
    private String strategyOrgRelId;
    @ApiModelProperty("业务组织实体类型")
    private String organizationType;
    @ApiModelProperty("业务组织实体")
    private String organizationId;
    @ApiModelProperty("范围策略")
    private String rangeStrategy;
    @ApiModelProperty("调度策略")
    private String publishStrategy;
    @ApiModelProperty("移动策略")
    private String moveStrategy;
    @ApiModelProperty("是否有效")
    private String enableFlag;

    public String getStrategyOrgRelId() {
        return strategyOrgRelId;
    }

    public void setStrategyOrgRelId(String strategyOrgRelId) {
        this.strategyOrgRelId = strategyOrgRelId;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getRangeStrategy() {
        return rangeStrategy;
    }

    public void setRangeStrategy(String rangeStrategy) {
        this.rangeStrategy = rangeStrategy;
    }

    public String getPublishStrategy() {
        return publishStrategy;
    }

    public void setPublishStrategy(String publishStrategy) {
        this.publishStrategy = publishStrategy;
    }

    public String getMoveStrategy() {
        return moveStrategy;
    }

    public void setMoveStrategy(String moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
