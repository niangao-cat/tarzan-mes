package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-06
 */
public class MtProcessWorkingStrategyOrgDto implements Serializable {
    private static final long serialVersionUID = 7180244143840470252L;

    @ApiModelProperty("关系Id")
    private String strategyOrganizationId;

    @ApiModelProperty("组织ID")
    private String organizationId;

    @ApiModelProperty("组织编码")
    private String organizationCode;

    @ApiModelProperty("组织类型")
    private String organizationType;

    @ApiModelProperty("组织类型描述")
    private String organizationTypeDesc;

    @ApiModelProperty("有效性")
    private String enableFlag;

    public String getStrategyOrganizationId() {
        return strategyOrganizationId;
    }

    public void setStrategyOrganizationId(String strategyOrganizationId) {
        this.strategyOrganizationId = strategyOrganizationId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationTypeDesc() {
        return organizationTypeDesc;
    }

    public void setOrganizationTypeDesc(String organizationTypeDesc) {
        this.organizationTypeDesc = organizationTypeDesc;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
