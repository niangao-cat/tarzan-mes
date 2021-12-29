package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtRouterStepGroupStepVO implements Serializable {

    private static final long serialVersionUID = 3697547867323120711L;

    @ApiModelProperty("步骤组步骤表主键Id")
    private String routerStepGroupStepId;
    @ApiModelProperty("步骤组Id")
    private String routerStepGroupId;
    @ApiModelProperty("步骤组类型")
    private String routerStepGroupType;
    @ApiModelProperty("步骤组对应的步骤Id")
    private String groupRouterStepId;
    @ApiModelProperty("传入参数步骤Id")
    private String routerStepId;

    public String getRouterStepGroupStepId() {
        return routerStepGroupStepId;
    }

    public void setRouterStepGroupStepId(String routerStepGroupStepId) {
        this.routerStepGroupStepId = routerStepGroupStepId;
    }

    public String getRouterStepGroupId() {
        return routerStepGroupId;
    }

    public void setRouterStepGroupId(String routerStepGroupId) {
        this.routerStepGroupId = routerStepGroupId;
    }

    public String getRouterStepGroupType() {
        return routerStepGroupType;
    }

    public void setRouterStepGroupType(String routerStepGroupType) {
        this.routerStepGroupType = routerStepGroupType;
    }

    public String getGroupRouterStepId() {
        return groupRouterStepId;
    }

    public void setGroupRouterStepId(String groupRouterStepId) {
        this.groupRouterStepId = groupRouterStepId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    @Override
    public String toString() {
        return "MtRouterStepGroupStepVO{" + "routerStepGroupStepId=" + routerStepGroupStepId + ", routerStepGroupId="
                + routerStepGroupId + ", groupRouterStepId=" + groupRouterStepId + '}';
    }

}
