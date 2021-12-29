package tarzan.method.api.dto;

import java.io.Serializable;

public class MtRouterSiteAssignDTO implements Serializable {
    private static final long serialVersionUID = -3886831825172456356L;
    private String routerSiteAssignId;
    private String routerId;
    private String siteId;
    private String enableFlag;

    public String getRouterSiteAssignId() {
        return routerSiteAssignId;
    }

    public void setRouterSiteAssignId(String routerSiteAssignId) {
        this.routerSiteAssignId = routerSiteAssignId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
