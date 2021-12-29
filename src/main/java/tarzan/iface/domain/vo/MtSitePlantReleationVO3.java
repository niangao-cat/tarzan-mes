package tarzan.iface.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Leeloing
 * @date 2019/6/26 16:31
 */
public class MtSitePlantReleationVO3 implements Serializable {

    private static final long serialVersionUID = -9015668978837075154L;
    private List<String> plantCodes;
    private String siteType;

    public List<String> getPlantCodes() {
        return plantCodes;
    }

    public void setPlantCodes(List<String> plantCodes) {
        this.plantCodes = plantCodes;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }
}
