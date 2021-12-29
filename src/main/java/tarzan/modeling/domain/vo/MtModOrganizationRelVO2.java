package tarzan.modeling.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author : MrZ
 * @date : 2019-12-17 11:02
 **/
public class MtModOrganizationRelVO2 implements Serializable {
    private static final long serialVersionUID = 1998376524479887325L;
    private String organizationType;
    private List<String> organizationIdList;
    private String siteType;

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public List<String> getOrganizationIdList() {
        return organizationIdList;
    }

    public void setOrganizationIdList(List<String> organizationIdList) {
        this.organizationIdList = organizationIdList;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }
}
