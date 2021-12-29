package tarzan.instruction.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import tarzan.instruction.domain.entity.MtInstructionDoc;

import java.util.Date;

/**
 * @author Leeloing
 * @date 2019-09-19 13:45
 */
public class MtInstructionDocVO4 extends MtInstructionDoc {
    private static final long serialVersionUID = 1883723004588372656L;

    @ApiModelProperty(value = "需求时间从")
    private Date demandTimeFrom;

    @ApiModelProperty(value = "需求时间至")
    private Date demandTimeTo;

    @ApiModelProperty(value = "预计送达时间从")
    private Date expectedArrivalTimeFrom;

    @ApiModelProperty(value = "预计送达时间至")
    private Date expectedArrivalTimeTo;

    public Date getDemandTimeFrom() {
        if (demandTimeFrom == null) {
            return null;
        } else {
            return (Date) demandTimeFrom.clone();
        }
    }

    public void setDemandTimeFrom(Date demandTimeFrom) {
        if (demandTimeFrom == null) {
            this.demandTimeFrom = null;
        } else {
            this.demandTimeFrom = (Date) demandTimeFrom.clone();
        }
    }

    public Date getDemandTimeTo() {
        if (demandTimeTo == null) {
            return null;
        } else {
            return (Date) demandTimeTo.clone();
        }
    }

    public void setDemandTimeTo(Date demandTimeTo) {
        if (demandTimeTo == null) {
            this.demandTimeTo = null;
        } else {
            this.demandTimeTo = (Date) demandTimeTo.clone();
        }
    }

    public Date getExpectedArrivalTimeFrom() {
        if (expectedArrivalTimeFrom == null) {
            return null;
        } else {
            return (Date) expectedArrivalTimeFrom.clone();
        }
    }

    public void setExpectedArrivalTimeFrom(Date expectedArrivalTimeFrom) {
        if (expectedArrivalTimeFrom == null) {
            this.expectedArrivalTimeFrom = null;
        } else {
            this.expectedArrivalTimeFrom = (Date) expectedArrivalTimeFrom.clone();
        }
    }

    public Date getExpectedArrivalTimeTo() {
        if (expectedArrivalTimeTo == null) {
            return null;
        } else {
            return (Date) expectedArrivalTimeTo.clone();
        }
    }

    public void setExpectedArrivalTimeTo(Date expectedArrivalTimeTo) {
        if (expectedArrivalTimeTo == null) {
            this.expectedArrivalTimeTo = null;
        } else {
            this.expectedArrivalTimeTo = (Date) expectedArrivalTimeTo.clone();
        }
    }
}
