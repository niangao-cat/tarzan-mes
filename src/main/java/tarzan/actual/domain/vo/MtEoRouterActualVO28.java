package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/9/16 17:48
 * @Description:
 */
public class MtEoRouterActualVO28 implements Serializable {

    private static final long serialVersionUID = -9139535918035232660L;

    @ApiModelProperty("执行作业实绩Id")
    private String eoRouterActualId;
    @ApiModelProperty("执行作业实绩历史ID")
    private String eoRouterActualHisId;

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getEoRouterActualHisId() {
        return eoRouterActualHisId;
    }

    public void setEoRouterActualHisId(String eoRouterActualHisId) {
        this.eoRouterActualHisId = eoRouterActualHisId;
    }
}
