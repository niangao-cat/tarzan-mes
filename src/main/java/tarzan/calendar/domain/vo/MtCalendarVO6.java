package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/17 6:59 下午
 */
public class MtCalendarVO6 implements Serializable {
    private static final long serialVersionUID = -6847848098366600319L;
    @ApiModelProperty("组织类型")
    private String organizationType;
    @ApiModelProperty("组织ID列表")
    private List<String> organizationIdList;
    @ApiModelProperty("站点类型")
    private String siteType;
    @ApiModelProperty("日历类型")
    private String calendarType;

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public List<String> getOrganizationIdList() {
        return organizationIdList;
    }

    public void setOrganizationIdList(List<String> organizationIdList) {
        this.organizationIdList = organizationIdList;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(String calendarType) {
        this.calendarType = calendarType;
    }
}
