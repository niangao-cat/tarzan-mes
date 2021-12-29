package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 0006, 2020-02-06
 */
public class MtProcessWorkingStrategyOrgDto3 implements Serializable {
    private static final long serialVersionUID = 823128813144029660L;

    @ApiModelProperty("工序作业平台配置组织关系唯一标识，主键")
    private String strategyOrganizationId;

    @ApiModelProperty(value = "工序作业平台配置唯一标识，用于其他数据结构引用")
    private String strategyId;

    @ApiModelProperty(value = "配置编码")
    private String strategyCode;

    @ApiModelProperty(value = "组织Id")
    private String organizationId;

    @ApiModelProperty(value = "组织编码")
    private String organizationCode;

    @ApiModelProperty(value = "组织描述")
    private String organizationDesc;

    @ApiModelProperty(value = "组织类型")
    private String organizationType;

    @ApiModelProperty(value = "组织类型描述")
    private String organizationTypeDesc;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    public String getStrategyOrganizationId() {
        return strategyOrganizationId;
    }

    public void setStrategyOrganizationId(String strategyOrganizationId) {
        this.strategyOrganizationId = strategyOrganizationId;
    }

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getStrategyCode() {
        return strategyCode;
    }

    public void setStrategyCode(String strategyCode) {
        this.strategyCode = strategyCode;
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

    public String getOrganizationDesc() {
        return organizationDesc;
    }

    public void setOrganizationDesc(String organizationDesc) {
        this.organizationDesc = organizationDesc;
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
