package tarzan.iface.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Leeloing
 * @date 2019/6/26 13:31
 */
public class MtSitePlantReleationVO2 implements Serializable {
    private static final long serialVersionUID = -4322543597359816552L;
    private List<String> materialIds;
    private List<String> siteIds;

    public List<String> getMaterialIds() {
        return materialIds;
    }

    public void setMaterialIds(List<String> materialIds) {
        this.materialIds = materialIds;
    }

    public List<String> getSiteIds() {
        return siteIds;
    }

    public void setSiteIds(List<String> siteIds) {
        this.siteIds = siteIds;
    }
}
