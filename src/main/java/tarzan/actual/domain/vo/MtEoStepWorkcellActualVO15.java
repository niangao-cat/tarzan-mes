package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2020-10-26
 * @Author: Tangxiao
 */
public class MtEoStepWorkcellActualVO15 implements Serializable {

    private static final long serialVersionUID = 2975240523842148288L;
    private String eoStepWorkcellId;// 执行作业工作单元实绩ID
    private String eoStepWorkcellHisId;// 执行作业工作单元实绩历史ID
    private String eoStepActualId;// 执行作业步骤实绩ID
    private String workcellId;// 工作单元ID

    public String getEoStepWorkcellId() {
        return eoStepWorkcellId;
    }

    public void setEoStepWorkcellId(String eoStepWorkcellId) {
        this.eoStepWorkcellId = eoStepWorkcellId;
    }

    public String getEoStepWorkcellHisId() {
        return eoStepWorkcellHisId;
    }

    public void setEoStepWorkcellHisId(String eoStepWorkcellHisId) {
        this.eoStepWorkcellHisId = eoStepWorkcellHisId;
    }

    public MtEoStepWorkcellActualVO15() {}

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public MtEoStepWorkcellActualVO15(String eoStepWorkcellId, String eoStepWorkcellHisId, String eoStepActualId,
                    String workcellId) {
        this.eoStepWorkcellId = eoStepWorkcellId;
        this.eoStepWorkcellHisId = eoStepWorkcellHisId;
        this.eoStepActualId = eoStepActualId;
        this.workcellId = workcellId;
    }
}


