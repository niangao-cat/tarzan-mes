package tarzan.dispatch.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-06 17:23
 **/
public class MtEoDispatchPlatformDTO11 implements Serializable {
    private static final long serialVersionUID = -9170561614049830811L;
    @ApiModelProperty(value = "生产线ID", required = true)
    private String prodLineId;
    @ApiModelProperty(value = "用户默认站点ID", required = true)
    private String defaultSiteId;

    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getDefaultSiteId() {
        return defaultSiteId;
    }

    public void setDefaultSiteId(String defaultSiteId) {
        this.defaultSiteId = defaultSiteId;
    }
}
