package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/3/22 13:53
 */
public class MtAssembleProcessActualVO2 implements Serializable {
    private static final long serialVersionUID = 1714085340081427916L;
    private String bomComponentId;
    private String bomComponentType;
    private String routerStepId;
    private String substituteFlag;
    private String eoId; // 执行作业ID

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomComponentType() {
        return bomComponentType;
    }

    public void setBomComponentType(String bomComponentType) {
        this.bomComponentType = bomComponentType;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }
}
