package tarzan.modeling.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtModOrganizationVO3 implements Serializable {
    private static final long serialVersionUID = 6014047898925073555L;

    @ApiModelProperty(value = "组织Id")
    private String orgId;
    @ApiModelProperty(value = "组织类型")
    private String orgType;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }
}
