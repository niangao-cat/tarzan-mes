package tarzan.method.api.dto;

import java.io.Serializable;

public class MtAssemblePointDTO6 implements Serializable {
    private static final long serialVersionUID = -5315732548005491584L;

    private String assembleGroupId;
    private String sortBy;

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
