package tarzan.actual.domain.vo;

import java.io.Serializable;

import tarzan.actual.domain.entity.MtEoRouterActual;

/**
 * @author peng.yuan
 * @ClassName MtEoRouterActualVO35
 * @description
 * @date 2019年11月26日 15:26
 */
public class MtEoRouterActualVO35 extends MtEoRouterActual implements Serializable {
    private static final long serialVersionUID = 3426312490115492357L;
    private String eoStepActualId;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }
}
