package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-04 15:57
 **/
public class MtOperationVO3 implements Serializable {
    private static final long serialVersionUID = -2566750792029402609L;

    @ApiModelProperty(value = "站点标识")
    private List<String> siteIdList;
    @ApiModelProperty(value = "工艺名称")
    private List<String> operationNameList;
    @ApiModelProperty(value = "工艺版本")
    private List<String> revisionList;
    @ApiModelProperty(value = "Y/N，是否当前版本")
    private String currentFlag;
    @ApiModelProperty(value = "有效时间至")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateFrom;
    @ApiModelProperty(value = "有效时间从")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateTo;
    @ApiModelProperty(value = "备注，有条件的话做成多行文本")
    private List<String> descriptionList;
    @ApiModelProperty(value = "工艺状态（STATUS_GROUP:OPERATION_STATUS）")
    private List<String> operationStatusList;
    @ApiModelProperty(value = "工艺类型（TYPE_GROUP:OPERATION_TYPE）")
    private List<String> operationTypeList;
    @ApiModelProperty(value = "特殊工艺路线ID")
    private List<String> specialRouterIdList;
    @ApiModelProperty(value = "工具类型")
    private List<String> toolTypeList;
    @ApiModelProperty(value = "工具")
    private List<String> toolIdList;
    @ApiModelProperty(value = "工作单元类型")
    private List<String> workcellTypeList;
    @ApiModelProperty(value = "工作单元")
    private List<String> workcellIdList;
    @ApiModelProperty(value = "此操作的缺省不合格代码。操作员针对 EO 编号记录不合格项时，系统会提供此代码。")
    private List<String> defaultNcIdList;
    @ApiModelProperty(value = "路线步骤的特殊指令REQUIRED_TIME_IN_PROCESS")
    private String standardSpecialIntroduction;

    public List<String> getSiteIdList() {
        return siteIdList;
    }

    public void setSiteIdList(List<String> siteIdList) {
        this.siteIdList = siteIdList;
    }

    public List<String> getOperationNameList() {
        return operationNameList;
    }

    public void setOperationNameList(List<String> operationNameList) {
        this.operationNameList = operationNameList;
    }

    public List<String> getRevisionList() {
        return revisionList;
    }

    public void setRevisionList(List<String> revisionList) {
        this.revisionList = revisionList;
    }

    public String getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(String currentFlag) {
        this.currentFlag = currentFlag;
    }

    public Date getDateFrom() {
        if (dateFrom != null) {
            return (Date) dateFrom.clone();
        } else {
            return null;
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
        if (dateTo != null) {
            return (Date) dateTo.clone();
        } else {
            return null;
        }
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    public List<String> getDescriptionList() {
        return descriptionList;
    }

    public void setDescriptionList(List<String> descriptionList) {
        this.descriptionList = descriptionList;
    }

    public List<String> getOperationStatusList() {
        return operationStatusList;
    }

    public void setOperationStatusList(List<String> operationStatusList) {
        this.operationStatusList = operationStatusList;
    }

    public List<String> getOperationTypeList() {
        return operationTypeList;
    }

    public void setOperationTypeList(List<String> operationTypeList) {
        this.operationTypeList = operationTypeList;
    }

    public List<String> getSpecialRouterIdList() {
        return specialRouterIdList;
    }

    public void setSpecialRouterIdList(List<String> specialRouterIdList) {
        this.specialRouterIdList = specialRouterIdList;
    }

    public List<String> getToolTypeList() {
        return toolTypeList;
    }

    public void setToolTypeList(List<String> toolTypeList) {
        this.toolTypeList = toolTypeList;
    }

    public List<String> getToolIdList() {
        return toolIdList;
    }

    public void setToolIdList(List<String> toolIdList) {
        this.toolIdList = toolIdList;
    }

    public List<String> getWorkcellTypeList() {
        return workcellTypeList;
    }

    public void setWorkcellTypeList(List<String> workcellTypeList) {
        this.workcellTypeList = workcellTypeList;
    }

    public List<String> getWorkcellIdList() {
        return workcellIdList;
    }

    public void setWorkcellIdList(List<String> workcellIdList) {
        this.workcellIdList = workcellIdList;
    }

    public List<String> getDefaultNcIdList() {
        return defaultNcIdList;
    }

    public void setDefaultNcIdList(List<String> defaultNcIdList) {
        this.defaultNcIdList = defaultNcIdList;
    }

    public String getStandardSpecialIntroduction() {
        return standardSpecialIntroduction;
    }

    public void setStandardSpecialIntroduction(String standardSpecialIntroduction) {
        this.standardSpecialIntroduction = standardSpecialIntroduction;
    }
}
