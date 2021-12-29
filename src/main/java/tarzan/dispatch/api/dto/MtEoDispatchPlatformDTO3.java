package tarzan.dispatch.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-04 10:40
 **/
public class MtEoDispatchPlatformDTO3 implements Serializable {
    private static final long serialVersionUID = 8404356383712418876L;
    @ApiModelProperty(value = "主键ID ,表示唯一一条记录", required = true)
    private String prodLineId;
    @ApiModelProperty(value = "工艺唯一标识", required = true)
    private String operationId;
    @ApiModelProperty(value = "用户默认站点ID", required = true)
    private String defaultSiteId;

    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getDefaultSiteId() {
        return defaultSiteId;
    }

    public void setDefaultSiteId(String defaultSiteId) {
        this.defaultSiteId = defaultSiteId;
    }
}
