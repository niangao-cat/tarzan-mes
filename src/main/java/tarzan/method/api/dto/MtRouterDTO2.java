package tarzan.method.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtRouterDTO2 implements Serializable {
    private static final long serialVersionUID = -7064956381145380622L;

    @ApiModelProperty("来源工艺路线Id")
    @NotEmpty
    private String sourceRouterId;
    @ApiModelProperty("目标工艺路线名称")
    @NotEmpty
    private String targetRouterName;
    @ApiModelProperty("目标工艺路线类型")
    @NotEmpty
    private String targetRouterType;
    @ApiModelProperty("目标工艺路线站点")
    @NotEmpty
    private String targetSiteId;
    @ApiModelProperty("目标工艺路线版本")
    @NotEmpty
    private String targetRevision;

    public String getSourceRouterId() {
        return sourceRouterId;
    }

    public void setSourceRouterId(String sourceRouterId) {
        this.sourceRouterId = sourceRouterId;
    }

    public String getTargetRouterName() {
        return targetRouterName;
    }

    public void setTargetRouterName(String targetRouterName) {
        this.targetRouterName = targetRouterName;
    }

    public String getTargetRouterType() {
        return targetRouterType;
    }

    public void setTargetRouterType(String targetRouterType) {
        this.targetRouterType = targetRouterType;
    }

    public String getTargetSiteId() {
        return targetSiteId;
    }

    public void setTargetSiteId(String targetSiteId) {
        this.targetSiteId = targetSiteId;
    }

    public String getTargetRevision() {
        return targetRevision;
    }

    public void setTargetRevision(String targetRevision) {
        this.targetRevision = targetRevision;
    }
}
