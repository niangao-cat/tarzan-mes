package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MtAssembleConfirmActualVO23 implements Serializable {

    private static final long serialVersionUID = 7129449185084651505L;
    private String assembleConfirmActualId;
    private String assembleProcessActualId;
    private String eoId;
    private String materialId;
    private String substituteFlag;
    private String bomComponentId;
    private String eventId;
    private BigDecimal scrapQty;
    private BigDecimal assembleQty;
    private Date eventTime;

    public String getAssembleConfirmActualId() {
        return assembleConfirmActualId;
    }

    public void setAssembleConfirmActualId(String assembleConfirmActualId) {
        this.assembleConfirmActualId = assembleConfirmActualId;
    }

    public String getAssembleProcessActualId() {
        return assembleProcessActualId;
    }

    public void setAssembleProcessActualId(String assembleProcessActualId) {
        this.assembleProcessActualId = assembleProcessActualId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public BigDecimal getScrapQty() {
        return scrapQty;
    }

    public void setScrapQty(BigDecimal scrapQty) {
        this.scrapQty = scrapQty;
    }

    public BigDecimal getAssembleQty() {
        return assembleQty;
    }

    public void setAssembleQty(BigDecimal assembleQty) {
        this.assembleQty = assembleQty;
    }

    public Date getEventTime() {
        return eventTime == null ? null : (Date) eventTime.clone();
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime == null ? null : (Date) eventTime.clone();
    }
}
