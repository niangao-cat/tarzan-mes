package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtEoDispatchActionVO13 implements Serializable {

    private static final long serialVersionUID = 7334229366485863902L;

    private Long sequence;

    private Long priority;

    private MtEoDispatchActionVO14 adjustObject;// 任何需要排序的对象

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public MtEoDispatchActionVO14 getAdjustObject() {
        return adjustObject;
    }

    public void setAdjustObject(MtEoDispatchActionVO14 adjustObject) {
        this.adjustObject = adjustObject;
    }
}
