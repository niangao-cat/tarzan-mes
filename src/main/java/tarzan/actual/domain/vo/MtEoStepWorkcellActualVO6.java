package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2019/9/17 20:08
 * @Author: {yiyang.xie@hand-china.com}
 */
public class MtEoStepWorkcellActualVO6 implements Serializable {
    private static final long serialVersionUID = 8805999045789145343L;
    private String eoStepWorkcellId;//执行作业工作单元实绩ID
    private String eoStepWorkcellHisId;//执行作业工作单元实绩历史ID

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
}