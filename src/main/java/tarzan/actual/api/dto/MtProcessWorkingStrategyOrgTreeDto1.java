package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-06
 */
public class MtProcessWorkingStrategyOrgTreeDto1 implements Serializable {

    private static final long serialVersionUID = 4362140043622651706L;
    @ApiModelProperty("顶层站点ID")
    private String topSiteId;

    @ApiModelProperty("父组织类型")
    private String parentOrganizationType;

    @ApiModelProperty("父组织ID")
    private String parentOrganizationId;

    @ApiModelProperty("请求是否为点击查询按钮方式")
    private String isBtnQuery;

    @ApiModelProperty("工序作业平台配置ID")
    private String strategyId;

    public String getTopSiteId() {
        return topSiteId;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

    public String getParentOrganizationType() {
        return parentOrganizationType;
    }

    public void setParentOrganizationType(String parentOrganizationType) {
        this.parentOrganizationType = parentOrganizationType;
    }

    public String getParentOrganizationId() {
        return parentOrganizationId;
    }

    public void setParentOrganizationId(String parentOrganizationId) {
        this.parentOrganizationId = parentOrganizationId;
    }

    public String getIsBtnQuery() {
        return isBtnQuery;
    }

    public void setIsBtnQuery(String isBtnQuery) {
        this.isBtnQuery = isBtnQuery;
    }

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }
}
