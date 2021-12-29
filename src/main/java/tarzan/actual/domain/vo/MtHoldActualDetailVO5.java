package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtHoldActualDetailVO5 implements Serializable {


    private static final long serialVersionUID = 8312478465978544645L;

    private Date expiredReleaseTime;// 保留到期释放时间

    private Date holdTime;// 保留时间

    public Date getExpiredReleaseTime() {
        if (expiredReleaseTime != null) {
            return (Date) expiredReleaseTime.clone();
        } else {
            return null;
        }
    }

    public void setExpiredReleaseTime(Date expiredReleaseTime) {
        if (expiredReleaseTime == null) {
            this.expiredReleaseTime = null;
        } else {
            this.expiredReleaseTime = (Date) expiredReleaseTime.clone();
        }
    }

    public Date getHoldTime() {
        if (holdTime != null) {
            return (Date) holdTime.clone();
        } else {
            return null;
        }
    }

    public void setHoldTime(Date holdTime) {
        if (holdTime == null) {
            this.holdTime = null;
        } else {
            this.holdTime = (Date) holdTime.clone();
        }
    }

}
