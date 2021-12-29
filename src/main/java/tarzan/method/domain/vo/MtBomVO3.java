package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtBomVO3 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7883767763518738637L;

    private String bomId; // 物料清单名

    private String bomType; // BOM类型

    private String siteId;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomType() {
        return bomType;
    }

    public void setBomType(String bomType) {
        this.bomType = bomType;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

}
