package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtEventRequestVO3 implements Serializable {

	private static final long serialVersionUID = -2253379641283785683L;
	@ApiModelProperty("事件请求ID")
    private String eventRequestId;
    @ApiModelProperty("事件请求类型ID")
    private String requestTypeId;
    @ApiModelProperty("事件请求创建人")
    private Long requestBy;
    @ApiModelProperty("事件请求时间从")
    private Date requestTimeFrom;
    @ApiModelProperty("事件请求时间至")
    private Date requestTimeTo;

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(String requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    public Long getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(Long requestBy) {
        this.requestBy = requestBy;
    }

    public Date getRequestTimeFrom() {
        if (requestTimeFrom != null) {
            return (Date) requestTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setRequestTimeFrom(Date requestTimeFrom) {
        if (requestTimeFrom == null) {
            this.requestTimeFrom = null;
        } else {
            this.requestTimeFrom = (Date) requestTimeFrom.clone();
        }
    }

    public Date getRequestTimeTo() {
        if (requestTimeTo != null) {
            return (Date) requestTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setRequestTimeTo(Date requestTimeTo) {
        if (requestTimeTo == null) {
            this.requestTimeTo = null;
        } else {
            this.requestTimeTo = (Date) requestTimeTo.clone();
        }
    }

}
