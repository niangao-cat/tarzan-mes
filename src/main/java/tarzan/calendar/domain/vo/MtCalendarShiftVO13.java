package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtCalendarShiftVO13
 *
 * @author: {xieyiyang}
 * @date: 2020/2/10 17:46
 * @description:
 */
public class MtCalendarShiftVO13 implements Serializable {
    private static final long serialVersionUID = 316241007264906200L;

    @ApiModelProperty("业务组织实体类型")
    private String organizationType;
    @ApiModelProperty("业务组织实体ID")
    private String organizationId;
    @ApiModelProperty("时间")
    private Date date;

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

    public Date getDate() {
        if (date != null) {
            return (Date) date.clone();
        } else {
            return null;
        }
    }

    public void setDate(Date date) {
        if (date == null) {
            this.date = null;
        } else {
            this.date = (Date) date.clone();
        }
    }
}
