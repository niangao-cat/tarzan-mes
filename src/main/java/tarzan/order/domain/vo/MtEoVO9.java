package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtEoVO9 implements Serializable {

    private static final long serialVersionUID = -2881611537554122490L;
    private List<String> eoIds;
    private String property;
    private String sortBy;

    public List<String> getEoIds() {
        return eoIds;
    }

    public void setEoIds(List<String> eoIds) {
        this.eoIds = eoIds;
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
