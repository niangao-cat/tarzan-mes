package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtBomVO2 implements Serializable {

    private static final long serialVersionUID = -399761674372815040L;

    private String bomId; // 物料清单名

    private String bomName; // 物料清单名称

    private String revision; // 版本

    private List<String> siteId; // 站点ID（生产类型）

    private String bomType; // BOM类型

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomName() {
        return bomName;
    }

    public void setBomName(String bomName) {
        this.bomName = bomName;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public List<String> getSiteId() {
        return siteId;
    }

    public void setSiteId(List<String> siteId) {
        this.siteId = siteId;
    }

    public String getBomType() {
        return bomType;
    }

    public void setBomType(String bomType) {
        this.bomType = bomType;
    }

}
