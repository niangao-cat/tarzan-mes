package tarzan.method.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/4/27 18:28
 * @Description:
 */
public class MtRouterVO6 implements Serializable {
    private static final long serialVersionUID = 5503488755633159624L;

    private String sourceRouterId; // 工艺路线
    private String targetRouterId; // 工艺路线

    public String getSourceRouterId() {
        return sourceRouterId;
    }

    public void setSourceRouterId(String sourceRouterId) {
        this.sourceRouterId = sourceRouterId;
    }

    public String getTargetRouterId() {
        return targetRouterId;
    }

    public void setTargetRouterId(String targetRouterId) {
        this.targetRouterId = targetRouterId;
    }
}
