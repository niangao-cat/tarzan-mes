package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2019/9/17 17:51
 * @Author: {yiyang.xie@hand-china.com}
 */
public class MtEoStepWorkcellActualVO5 implements Serializable {
    private static final long serialVersionUID = -6115215426438011824L;
    private String eoStepWorkcellActualId; //主键
    private String eoStepWorkcellActualHisId; //步骤实绩明细历史ID

    public String getEoStepWorkcellActualId() {
        return eoStepWorkcellActualId;
    }

    public void setEoStepWorkcellActualId(String eoStepWorkcellActualId) {
        this.eoStepWorkcellActualId = eoStepWorkcellActualId;
    }

    public String getEoStepWorkcellActualHisId() {
        return eoStepWorkcellActualHisId;
    }

    public void setEoStepWorkcellActualHisId(String eoStepWorkcellActualHisId) {
        this.eoStepWorkcellActualHisId = eoStepWorkcellActualHisId;
    }
}