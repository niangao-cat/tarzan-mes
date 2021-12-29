package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtEventRequestVO2 implements Serializable {
    private static final long serialVersionUID = -2962238904847809458L;

    private String requestTypeCode;
    private Long requestBy;
    private Date requestStartTime;
    private Date requestEndTime;

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

    public Date getRequestStartTime() {
        if (requestStartTime == null) {
            return null;
        }
        return (Date) requestStartTime.clone();
    }

    public void setRequestStartTime(Date requestStartTime) {
        if (requestStartTime == null) {
            this.requestStartTime = null;
        } else {
            this.requestStartTime = (Date) requestStartTime.clone();
        }
    }

    public Date getRequestEndTime() {
        if (requestEndTime == null) {
            return null;
        }
        return (Date) requestEndTime.clone();
    }

    public void setRequestEndTime(Date requestEndTime) {
        if (requestEndTime == null) {
            this.requestEndTime = null;
        } else {
            this.requestEndTime = (Date) requestEndTime.clone();
        }
    }
}
