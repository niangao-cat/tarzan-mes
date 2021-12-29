package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Date: 2019/9/29 13:08
 * @Author: ${yiyang.xie}
 */
public class MtEoVO30 implements Serializable {
    private static final long serialVersionUID = -4494449610341275837L;
    /**
     * 计划开始时间从
     */
    private Date planStartTimeFrom;
    /**
     * 计划开始时间至
     */
    private Date planStartTimeTo;
    /**
     * 计划结束时间从
     */
    private Date planEndTimeFrom;
    /**
     * 计划结束时间至
     */
    private Date planEndTimeTo;
    /**
     * 执行作业状态
     */
    private String status;
    /**
     * 执行作业类型
     */
    private String eoType;
    /**
     * 站点ID
     */
    private String siteId;
    /**
     * 生产线ID
     */
    private String productionLineId;
    /**
     * 工作单元ID
     */
    private String workcellId;
    /**
     * 物料ID
     */
    private String materialId;
    /**
     * 执行作业ID
     */
    private String eoId;
    /**
     * EO编码
     */
    private String eoNum;
    /**
     * 执行作业标识说明
     */
    private String identification;
    /**
     * 验证通过标识
     */
    private String validateFlag;

    public Date getPlanStartTimeFrom() {
        if (planStartTimeFrom != null) {
            return (Date) planStartTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setPlanStartTimeFrom(Date planStartTimeFrom) {
        if (planStartTimeFrom == null) {
            this.planStartTimeFrom = null;
        } else {
            this.planStartTimeFrom = (Date) planStartTimeFrom.clone();
        }
    }

    public Date getPlanStartTimeTo() {
        if (planStartTimeTo != null) {
            return (Date) planStartTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setPlanStartTimeTo(Date planStartTimeTo) {
        if (planStartTimeTo == null) {
            this.planStartTimeTo = null;
        } else {
            this.planStartTimeTo = (Date) planStartTimeTo.clone();
        }
    }

    public Date getPlanEndTimeFrom() {
        if (planEndTimeFrom != null) {
            return (Date) planEndTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setPlanEndTimeFrom(Date planEndTimeFrom) {
        if (planEndTimeFrom == null) {
            this.planEndTimeFrom = null;
        } else {
            this.planEndTimeFrom = (Date) planEndTimeFrom.clone();
        }
    }

    public Date getPlanEndTimeTo() {
        if (planEndTimeTo != null) {
            return (Date) planEndTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setPlanEndTimeTo(Date planEndTimeTo) {
        if (planEndTimeTo == null) {
            this.planEndTimeTo = null;
        } else {
            this.planEndTimeTo = (Date) planEndTimeTo.clone();
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEoType() {
        return eoType;
    }

    public void setEoType(String eoType) {
        this.eoType = eoType;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

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

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getValidateFlag() {
        return validateFlag;
    }

    public void setValidateFlag(String validateFlag) {
        this.validateFlag = validateFlag;
    }

}
