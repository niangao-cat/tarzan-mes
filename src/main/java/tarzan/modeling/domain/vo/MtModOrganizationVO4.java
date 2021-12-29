package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtModOrganizationVO4
 *
 * @author: {xieyiyang}
 * @date: 2020/2/20 9:43
 * @description:
 */
public class MtModOrganizationVO4 implements Serializable {
    private static final long serialVersionUID = -812660050242366631L;

    @ApiModelProperty("顶层站点ID")
    private String topSiteId;
    @ApiModelProperty("目标组织类型编码")
    private String parentOrganizationType;
    @ApiModelProperty("子层组织结构类型编码")
    private String organizationType;
    @ApiModelProperty("子层组织对象ID列表")
    private List<String> organizationIdList;
    @ApiModelProperty("查询类型（仅限查询区域、工作单元时使用）")
    private String queryType;

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

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public List<String> getOrganizationIdList() {
        return organizationIdList;
    }

    public void setOrganizationIdList(List<String> organizationIdList) {
        this.organizationIdList = organizationIdList;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
