package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtEventRequestVO4 implements Serializable {

	private static final long serialVersionUID = -8344585835929977768L;
	@ApiModelProperty("事件请求ID")
    private String eventRequestId;
    @ApiModelProperty("事件请求类型ID")
    private String requestTypeId;
    @ApiModelProperty("事件请求类型编码")
    private String requestTypeCode;
    @ApiModelProperty("事件请求类型描述")
    private String requestTypeDescription;
    @ApiModelProperty("事件请求创建人")
    private Long requestBy;
	@ApiModelProperty("事件请求时间")
    private Date requestTime;

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

    public String getRequestTypeCode() {
        return requestTypeCode;
    }

    public void setRequestTypeCode(String requestTypeCode) {
        this.requestTypeCode = requestTypeCode;
    }

    public String getRequestTypeDescription() {
        return requestTypeDescription;
    }

    public void setRequestTypeDescription(String requestTypeDescription) {
        this.requestTypeDescription = requestTypeDescription;
    }

    public Long getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(Long requestBy) {
        this.requestBy = requestBy;
    }

    public Date getRequestTime() {
        if (requestTime != null) {
            return (Date) requestTime.clone();
        } else {
            return null;
        }
    }

    public void setRequestTime(Date requestTime) {
        if (requestTime == null) {
            this.requestTime = null;
        } else {
            this.requestTime = (Date) requestTime.clone();
        }
    }

}
