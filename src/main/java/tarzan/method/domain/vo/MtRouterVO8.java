package tarzan.method.domain.vo;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/4/29 14:44
 */
public class MtRouterVO8 implements Serializable {
    private static final long serialVersionUID = 6631569534606740621L;

    private String eoId;
    private String routerId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public MtRouterVO8() {
    }

    public MtRouterVO8(String eoId, String routerId) {
        this.eoId = eoId;
        this.routerId = routerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MtRouterVO8 that = (MtRouterVO8) o;
        return Objects.equal(eoId, that.eoId) &&
                Objects.equal(routerId, that.routerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(eoId, routerId);
    }
}
