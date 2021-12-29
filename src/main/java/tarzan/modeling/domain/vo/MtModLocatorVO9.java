package tarzan.modeling.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/12/9 19:39
 * @Description:
 */
public class MtModLocatorVO9 implements Serializable {
    private static final long serialVersionUID = -472018630068378370L;

    @ApiModelProperty("库位ID")
    private String locatorId;

    @ApiModelProperty("查询类型")
    private String queryType;

    @ApiModelProperty("库位类别")
    private String locatorCategory;

    @ApiModelProperty("库位类型")
    private String locatorType;

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getLocatorCategory() {
        return locatorCategory;
    }

    public void setLocatorCategory(String locatorCategory) {
        this.locatorCategory = locatorCategory;
    }

    public String getLocatorType() {
        return locatorType;
    }

    public void setLocatorType(String locatorType) {
        this.locatorType = locatorType;
    }
}
