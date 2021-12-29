package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Leeloing
 * @date 2019/9/16 17:35
 */
public class MtEoComponentActualVO24 implements Serializable {

    private static final long         serialVersionUID = 782412228692264477L;
    private              List<String> bomComponentId;
    private              List<String> bomComponentHisId;

    public List<String> getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(List<String> bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public List<String> getBomComponentHisId() {
        return bomComponentHisId;
    }

    public void setBomComponentHisId(List<String> bomComponentHisId) {
        this.bomComponentHisId = bomComponentHisId;
    }
}
