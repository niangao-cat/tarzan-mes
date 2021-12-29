package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtRouterVO1 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7568542174051059819L;
    private String routerId;
    private String routerName;
    private String routerType;
    private List<String> siteIds;
    private String revision;
    private String bomId;

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public String getRouterType() {
        return routerType;
    }

    public void setRouterType(String routerType) {
        this.routerType = routerType;
    }

    public List<String> getSiteIds() {
        return siteIds;
    }

    public void setSiteIds(List<String> siteIds) {
        this.siteIds = siteIds;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

}
