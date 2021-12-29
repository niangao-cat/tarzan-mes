package tarzan.iface.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/6/26 10:20
 */
public class MtMaterialBasisVO implements Serializable {

    private static final long serialVersionUID = -1612502297038520902L;
    
    @ApiModelProperty("物料站点 ID ")
    private String siteId;
    @ApiModelProperty("物料 ID ")
    private String materialId;
    
    public String getSiteId() {
        return siteId;
    }
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    public String getMaterialId() {
        return materialId;
    }
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
    
}