package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/9/4 11:08
 */
public class MtModLocatorVO14 implements Serializable {
    private static final long serialVersionUID = 5937261698713299897L;

    @ApiModelProperty("库位ID")
    private String locatorId;

    @ApiModelProperty("子层库位对象")
    private List<MtModLocatorVO12> subLocatorIdList;

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public List<MtModLocatorVO12> getSubLocatorIdList() {
        return subLocatorIdList;
    }

    public void setSubLocatorIdList(List<MtModLocatorVO12> subLocatorIdList) {
        this.subLocatorIdList = subLocatorIdList;
    }
}
