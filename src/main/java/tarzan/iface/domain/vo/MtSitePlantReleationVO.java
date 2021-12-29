package tarzan.iface.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Leeloing
 * @date 2019/6/26 10:20
 */
public class MtSitePlantReleationVO implements Serializable {

    private static final long serialVersionUID = 4697680512690042022L;
    private List<String> itemCodeList;
    private List<String> plantCodeList;
    private String siteType;


    public List<String> getItemCodeList() {
        return itemCodeList;
    }

    public void setItemCodeList(List<String> itemCodeList) {
        this.itemCodeList = itemCodeList;
    }


    public List<String> getPlantCodeList() {
        return plantCodeList;
    }

    public void setPlantCodeList(List<String> plantCodeList) {
        this.plantCodeList = plantCodeList;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }
}
