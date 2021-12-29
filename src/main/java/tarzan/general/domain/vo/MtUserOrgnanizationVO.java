package tarzan.general.domain.vo;

import tarzan.general.domain.entity.MtUserOrganization;

import java.io.Serializable;

public class MtUserOrgnanizationVO extends MtUserOrganization implements Serializable {

    private static final long serialVersionUID = -3880067172586998673L;
    private String userName;
    private String userDesc;
    private String organizationCode;
    private String organizationDesc;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationDesc() {
        return organizationDesc;
    }

    public void setOrganizationDesc(String organizationDesc) {
        this.organizationDesc = organizationDesc;
    }
}
