package tarzan.material.api.dto;

import java.io.Serializable;

public class MtMaterialCategoryDTO implements Serializable {

    private static final long serialVersionUID = -2388899535313101438L;

    private String categoryCode;

    private String description;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
