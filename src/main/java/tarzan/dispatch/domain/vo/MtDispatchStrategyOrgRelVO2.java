package tarzan.dispatch.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtDispatchStrategyOrgRelVO2
 * @description
 * @date 2020年02月04日 9:38
 */
public class MtDispatchStrategyOrgRelVO2 implements Serializable {
    private static final long serialVersionUID = -6574189576216914914L;

    @ApiModelProperty(value = "业务组织实体类型")
    private String organizationType;
    @ApiModelProperty(value = "业务组织实体")
    private String organizationId;

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
