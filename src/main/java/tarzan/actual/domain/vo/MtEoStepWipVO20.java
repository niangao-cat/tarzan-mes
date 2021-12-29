package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/11/3 19:51
 */
public class MtEoStepWipVO20 implements Serializable {
    private static final long serialVersionUID = -5948508378184858901L;

    @ApiModelProperty("工艺路线实绩唯一标识")
    private String eoRouterActualId;

    @ApiModelProperty("步骤唯一标识")
    private String routerStepId;

    @ApiModelProperty("返回值")
    private List<MtEoStepWipVO22> result;

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public List<MtEoStepWipVO22> getResult() {
        return result;
    }

    public void setResult(List<MtEoStepWipVO22> result) {
        this.result = result;
    }
}
