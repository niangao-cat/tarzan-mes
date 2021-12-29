package tarzan.modeling.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/1/7 13:39
 * @Description:
 */
public class MtModLocatorVO10 implements Serializable {
    private static final long serialVersionUID = 2906393723717513499L;

    @ApiModelProperty("父库位ID")
    private String parentLocatorId;

    @ApiModelProperty("库位ID")
    private String locatorId;

    public String getParentLocatorId() {
        return parentLocatorId;
    }

    public void setParentLocatorId(String parentLocatorId) {
        this.parentLocatorId = parentLocatorId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }
}
