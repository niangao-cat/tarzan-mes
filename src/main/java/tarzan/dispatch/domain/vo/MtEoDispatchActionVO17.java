package tarzan.dispatch.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/9/17 10:55
 */
public class MtEoDispatchActionVO17 implements Serializable {

    private static final long serialVersionUID = -5482986624746668461L;
    /**
     * 主键ID
     */
    private String eoDispatchActionId;
    /**
     * 主键历史ID
     */
    private String eoDispatchHistoryId;

    public String getEoDispatchActionId() {
        return eoDispatchActionId;
    }

    public void setEoDispatchActionId(String eoDispatchActionId) {
        this.eoDispatchActionId = eoDispatchActionId;
    }

    public String getEoDispatchHistoryId() {
        return eoDispatchHistoryId;
    }

    public void setEoDispatchHistoryId(String eoDispatchHistoryId) {
        this.eoDispatchHistoryId = eoDispatchHistoryId;
    }
}
