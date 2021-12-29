package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtEoDispatchActionVO12 implements Serializable {


    private static final long serialVersionUID = 3533651933340489522L;

    private Long sequence;

    private Long priority;

    private Object adjustObject;// 任何需要排序的对象

    private String sortBy;

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

    public Object getAdjustObject() {
        return adjustObject;
    }

    public void setAdjustObject(Object adjustObject) {
        this.adjustObject = adjustObject;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
