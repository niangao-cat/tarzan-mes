package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtAssemblePointActualVO7
 * @description
 * @date 2019年09月17日 15:27
 */
public class MtAssemblePointActualVO7 implements Serializable {

    private static final long serialVersionUID = -2000173125163581266L;

    private String assemblePointActualId;//装配点实绩ID

    private String assemblePointActualHisId;//历史装配点实绩ID

    public String getAssemblePointActualId() {
        return assemblePointActualId;
    }

    public void setAssemblePointActualId(String assemblePointActualId) {
        this.assemblePointActualId = assemblePointActualId;
    }

    public String getAssemblePointActualHisId() {
        return assemblePointActualHisId;
    }

    public void setAssemblePointActualHisId(String assemblePointActualHisId) {
        this.assemblePointActualHisId = assemblePointActualHisId;
    }
}
