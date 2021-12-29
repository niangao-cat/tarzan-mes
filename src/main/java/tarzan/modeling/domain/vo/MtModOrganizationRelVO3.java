package tarzan.modeling.domain.vo;

import java.io.Serializable;

/**
 * @author : MrZ
 * @date : 2019-12-17 11:02
 **/
public class MtModOrganizationRelVO3 implements Serializable {
    private static final long serialVersionUID = -8550512504972305238L;
    private String siteId;
    private String organizationId;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
