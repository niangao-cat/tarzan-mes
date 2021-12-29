package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-17 11:34
 **/
public class MtCalendarOrgRelVO3 implements Serializable {
    private static final long serialVersionUID = -2561275840968982757L;
    @ApiModelProperty("日历组织关系唯一标识")
    private List<String> calendarOrgRelIdList;

    @ApiModelProperty("日历唯一标识")
    private List<String> calendarIdList;

    @ApiModelProperty("业务组织实体")
    private List<String> organizationIdList;

    @ApiModelProperty("业务组织实体类型")
    private List<String> organizationTypeList;

    @ApiModelProperty("是否有效")
    private String enableFlag;

    public List<String> getCalendarOrgRelIdList() {
        return calendarOrgRelIdList;
    }

    public void setCalendarOrgRelIdList(List<String> calendarOrgRelIdList) {
        this.calendarOrgRelIdList = calendarOrgRelIdList;
    }

    public List<String> getCalendarIdList() {
        return calendarIdList;
    }

    public void setCalendarIdList(List<String> calendarIdList) {
        this.calendarIdList = calendarIdList;
    }

    public List<String> getOrganizationIdList() {
        return organizationIdList;
    }

    public void setOrganizationIdList(List<String> organizationIdList) {
        this.organizationIdList = organizationIdList;
    }

    public List<String> getOrganizationTypeList() {
        return organizationTypeList;
    }

    public void setOrganizationTypeList(List<String> organizationTypeList) {
        this.organizationTypeList = organizationTypeList;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
