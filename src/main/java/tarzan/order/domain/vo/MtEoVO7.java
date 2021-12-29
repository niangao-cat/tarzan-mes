package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by slj on 2019-01-09.
 */
public class MtEoVO7 implements Serializable {

    private static final long serialVersionUID = 3629704490887557397L;
    private Date planStartTimeFrom;
    private Date planStartTimeTo;
    private Date planEndTimeFrom;
    private Date planEndTimeTo;
    private String status;
    private String eoType;
    private String siteId;
    private String productionLineId;
    private String workcellId;
    private String materialId;
    private String eoId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getPlanStartTimeFrom() {
        if (planStartTimeFrom == null) {
            return null;
        }
        return (Date) planStartTimeFrom.clone();
    }

    public void setPlanStartTimeFrom(Date planStartTimeFrom) {
        if (planStartTimeFrom == null) {
            this.planStartTimeFrom = null;
        } else {
            this.planStartTimeFrom = (Date) planStartTimeFrom.clone();
        }
    }

    public Date getPlanStartTimeTo() {
        if (planStartTimeTo == null) {
            return null;
        }
        return (Date) planStartTimeTo.clone();
    }

    public void setPlanStartTimeTo(Date planStartTimeTo) {
        if (planStartTimeTo == null) {
            this.planStartTimeTo = null;
        } else {
            this.planStartTimeTo = (Date) planStartTimeTo.clone();
        }
    }

    public Date getPlanEndTimeFrom() {
        if (planEndTimeFrom == null) {
            return null;
        }
        return (Date) planEndTimeFrom.clone();
    }

    public void setPlanEndTimeFrom(Date planEndTimeFrom) {
        if (planEndTimeFrom == null) {
            this.planEndTimeFrom = null;
        } else {
            this.planEndTimeFrom = (Date) planEndTimeFrom.clone();
        }
    }

    public Date getPlanEndTimeTo() {
        if (planEndTimeTo == null) {
            return null;
        }
        return (Date) planEndTimeTo.clone();
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


}
