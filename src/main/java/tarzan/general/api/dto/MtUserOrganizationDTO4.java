package tarzan.general.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtUserOrganizationDTO4 implements Serializable {

    private static final long serialVersionUID = -6641439015298465215L;
    @ApiModelProperty("用户组织关系ID")
    private String userOrganizationId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户描述")
    private String userDesc;

    @ApiModelProperty("用户关联组织对象ID")
    private String organizationId;

    @ApiModelProperty(value = "组织编码")
    private String organizationCode;

    @ApiModelProperty(value = "组织描述")
    private String organizationDesc;

    @ApiModelProperty(value = "组织类型")
    private String organizationType;

    @ApiModelProperty(value = "库位类别")
    private String locatorCategory;

    @ApiModelProperty(value = "默认组织标识")
    private String defaultOrganizationFlag;

    @ApiModelProperty(value = "有效性", required = true)
    private String enableFlag;

    @ApiModelProperty(value = "有效性")
    private String locatorEnableFlag;

    private Long cid;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserOrganizationId() {
        return userOrganizationId;
    }

    public void setUserOrganizationId(String userOrganizationId) {
        this.userOrganizationId = userOrganizationId;
    }

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

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getLocatorCategory() {
        return locatorCategory;
    }

    public void setLocatorCategory(String locatorCategory) {
        this.locatorCategory = locatorCategory;
    }

    public String getLocatorEnableFlag() {
        return locatorEnableFlag;
    }

    public void setLocatorEnableFlag(String locatorEnableFlag) {
        this.locatorEnableFlag = locatorEnableFlag;
    }
}


