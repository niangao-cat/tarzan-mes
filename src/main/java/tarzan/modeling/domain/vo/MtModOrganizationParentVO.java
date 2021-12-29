package tarzan.modeling.domain.vo;

import java.io.Serializable;

/**
 * Created by slj on 2018-10-09.
 */
public class MtModOrganizationParentVO implements Serializable {
    private static final long serialVersionUID = 2443788779900936140L;
    // 组织ID
    private String organizationId;
    // 组织类型
    private String organizationType;
    private String topSiteId;
    private Long sequence;

    public int getPro() {
        return pro;
    }

    public void setPro(int pro) {
        this.pro = pro;
    }

    private int pro;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getTopSiteId() {
        return topSiteId;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
}
