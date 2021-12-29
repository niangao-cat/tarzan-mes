package tarzan.method.domain.vo;

import java.io.Serializable;

import tarzan.method.domain.entity.MtRouterOperationComponent;

/**
 * @author Leeloing
 * @date 2019/12/12 2:56 下午
 */
public class MtRouterOpComponentVO4 extends MtRouterOperationComponent implements Serializable {
    private static final long serialVersionUID = -6742108529129318734L;
    private String routerId;

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }
}
