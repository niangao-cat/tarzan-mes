package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/9/4 10:59
 */
public class MtModLocatorVO13 implements Serializable {
    private static final long serialVersionUID = -446387152120347404L;
    @ApiModelProperty("库位ID")
    private List<String> locatorIds;

    @ApiModelProperty("查询类型")
    private String queryType;

    @ApiModelProperty("库位类别")
    private List<String> locatorCategorys;

    @ApiModelProperty("库位类型")
    private List<String> locatorTypes;

    public List<String> getLocatorIds() {
        return locatorIds;
    }

    public void setLocatorIds(List<String> locatorIds) {
        this.locatorIds = locatorIds;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public List<String> getLocatorCategorys() {
        return locatorCategorys;
    }

    public void setLocatorCategorys(List<String> locatorCategorys) {
        this.locatorCategorys = locatorCategorys;
    }

    public List<String> getLocatorTypes() {
        return locatorTypes;
    }

    public void setLocatorTypes(List<String> locatorTypes) {
        this.locatorTypes = locatorTypes;
    }
}
