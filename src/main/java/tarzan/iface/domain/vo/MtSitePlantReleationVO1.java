package tarzan.iface.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/6/26 10:26
 */
public class MtSitePlantReleationVO1 implements Serializable {
    private static final long serialVersionUID = 8318577760193859513L;
    private String plantCode;
    private String itemCode;
    private String siteId;
    private String materialId;
    private String materialSiteId;

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    public MtSitePlantReleationVO1(String plantCode, String itemCode, String siteId, String materialId, String materialSiteId) {
        this.plantCode = plantCode;
        this.itemCode = itemCode;
        this.siteId = siteId;
        this.materialId = materialId;
        this.materialSiteId = materialSiteId;
    }
}
