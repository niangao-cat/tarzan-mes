package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModLocatorOrgRelDTO4 implements Serializable {

    private static final long serialVersionUID = 5025971505829577298L;
    @ApiModelProperty(value = "父层结构类型，如站点、区域、产线、工作单元", required = true)
    private String organizationType;
    @ApiModelProperty(value = "父层结构对象值ID", required = true)
    private String organizationId;
    @ApiModelProperty(value = "子层库位ID", required = true)
    private String locatorId;


    /**
     * @return 父层结构类型，如站点、区域、产线、工作单元
     */
    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    /**
     * @return 父层结构对象值ID
     */
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return 子层库位ID
     */
    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }
}
