package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/9/17 14:36
 */
public class MtWorkOrderActualVO5 implements Serializable {
    private static final long serialVersionUID = -6008940508455130332L;
    /**
     * 生产指令实绩Id
     */
    private String workOrderActualId;
    /**
     * 生产指令实绩历史Id
     */
    private String workOrderActualHisId;

    public String getWorkOrderActualId() {
        return workOrderActualId;
    }

    public void setWorkOrderActualId(String workOrderActualId) {
        this.workOrderActualId = workOrderActualId;
    }

    public String getWorkOrderActualHisId() {
        return workOrderActualHisId;
    }

    public void setWorkOrderActualHisId(String workOrderActualHisId) {
        this.workOrderActualHisId = workOrderActualHisId;
    }
}
