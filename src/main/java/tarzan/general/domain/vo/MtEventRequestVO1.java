package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtEventRequestVO1 implements Serializable {
    private static final long serialVersionUID = -5436955092705366353L;

    private String eventRequestId;
    private String requestTypeCode;
    private Long requestBy;
    private Date requestTime;

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getRequestTypeCode() {
        return requestTypeCode;
    }

    public void setRequestTypeCode(String requestTypeCode) {
        this.requestTypeCode = requestTypeCode;
    }

    public Long getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(Long requestBy) {
        this.requestBy = requestBy;
    }

    public Date getRequestTime() {
        if (requestTime == null) {
            return null;
        }
        return (Date) requestTime.clone();
    }

    public void setRequestTime(Date requestTime) {
        if (requestTime == null) {
            this.requestTime = null;
        } else {
            this.requestTime = (Date) requestTime.clone();
        }
    }
}
