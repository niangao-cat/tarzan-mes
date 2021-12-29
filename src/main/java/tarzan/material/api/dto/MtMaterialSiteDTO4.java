package tarzan.material.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author YUAN.YUAN
 */
public class MtMaterialSiteDTO4 implements Serializable {
    private static final long serialVersionUID = 7815179652315654009L;

    @ApiModelProperty("主键，标识唯一一条记录")
    private String materialSiteId;
    @ApiModelProperty(value = "物料ID", required = true)
    private String materialId;
    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;
    @ApiModelProperty(value = "有效性", required = true)
    private String enableFlag;

    /**
     * @return 主键，标识唯一一条记录
     */
    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    /**
     * @return 物料，来自物料表，对应唯一物料
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 站点，来自站点表，对应唯一站点
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

}
