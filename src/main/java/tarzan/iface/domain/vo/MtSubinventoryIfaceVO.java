package tarzan.iface.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Leeloing
 * @date 2019/12/12 7:32 下午
 */
public class MtSubinventoryIfaceVO implements Serializable {
    private static final long serialVersionUID = 6715514635118734826L;
    private String plantCode;
    private String subinventoryCode;

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getSubinventoryCode() {
        return subinventoryCode;
    }

    public void setSubinventoryCode(String subinventoryCode) {
        this.subinventoryCode = subinventoryCode;
    }

    public MtSubinventoryIfaceVO(String plantCode, String subinventoryCode) {
        this.plantCode = plantCode;
        this.subinventoryCode = subinventoryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtSubinventoryIfaceVO that = (MtSubinventoryIfaceVO) o;
        return Objects.equals(plantCode, that.plantCode) && Objects.equals(subinventoryCode, that.subinventoryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plantCode, subinventoryCode);
    }
}
