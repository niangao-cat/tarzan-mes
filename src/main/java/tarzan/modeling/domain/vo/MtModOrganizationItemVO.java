package tarzan.modeling.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by slj on 2018-11-22.
 */
public class MtModOrganizationItemVO implements Serializable {
    private static final long serialVersionUID = 8821756587449267734L;

    @ApiModelProperty("组织ID")
    private String organizationId;

    @ApiModelProperty("组织类型")
    private String organizationType;

    @ApiModelProperty("顺序")
    private Long sequence;

    @ApiModelProperty("组织关系ID")
    private String relId;

    @ApiModelProperty("PRO")
    private Integer pro;

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

    public Integer getPro() {
        return pro;
    }

    public void setPro(Integer pro) {
        this.pro = pro;
    }

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return "MtModOrganizationItemVO{" + "organizationId='" + organizationId + '\'' + ", organizationType='"
                        + organizationType + '\'' + ", sequence=" + sequence + ", relId='" + relId + '\'' + ", pro="
                        + pro + '}';
    }
}
