package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtWorkOrderVO1 implements Serializable {

    private static final long serialVersionUID = 1892430929851911360L;
    private List<String> workOrderIds;
    private String property;
    private String sortBy;

    public List<String> getWorkOrderIds() {
        return workOrderIds;
    }

    public void setWorkOrderIds(List<String> workOrderIds) {
        this.workOrderIds = workOrderIds;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

}
