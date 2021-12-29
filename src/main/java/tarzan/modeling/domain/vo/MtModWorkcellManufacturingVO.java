package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModWorkcellManufacturingVO implements Serializable {
    private static final long serialVersionUID = -2726658219161244721L;
    private String workcellManufacturingId;
    private String workcellId;
    private Long forwardShiftNumber;
    private Long backwardShiftNumber;

    public String getWorkcellManufacturingId() {
        return workcellManufacturingId;
    }

    public void setWorkcellManufacturingId(String workcellManufacturingId) {
        this.workcellManufacturingId = workcellManufacturingId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Long getForwardShiftNumber() {
        return forwardShiftNumber;
    }

    public void setForwardShiftNumber(Long forwardShiftNumber) {
        this.forwardShiftNumber = forwardShiftNumber;
    }

    public Long getBackwardShiftNumber() {
        return backwardShiftNumber;
    }

    public void setBackwardShiftNumber(Long backwardShiftNumber) {
        this.backwardShiftNumber = backwardShiftNumber;
    }


}
