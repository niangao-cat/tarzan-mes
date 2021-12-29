package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/11/2 19:15
 * @Description:
 */
public class MtRouterOpComponentVO6 extends MtRouterOpComponentVO7 implements Serializable {
    private static final long serialVersionUID = 6690311154627365927L;

    @ApiModelProperty("工艺路线ID")
    private String routerId;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("装配清单组件ID集合")
    private List<String> bomComponentIds;

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public List<String> getBomComponentIds() {
        return bomComponentIds;
    }

    public void setBomComponentIds(List<String> bomComponentIds) {
        this.bomComponentIds = bomComponentIds;
    }
}
