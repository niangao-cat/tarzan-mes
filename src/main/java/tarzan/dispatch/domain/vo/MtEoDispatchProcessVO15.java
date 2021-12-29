package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import tarzan.actual.domain.vo.MtEoStepActualVO37;

/**
 * @Author: chuang.yang
 * @Date: 2019/12/19 17:20
 * @Description:
 */
public class MtEoDispatchProcessVO15 implements Serializable {
    private static final long serialVersionUID = 3253143943282757444L;

    List<MtEoStepActualVO37> eoMessageList;

    @ApiModelProperty("工作单元ID")
    private String workcellId;

    public List<MtEoStepActualVO37> getEoMessageList() {
        return eoMessageList;
    }

    public void setEoMessageList(List<MtEoStepActualVO37> eoMessageList) {
        this.eoMessageList = eoMessageList;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }
}
