package tarzan.general.api.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class MtUserOrganizationDTO3 implements Serializable {
    private static final long serialVersionUID = -2190350009528819811L;

    @ApiModelProperty(value = "用户组织关系ID", required = true)
    @Id
    private String userOrganizationId;
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull
    private Long userId;
    @ApiModelProperty(value = "用户关联组织类型", required = true)
    @NotNull
    private String organizationType;
    @ApiModelProperty(value = "用户关联组织对象ID", required = true)
    private String organizationId;
    @ApiModelProperty(value = "默认组织标识")
    private String defaultOrganizationFlag;
    @ApiModelProperty(value = "有效性", required = true)
    private String enableFlag;

    public String getUserOrganizationId() {
        return userOrganizationId;
    }

    public void setUserOrganizationId(String userOrganizationId) {
        this.userOrganizationId = userOrganizationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public String getDefaultOrganizationFlag() {
        return defaultOrganizationFlag;
    }

    public void setDefaultOrganizationFlag(String defaultOrganizationFlag) {
        this.defaultOrganizationFlag = defaultOrganizationFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
