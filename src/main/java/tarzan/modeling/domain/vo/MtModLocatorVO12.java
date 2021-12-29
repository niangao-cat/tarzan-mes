package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/9/4 09:45
 */
public class MtModLocatorVO12 implements Serializable {

    private static final long serialVersionUID = 1025419524618778754L;

    @ApiModelProperty("子层库位ID")
    private String subLocatorId;

    @ApiModelProperty("子层库位类别")
    private String locatorCategory;

    @ApiModelProperty("子层库位类型")
    private String locatorType;

    public String getSubLocatorId() {
        return subLocatorId;
    }

    public void setSubLocatorId(String subLocatorId) {
        this.subLocatorId = subLocatorId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtModLocatorVO12 that = (MtModLocatorVO12) o;
        return Objects.equals(getSubLocatorId(), that.getSubLocatorId())
                        && Objects.equals(getLocatorCategory(), that.getLocatorCategory())
                        && Objects.equals(getLocatorType(), that.getLocatorType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubLocatorId(), getLocatorCategory(), getLocatorType());
    }
}
