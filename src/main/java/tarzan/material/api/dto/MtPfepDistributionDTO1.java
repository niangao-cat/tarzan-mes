package tarzan.material.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * TODO 前台查询 DTO
 *
 * @author jin.xu@hand-china.com
 * @date 2020/2/11 14:15
 */
public class MtPfepDistributionDTO1 implements Serializable {
    private static final long serialVersionUID = 5834388533182686784L;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "生产站点ID/物料站点ID/分类集站点ID")
    private String siteId;

    @ApiModelProperty(value = "物料类别ID")
    private String materialCategoryId;

    @ApiModelProperty(value = "工作单元ID")
    private String workcellId;

    @ApiModelProperty(value = "配送路线ID")
    private String areaId;

    @ApiModelProperty(value = "配送库位ID")
    private String areaLocatorId;

    @ApiModelProperty(value = "生效标记")
    private String enableFlag;

    @ApiModelProperty(value = "线边库位ID")
    private String locatorId;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }

    public String getAreaLocatorId() {
        return areaLocatorId;
    }

    public void setAreaLocatorId(String areaLocatorId) {
        this.areaLocatorId = areaLocatorId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }
}
