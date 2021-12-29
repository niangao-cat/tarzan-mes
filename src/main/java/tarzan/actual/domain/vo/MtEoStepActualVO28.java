package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/9/16 19:38
 * @Description:
 */
public class MtEoStepActualVO28 implements Serializable {

    private static final long serialVersionUID = 4724396977840294757L;
    /**
     * 执行作业步骤实绩唯一标识
     */
    private String eoStepActualId;
    /**
     * 执行作业步骤实绩历史ID
     */
    private String eoStepActualHisId;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getEoStepActualHisId() {
        return eoStepActualHisId;
    }

    public void setEoStepActualHisId(String eoStepActualHisId) {
        this.eoStepActualHisId = eoStepActualHisId;
    }
}
