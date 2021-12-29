package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/12/17 10:39
 * @Author: ${yiyang.xie}
 */
public class MtEoStepActualVO38 implements Serializable {
    private static final long serialVersionUID = -3882455626785951963L;

    @ApiModelProperty("执行作业Id")
    private String eoId;
    
    @ApiModelProperty("执行作业Id")
    private String routerId;

    @ApiModelProperty("执行作业步骤唯一标识")
    private List<String> eoStepActualId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public List<String> getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(List<String> eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }
    
}
