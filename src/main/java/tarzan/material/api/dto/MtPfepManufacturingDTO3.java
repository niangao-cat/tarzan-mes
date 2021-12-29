package tarzan.material.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtPfepManufacturingDTO3 implements Serializable {
    private static final long serialVersionUID = 5059796053980755839L;

    @ApiModelProperty(value = "主键ID")
    private String kid;
    @ApiModelProperty(value = "pfep类型(material/category)")
    private String type;

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
