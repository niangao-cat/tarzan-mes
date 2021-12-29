package tarzan.order.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author xiao.tang02@hand-china.com 2019年12月20日 下午6:38:45
 *
 */
public class MtWorkOrderVO51 implements Serializable {

    private static final long serialVersionUID = 7233194917239499114L;

    @ApiModelProperty(value = "执行作业ID")
    private String eoId;
    @ApiModelProperty(value = "EO编码")
    private String eoNum;
    @ApiModelProperty(value = "EO状态")
    private String status;
    @ApiModelProperty(value = "描述")
    private String statusDesc;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    @ApiModelProperty(value = "站点名称")
    private String siteName;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "EO数量")
    private Double qty;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "单位名称")
    private String uomName;
    @ApiModelProperty(value = "EO类型")
    private String eoType;
    @ApiModelProperty(value = "EO类型描述")
    private String eoTypeDesc;
    @ApiModelProperty(value = "生产线ID")
    private String productionLineId;
    @ApiModelProperty(value = "生产线编码")
    private String productionLineCode;
    @ApiModelProperty(value = "生产线名称")
    private String productionLineName;
    @ApiModelProperty(value = "计划开始时间")
    private Date planStartTime;
    @ApiModelProperty(value = "计划结束时间")
    private Date planEndTime;

    //2020-07-09 add by sanfeng.zhang
    //增加EO标识及当前EO所处工序字段
    @ApiModelProperty(value = "EO标识")
    private String eoIdentification;

    //2020-08-10 add by sanfeng.zhang 增加eo创建时间
    @ApiModelProperty(value = "创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "当前工序")
    private String eoWorkcellIdDesc;

    @ApiModelProperty("EO装配清单编码")
    private String eoBomName;

    @ApiModelProperty("EO工艺路线编码")
    private String eoRouterName;

    @ApiModelProperty("不良记录数")
    private Integer ncRecordCount;

    @ApiModelProperty("不良标记")
    @LovValue(lovCode = "WMS.FLAG_YN",meaningField = "ncFlagMeaning")
    private String ncFlag;

    @ApiModelProperty("不良标记含义")
    private String ncFlagMeaning;


    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getEoType() {
        return eoType;
    }

    public void setEoType(String eoType) {
        this.eoType = eoType;
    }

    public String getEoTypeDesc() {
        return eoTypeDesc;
    }

    public void setEoTypeDesc(String eoTypeDesc) {
        this.eoTypeDesc = eoTypeDesc;
    }

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getProductionLineCode() {
        return productionLineCode;
    }

    public void setProductionLineCode(String productionLineCode) {
        this.productionLineCode = productionLineCode;
    }

    public String getProductionLineName() {
        return productionLineName;
    }

    public void setProductionLineName(String productionLineName) {
        this.productionLineName = productionLineName;
    }

    public Date getPlanStartTime() {
        if (planStartTime == null) {
            return null;
        } else {
            return (Date) planStartTime.clone();
        }
    }

    public void setPlanStartTime(Date planStartTime) {
        if (planStartTime == null) {
            this.planStartTime = null;
        } else {
            this.planStartTime = (Date) planStartTime.clone();
        }
    }

    public Date getPlanEndTime() {
        if (planEndTime == null) {
            return null;
        } else {
            return (Date) planEndTime.clone();
        }
    }

    public void setPlanEndTime(Date planEndTime) {
        if (planEndTime == null) {
            this.planEndTime = null;
        } else {
            this.planEndTime = (Date) planEndTime.clone();
        }
    }

    public String getEoIdentification() {
        return eoIdentification;
    }

    public void setEoIdentification(String eoIdentification) {
        this.eoIdentification = eoIdentification;
    }

    public String getEoWorkcellIdDesc() {
        return eoWorkcellIdDesc;
    }

    public void setEoWorkcellIdDesc(String eoWorkcellIdDesc) {
        this.eoWorkcellIdDesc = eoWorkcellIdDesc;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getEoBomName() {
        return eoBomName;
    }

    public void setEoBomName(String eoBomName) {
        this.eoBomName = eoBomName;
    }

    public String getEoRouterName() {
        return eoRouterName;
    }

    public void setEoRouterName(String eoRouterName) {
        this.eoRouterName = eoRouterName;
    }

    public Integer getNcRecordCount() { return ncRecordCount; }

    public void setNcRecordCount(Integer ncRecordCount) { this.ncRecordCount = ncRecordCount; }

    public String getNcFlag() { return ncFlag; }

    public void setNcFlag(String ncFlag) { this.ncFlag = ncFlag; }

    public String getNcFlagMeaning() { return ncFlagMeaning; }

    public void setNcFlagMeaning(String ncFlagMeaning) { this.ncFlagMeaning = ncFlagMeaning; }
}
