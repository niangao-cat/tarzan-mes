package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/9/17 14:20
 */
public class MtAssembleProcessActualVO6 implements Serializable {
    private static final long serialVersionUID = -1255871278339345588L;
    /**
     * 装配过程实绩Id
     */
    private String assembleProcessActualId;
    /**
     * 装配确认实绩Id
     */
    private String assembleConfirmActualId;

    public String getAssembleProcessActualId() {
        return assembleProcessActualId;
    }

    public void setAssembleProcessActualId(String assembleProcessActualId) {
        this.assembleProcessActualId = assembleProcessActualId;
    }

    public String getAssembleConfirmActualId() {
        return assembleConfirmActualId;
    }

    public void setAssembleConfirmActualId(String assembleConfirmActualId) {
        this.assembleConfirmActualId = assembleConfirmActualId;
    }
}
