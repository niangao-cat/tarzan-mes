package tarzan.inventory.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/4/4 9:06
 */
public class MtContainerVO5 implements Serializable {

    private static final long serialVersionUID = 7430170501636557223L;

    private Long nextLoactionRow;
    private Long nextLocationColumn;

    public Long getNextLoactionRow() {
        return nextLoactionRow;
    }

    public void setNextLoactionRow(Long nextLoactionRow) {
        this.nextLoactionRow = nextLoactionRow;
    }

    public Long getNextLocationColumn() {
        return nextLocationColumn;
    }

    public void setNextLocationColumn(Long nextLocationColumn) {
        this.nextLocationColumn = nextLocationColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MtContainerVO5 that = (MtContainerVO5) o;

        if (getNextLoactionRow() != null ? !getNextLoactionRow().equals(that.getNextLoactionRow())
                        : that.getNextLoactionRow() != null) {
            return false;
        }
        return getNextLocationColumn() != null ? getNextLocationColumn().equals(that.getNextLocationColumn())
                        : that.getNextLocationColumn() == null;
    }

    @Override
    public int hashCode() {
        int result = getNextLoactionRow() != null ? getNextLoactionRow().hashCode() : 0;
        result = 31 * result + (getNextLocationColumn() != null ? getNextLocationColumn().hashCode() : 0);
        return result;
    }
}
