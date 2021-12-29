package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/5/27 17:27
 */
public class MtEoRouterActualVO26 implements Serializable {

    private static final long serialVersionUID = -1393475311693393304L;

    private String currentRouterId;
    private String eoRouterComletedFlag;

    public String getCurrentRouterId() {
        return currentRouterId;
    }

    public void setCurrentRouterId(String currentRouterId) {
        this.currentRouterId = currentRouterId;
    }

    public String getEoRouterComletedFlag() {
        return eoRouterComletedFlag;
    }

    public void setEoRouterComletedFlag(String eoRouterComletedFlag) {
        this.eoRouterComletedFlag = eoRouterComletedFlag;
    }
}
