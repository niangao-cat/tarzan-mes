package tarzan.dispatch.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtDispatchStrategyOrgRelVO4
 * @description
 * @date 2020年02月04日 9:59
 */
public class MtDispatchStrategyOrgRelVO4 implements Serializable {
    private static final long serialVersionUID = -7181568186731754193L;

    @ApiModelProperty(value = "主键")
    private String strategyOrgRelId;
    @ApiModelProperty(value = "业务组织实体类型")
    private String organizationType;
    @ApiModelProperty(value = "业务组织实体")
    private String organizationId;
    @ApiModelProperty(value = "范围策略")
    private String rangeStrategy;
    @ApiModelProperty(value = "发布策略")
    private String publishStrategy;
    @ApiModelProperty(value = "移动策略")
    private String moveStrategy;

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
}
