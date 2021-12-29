package tarzan.iface.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Leeloing
 * @date 2019-09-24 14:03
 */
public class MtErpSubinvLocatorVO implements Serializable {
    private static final long serialVersionUID = -6951604118943253474L;
    private String plantCode;
    private String subinvCode;
    private String locatorCode;


    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getSubinvCode() {
        return subinvCode;
    }

    public void setSubinvCode(String subinvCode) {
        this.subinvCode = subinvCode;
    }

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public MtErpSubinvLocatorVO(String plantCode, String subinvCode, String locatorCode) {
        this.plantCode = plantCode;
        this.subinvCode = subinvCode;
        this.locatorCode = locatorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtErpSubinvLocatorVO that = (MtErpSubinvLocatorVO) o;
        return Objects.equals(getPlantCode(), that.getPlantCode())
                        && Objects.equals(getSubinvCode(), that.getSubinvCode())
                        && Objects.equals(getLocatorCode(), that.getLocatorCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlantCode(), getSubinvCode(), getLocatorCode());
    }
}
