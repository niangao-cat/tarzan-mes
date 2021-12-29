package io.tarzan.common.domain.vo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.hzero.mybatis.domian.SecurityToken;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MtUserVO implements SecurityToken {
    private String _token;
    private Long id;
    private String loginName;
    private String email;
    private Long organizationId;
    private String realName;
    private String phone;
    private String internationalTelCode;
    private String internationalTelMeaning;
    private String imageUrl;
    private String profilePhoto;
    private String language;
    private String languageName;
    private String timeZone;
    private String timeZoneMeaning;
    private Date lastPasswordUpdatedAt;
    private Date lastLoginAt;
    private Boolean enabled;
    private Boolean locked;
    private Boolean ldap;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lockedUntilAt;
    private Integer passwordAttempt;
    private Boolean admin;
    private String companyName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String nickname;
    private Integer gender;
    private Long countryId;
    private String countryName;
    private Long regionId;
    private String regionName;
    private String addressDetail;
    private String invitationCode;
    private Long employeeId;
    private Long textId;
    private String securityLevelCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDateActive;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDateActive;
    private Integer userSource;
    private Integer phoneCheckFlag;
    private Integer emailCheckFlag;
    private Integer passwordResetFlag;
    private Long defaultRoleId;
    private String defaultRoleName;
    private Long defaultCompanyId;
    private String defaultCompanyName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lockedDate;
    private String tenantName;
    private String tenantNum;
    private String groupName;
    private String groupNum;
    private String securityLevelMeaning;
    private String dateFormat;
    private String dateFormatMeaning;
    private String timeFormat;
    private String timeFormatMeaning;
    private String dateTimeFormat;
    private String title;
    private String logo;
    private String menuLayout;
    private String menuLayoutTheme;
    private Long tenantId;
    private Long currentRoleId;
    private String currentRoleCode;
    private String currentRoleName;
    private String currentRoleLevel;
    private String favicon;
    private Integer defaultTenant;
    private String userCondition;
    private String tenantCondition;
    private Long objectVersionNumber;
    private List<Long> excludeUserIds;
    private List<String> excludeUserLoginNames;

    public static String generateCacheKey(String configCode, Long tenantId) {
        StringBuilder sb = new StringBuilder();
        return sb.append("hpfm").append(":config").append(":").append(configCode).append(".").append(tenantId)
                        .toString();
    }

    public String getMenuLayout() {
        return this.menuLayout;
    }

    public void setMenuLayout(String menuLayout) {
        this.menuLayout = menuLayout;
    }

    public String getMenuLayoutTheme() {
        return this.menuLayoutTheme;
    }

    public void setMenuLayoutTheme(String menuLayoutTheme) {
        this.menuLayoutTheme = menuLayoutTheme;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getOrganizationId() {
        return this.organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInternationalTelCode() {
        return this.internationalTelCode;
    }

    public void setInternationalTelCode(String internationalTelCode) {
        this.internationalTelCode = internationalTelCode;
    }

    public String getInternationalTelMeaning() {
        return this.internationalTelMeaning;
    }

    public void setInternationalTelMeaning(String internationalTelMeaning) {
        this.internationalTelMeaning = internationalTelMeaning;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProfilePhoto() {
        return this.profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Date getLastPasswordUpdatedAt() {
        if (lastPasswordUpdatedAt == null) {
            return null;
        }
        return (Date) lastPasswordUpdatedAt.clone();
    }

    public void setLastPasswordUpdatedAt(Date lastPasswordUpdatedAt) {
        if (lastPasswordUpdatedAt == null) {
            this.lastPasswordUpdatedAt = null;
        } else {
            this.lastPasswordUpdatedAt = (Date) lastPasswordUpdatedAt.clone();
        }
    }

    public Date getLastLoginAt() {
        if (lastLoginAt == null) {
            return null;
        }
        return (Date) lastLoginAt.clone();
    }

    public void setLastLoginAt(Date lastLoginAt) {
        if (lastLoginAt == null) {
            this.lastLoginAt = null;
        } else {
            this.lastLoginAt = (Date) lastLoginAt.clone();
        }
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getLocked() {
        return this.locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getLdap() {
        return this.ldap;
    }

    public void setLdap(Boolean ldap) {
        this.ldap = ldap;
    }

    public Date getLockedUntilAt() {
        if (lockedUntilAt == null) {
            return null;
        }
        return (Date) lockedUntilAt.clone();
    }

    public void setLockedUntilAt(Date lockedUntilAt) {
        if (lockedUntilAt == null) {
            this.lockedUntilAt = null;
        } else {
            this.lockedUntilAt = (Date) lockedUntilAt.clone();
        }
    }

    public Integer getPasswordAttempt() {
        return this.passwordAttempt;
    }

    public void setPasswordAttempt(Integer passwordAttempt) {
        this.passwordAttempt = passwordAttempt;
    }

    public Boolean getAdmin() {
        return this.admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInvitationCode() {
        return this.invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getTextId() {
        return this.textId;
    }

    public void setTextId(Long textId) {
        this.textId = textId;
    }

    public String getSecurityLevelCode() {
        return this.securityLevelCode;
    }

    public void setSecurityLevelCode(String securityLevelCode) {
        this.securityLevelCode = securityLevelCode;
    }

    public LocalDate getStartDateActive() {
        return this.startDateActive;
    }

    public void setStartDateActive(LocalDate startDateActive) {
        this.startDateActive = startDateActive;
    }

    public LocalDate getEndDateActive() {
        return this.endDateActive;
    }

    public void setEndDateActive(LocalDate endDateActive) {
        this.endDateActive = endDateActive;
    }

    public Integer getUserSource() {
        return this.userSource;
    }

    public void setUserSource(Integer userSource) {
        this.userSource = userSource;
    }

    public Integer getPhoneCheckFlag() {
        return this.phoneCheckFlag;
    }

    public void setPhoneCheckFlag(Integer phoneCheckFlag) {
        this.phoneCheckFlag = phoneCheckFlag;
    }

    public Integer getEmailCheckFlag() {
        return this.emailCheckFlag;
    }

    public void setEmailCheckFlag(Integer emailCheckFlag) {
        this.emailCheckFlag = emailCheckFlag;
    }

    public Integer getPasswordResetFlag() {
        return this.passwordResetFlag;
    }

    public void setPasswordResetFlag(Integer passwordResetFlag) {
        this.passwordResetFlag = passwordResetFlag;
    }

    public Long getDefaultRoleId() {
        return this.defaultRoleId;
    }

    public void setDefaultRoleId(Long defaultRoleId) {
        this.defaultRoleId = defaultRoleId;
    }

    public String getDefaultRoleName() {
        return this.defaultRoleName;
    }

    public void setDefaultRoleName(String defaultRoleName) {
        this.defaultRoleName = defaultRoleName;
    }

    public Long getDefaultCompanyId() {
        return this.defaultCompanyId;
    }

    public void setDefaultCompanyId(Long defaultCompanyId) {
        this.defaultCompanyId = defaultCompanyId;
    }

    public String getDefaultCompanyName() {
        return this.defaultCompanyName;
    }

    public void setDefaultCompanyName(String defaultCompanyName) {
        this.defaultCompanyName = defaultCompanyName;
    }

    public Date getLockedDate() {
        if (lockedDate == null) {
            return null;
        }
        return (Date) lockedDate.clone();
    }

    public void setLockedDate(Date lockedDate) {
        if (lockedDate == null) {
            this.lockedDate = null;
        } else {
            this.lockedDate = (Date) lockedDate.clone();
        }
    }

    public String getTenantName() {
        return this.tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantNum() {
        return this.tenantNum;
    }

    public void setTenantNum(String tenantNum) {
        this.tenantNum = tenantNum;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupNum() {
        return this.groupNum;
    }

    public void setGroupNum(String groupNum) {
        this.groupNum = groupNum;
    }

    public String getSecurityLevelMeaning() {
        return this.securityLevelMeaning;
    }

    public void setSecurityLevelMeaning(String securityLevelMeaning) {
        this.securityLevelMeaning = securityLevelMeaning;
    }

    public Integer getDefaultTenant() {
        return this.defaultTenant;
    }

    public void setDefaultTenant(Integer defaultTenant) {
        this.defaultTenant = defaultTenant;
    }

    public String getUserCondition() {
        return this.userCondition;
    }

    public void setUserCondition(String userCondition) {
        this.userCondition = userCondition;
    }

    public String getTenantCondition() {
        return this.tenantCondition;
    }

    public void setTenantCondition(String tenantCondition) {
        this.tenantCondition = tenantCondition;
    }

    public Long getObjectVersionNumber() {
        return this.objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getLanguageName() {
        return this.languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getTimeZoneMeaning() {
        return this.timeZoneMeaning;
    }

    public void setTimeZoneMeaning(String timeZoneMeaning) {
        this.timeZoneMeaning = timeZoneMeaning;
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateFormatMeaning() {
        return this.dateFormatMeaning;
    }

    public void setDateFormatMeaning(String dateFormatMeaning) {
        this.dateFormatMeaning = dateFormatMeaning;
    }

    public String getTimeFormat() {
        return this.timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public String getTimeFormatMeaning() {
        return this.timeFormatMeaning;
    }

    public void setTimeFormatMeaning(String timeFormatMeaning) {
        this.timeFormatMeaning = timeFormatMeaning;
    }

    public String getDateTimeFormat() {
        return this.dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCurrentRoleId() {
        return this.currentRoleId;
    }

    public void setCurrentRoleId(Long currentRoleId) {
        this.currentRoleId = currentRoleId;
    }

    public String getCurrentRoleCode() {
        return this.currentRoleCode;
    }

    public void setCurrentRoleCode(String currentRoleCode) {
        this.currentRoleCode = currentRoleCode;
    }

    public String getCurrentRoleName() {
        return this.currentRoleName;
    }

    public void setCurrentRoleName(String currentRoleName) {
        this.currentRoleName = currentRoleName;
    }

    public String getCurrentRoleLevel() {
        return this.currentRoleLevel;
    }

    public void setCurrentRoleLevel(String currentRoleLevel) {
        this.currentRoleLevel = currentRoleLevel;
    }

    public List<Long> getExcludeUserIds() {
        return this.excludeUserIds;
    }

    public void setExcludeUserIds(List<Long> excludeUserIds) {
        this.excludeUserIds = excludeUserIds;
    }

    public List<String> getExcludeUserLoginNames() {
        return this.excludeUserLoginNames;
    }

    public void setExcludeUserLoginNames(List<String> excludeUserLoginNames) {
        this.excludeUserLoginNames = excludeUserLoginNames;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGender() {
        return this.gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Long getCountryId() {
        return this.countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getRegionId() {
        return this.regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getAddressDetail() {
        return this.addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    @Override
    public String get_token() {
        return this._token;
    }

    @Override
    public void set_token(String tokenValue) {
        this._token = tokenValue;
    }


}
