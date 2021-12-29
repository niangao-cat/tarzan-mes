package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: xiao tang
 * @Date: 2020/02/14 15:36
 * @Description:
 */
public class MtRouterStepVO15 implements Serializable {
    
    private static final long serialVersionUID = -3209912398905837751L;
    
    
    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;
    
    @ApiModelProperty(value = "工艺路线ID")
    private List<MtRouterStepVO5> routerStepList;

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public List<MtRouterStepVO5> getRouterStepList() {
        return routerStepList;
    }

    public void setRouterStepList(List<MtRouterStepVO5> routerStepList) {
        this.routerStepList = routerStepList;
    }
    
}
