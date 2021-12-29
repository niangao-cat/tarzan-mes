package tarzan.dispatch.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/18 5:56 下午
 */
public class MtEoDispatchActionVO22 implements Serializable {
    private static final long serialVersionUID = 1204669084895043074L;
    @ApiModelProperty("执行作业")
    private String eoId;
    @ApiModelProperty("调度策略")
    private String dispatchStrategy;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getDispatchStrategy() {
        return dispatchStrategy;
    }

    public void setDispatchStrategy(String dispatchStrategy) {
        this.dispatchStrategy = dispatchStrategy;
    }

}
