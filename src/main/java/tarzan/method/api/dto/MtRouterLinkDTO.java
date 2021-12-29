package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtRouterLinkDTO implements Serializable {
    private static final long serialVersionUID = 1940037738929650937L;

    @ApiModelProperty("步骤标识")
    private String routerStepId;
    @ApiModelProperty("表ID，主键，供其他表做外键")
    private String routerLinkId;
    @ApiModelProperty(value = "工艺路线标识")
    @NotBlank
    private String routerId;
    @ApiModelProperty(value = "工艺路线简称")
    @NotBlank
    private String routerName;
    @ApiModelProperty(value = "工艺路线类型")
    @NotBlank
    private String routerType;
    @ApiModelProperty(value = "物料清单描述")
    private String description;
    @ApiModelProperty(value = "工艺路线状态")
    @NotBlank
    private String routerStatus;
    @ApiModelProperty(value = "工艺路线版本")
    @NotBlank
    private String revision;
    @ApiModelProperty(value = "是否为当前版本")
    private String currentFlag;
    @ApiModelProperty(value = "生效时间")
    @NotNull
    private Date dateFrom;
    @ApiModelProperty(value = "失效时间")
    private Date dateTo;
    @ApiModelProperty(value = "装配清单ID")
    private String bomId;
    @ApiModelProperty(value = "装配清单名称")
    private String bomName;

    public String getRouterLinkId() {
        return routerLinkId;
    }

    public void setRouterLinkId(String routerLinkId) {
        this.routerLinkId = routerLinkId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public String getRouterType() {
        return routerType;
    }

    public void setRouterType(String routerType) {
        this.routerType = routerType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRouterStatus() {
        return routerStatus;
    }

    public void setRouterStatus(String routerStatus) {
        this.routerStatus = routerStatus;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(String currentFlag) {
        this.currentFlag = currentFlag;
    }

    public Date getDateFrom() {
        if (dateFrom == null) {
            return null;
        } else {
            return (Date) dateFrom.clone();
        }
    }

    public void setDateFrom(Date dateFrom) {
        if (dateFrom == null) {
            this.dateFrom = null;
        } else {
            this.dateFrom = (Date) dateFrom.clone();
        }
    }

    public Date getDateTo() {
        if (dateTo == null) {
            return null;
        } else {
            return (Date) dateTo.clone();
        }
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomName() {
        return bomName;
    }

    public void setBomName(String bomName) {
        this.bomName = bomName;
    }
}
