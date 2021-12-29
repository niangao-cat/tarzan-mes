package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;

/**
 * @author benjamin
 */
public class MtRouterDTO implements Serializable {
    private static final long serialVersionUID = -3037096483145249547L;

    @ApiModelProperty("工艺路线唯一标识")
    private String routerId;
    @ApiModelProperty(value = "工艺路线简称")
    @NotBlank
    private String routerName;
    @ApiModelProperty(value = "工艺路线类型")
    @NotBlank
    private String routerType;
    @ApiModelProperty(value = "工艺路线描述")
    private String description;
    @ApiModelProperty(value = "工艺路线状态")
    @NotBlank
    private String routerStatus;
    @ApiModelProperty(value = "工艺路线版本")
    @NotBlank
    private String revision;
    @ApiModelProperty(value = "是否为当前版本")
    private String currentFlag;
    @ApiModelProperty(value = "自动升版本标识")
    private String autoRevisionFlag;
    @ApiModelProperty(value = "是否为松散工艺路线")
    private String relaxedFlowFlag;
    @ApiModelProperty(value = "是否已经应用于EO")
    private String hasBeenReleasedFlag;
    @ApiModelProperty(value = "生效时间")
    @NotNull
    private Date dateFrom;
    @ApiModelProperty(value = "失效时间")
    private Date dateTo;
    @ApiModelProperty(value = "旧装配清单ID")
    private String oldBomId;
    @ApiModelProperty(value = "装配清单ID")
    private String bomId;
    @ApiModelProperty(value = "装配清单类型")
    private String bomType;
    @ApiModelProperty(value = "装配清单类型描述")
    private String bomTypeDesc;
    @ApiModelProperty(value = "装配清单版本")
    private String bomRevision;
    @ApiModelProperty(value = "装配清单名称")
    private String bomName;
    @ApiModelProperty(value = "复制来源工艺路线标识")
    private String copiedFromRouterId;
    @ApiModelProperty(value = "复制来源工艺路线简称")
    private String copiedFromRouterName;
    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls = Collections.emptyMap();
    @ApiModelProperty("工艺路线扩展属性")
    private List<MtExtendAttrDTO3> routerAttrs;

    @ApiModelProperty("工艺路线步骤")
    private List<MtRouterStepDTO4> mtRouterStepDTO;

    @ApiModelProperty("工艺路线站点分配")
    private List<MtRouterSiteAssignDTO2> mtRouterSiteAssignDTO;

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

    public String getAutoRevisionFlag() {
        return autoRevisionFlag;
    }

    public void setAutoRevisionFlag(String autoRevisionFlag) {
        this.autoRevisionFlag = autoRevisionFlag;
    }

    public String getRelaxedFlowFlag() {
        return relaxedFlowFlag;
    }

    public void setRelaxedFlowFlag(String relaxedFlowFlag) {
        this.relaxedFlowFlag = relaxedFlowFlag;
    }

    public String getHasBeenReleasedFlag() {
        return hasBeenReleasedFlag;
    }

    public void setHasBeenReleasedFlag(String hasBeenReleasedFlag) {
        this.hasBeenReleasedFlag = hasBeenReleasedFlag;
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

    public String getOldBomId() {
        return oldBomId;
    }

    public void setOldBomId(String oldBomId) {
        this.oldBomId = oldBomId;
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

    public String getCopiedFromRouterId() {
        return copiedFromRouterId;
    }

    public void setCopiedFromRouterId(String copiedFromRouterId) {
        this.copiedFromRouterId = copiedFromRouterId;
    }

    public String getCopiedFromRouterName() {
        return copiedFromRouterName;
    }

    public void setCopiedFromRouterName(String copiedFromRouterName) {
        this.copiedFromRouterName = copiedFromRouterName;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

    public List<MtExtendAttrDTO3> getRouterAttrs() {
        return routerAttrs;
    }

    public void setRouterAttrs(List<MtExtendAttrDTO3> routerAttrs) {
        this.routerAttrs = routerAttrs;
    }

    public List<MtRouterStepDTO4> getMtRouterStepDTO() {
        return mtRouterStepDTO;
    }

    public void setMtRouterStepDTO(List<MtRouterStepDTO4> mtRouterStepDTO) {
        this.mtRouterStepDTO = mtRouterStepDTO;
    }

    public List<MtRouterSiteAssignDTO2> getMtRouterSiteAssignDTO() {
        return mtRouterSiteAssignDTO;
    }

    public void setMtRouterSiteAssignDTO(List<MtRouterSiteAssignDTO2> mtRouterSiteAssignDTO) {
        this.mtRouterSiteAssignDTO = mtRouterSiteAssignDTO;
    }

    public String getBomType() {
        return bomType;
    }

    public void setBomType(String bomType) {
        this.bomType = bomType;
    }

    public String getBomTypeDesc() {
        return bomTypeDesc;
    }

    public void setBomTypeDesc(String bomTypeDesc) {
        this.bomTypeDesc = bomTypeDesc;
    }

    public String getBomRevision() {
        return bomRevision;
    }

    public void setBomRevision(String bomRevision) {
        this.bomRevision = bomRevision;
    }
}
