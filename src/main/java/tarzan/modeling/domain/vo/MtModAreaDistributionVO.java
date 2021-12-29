package tarzan.modeling.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtModAreaDistributionVO
 *
 * @author: {xieyiyang}
 * @date: 2020/2/4 20:53
 * @description:
 */
public class MtModAreaDistributionVO implements Serializable {
    private static final long serialVersionUID = -4124443223967272827L;
    @ApiModelProperty(value = "区域配送属性ID")
    private String areaDistributionId;
    @ApiModelProperty(value = "区域ID")
    private String areaId;
    @ApiModelProperty(value = "库位ID")
    private String locatorId;

    public String getAreaDistributionId() {
        return areaDistributionId;
    }

    public void setAreaDistributionId(String areaDistributionId) {
        this.areaDistributionId = areaDistributionId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }
}
