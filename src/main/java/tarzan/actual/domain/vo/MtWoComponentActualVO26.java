package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by HP on 2019/3/15.
 */
public class MtWoComponentActualVO26 implements Serializable {


    private static final long serialVersionUID = 696927248642642862L;
    private Date minActualFirstTime;

    private Date maxActualLastTime;

    public Date getMinActualFirstTime() {
        return minActualFirstTime == null ? null : (Date) minActualFirstTime.clone();
    }

    public void setMinActualFirstTime(Date minActualFirstTime) {
        this.minActualFirstTime = minActualFirstTime == null ? null : (Date) minActualFirstTime.clone();
    }

    public Date getMaxActualLastTime() {
        return maxActualLastTime == null ? null : (Date) maxActualLastTime.clone();
    }

    public void setMaxActualLastTime(Date maxActualLastTime) {
        this.maxActualLastTime = maxActualLastTime == null ? null : (Date) maxActualLastTime.clone();
    }
}
