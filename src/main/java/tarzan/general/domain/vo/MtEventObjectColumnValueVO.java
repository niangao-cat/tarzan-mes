package tarzan.general.domain.vo;

import java.io.Serializable;

public class MtEventObjectColumnValueVO implements Serializable {
    private static final long serialVersionUID = -4162533893131868605L;

    private Integer index;
    private String title;
    private String value;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
