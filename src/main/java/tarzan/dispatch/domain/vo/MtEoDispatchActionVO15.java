package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtEoDispatchActionVO15 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1740626868677102012L;

    private Long sequence;

    private MtEoDispatchActionVO14 adjustObject;// 任何需要排序的对象

    public Long getSequence() {
        return sequence;
    }

    public MtEoDispatchActionVO14 getAdjustObject() {
        return adjustObject;
    }

    public void setAdjustObject(MtEoDispatchActionVO14 adjustObject) {
        this.adjustObject = adjustObject;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }


}
