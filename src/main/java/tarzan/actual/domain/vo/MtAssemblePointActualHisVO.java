package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtAssemblePointActualHisVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2714067704358915087L;
    private String assemblePointActualId;
    private String eventId;
    private String assembleGroupActualId;
    private String assemblePointId;
    private String materialId;
    private String materialLotId;

    public String getAssemblePointActualId() {
        return assemblePointActualId;
    }

    public void setAssemblePointActualId(String assemblePointActualId) {
        this.assemblePointActualId = assemblePointActualId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAssembleGroupActualId() {
        return assembleGroupActualId;
    }

    public void setAssembleGroupActualId(String assembleGroupActualId) {
        this.assembleGroupActualId = assembleGroupActualId;
    }

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

}
