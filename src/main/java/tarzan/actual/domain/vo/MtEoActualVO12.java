package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/11/26 9:49
 * @Author: ${yiyang.xie}
 */
public class MtEoActualVO12 implements Serializable {
    private static final long serialVersionUID = -1096020900439063241L;
    @ApiModelProperty("执行作业实绩ID")
    private String eoActualId;
    @ApiModelProperty("执行作业实绩历史ID")
    private String eoActualHisId;

    public String getEoActualId() {
        return eoActualId;
    }

    public void setEoActualId(String eoActualId) {
        this.eoActualId = eoActualId;
    }

    public String getEoActualHisId() {
        return eoActualHisId;
    }

    public void setEoActualHisId(String eoActualHisId) {
        this.eoActualHisId = eoActualHisId;
    }
}
