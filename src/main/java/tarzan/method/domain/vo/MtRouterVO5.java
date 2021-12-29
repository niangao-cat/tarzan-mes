package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtRouterVO5 implements Serializable {
    private static final long serialVersionUID = 3356042275120001845L;

    private String routerId; // 工艺路线ID
    private String revision; // 版本
    private String routerType; // 类型

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getRouterType() {
        return routerType;
    }

    public void setRouterType(String routerType) {
        this.routerType = routerType;
    }
}
