package tarzan.order.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/9/16 19:15
 * @Description:
 */
public class MtEoRouterVO2 implements Serializable {
    private static final long serialVersionUID = 3955816934527053050L;

    /**
     * 主键ID
     */
    private String eoRouterId;
    /**
     * 主键历史ID
     */
    private String eoRouterHisId;

    public String getEoRouterId() {
        return eoRouterId;
    }

    public void setEoRouterId(String eoRouterId) {
        this.eoRouterId = eoRouterId;
    }

    public String getEoRouterHisId() {
        return eoRouterHisId;
    }

    public void setEoRouterHisId(String eoRouterHisId) {
        this.eoRouterHisId = eoRouterHisId;
    }
}
