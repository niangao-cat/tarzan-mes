package tarzan.instruction.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import tarzan.instruction.domain.entity.MtInstruction;

import java.util.Date;

/**
 * @author Leeloing
 * @date 2019-09-19 15:50
 */
public class MtInstructionVO10 extends MtInstruction {

    private static final long serialVersionUID = -7748182826120461652L;
    private Date demandTimeFrom;
    private Date demandTimeTo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateFrom;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateTo;

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

    public Date getShiftDateFrom() {
        if (shiftDateFrom == null) {
            return null;
        } else {
            return (Date) shiftDateFrom.clone();
        }
    }

    public void setShiftDateFrom(Date shiftDateFrom) {
        if (shiftDateFrom == null) {
            this.shiftDateFrom = null;
        } else {
            this.shiftDateFrom = (Date) shiftDateFrom.clone();
        }
    }

    public Date getShiftDateTo() {
        if (shiftDateTo == null) {
            return null;
        } else {
            return (Date) shiftDateTo.clone();
        }
    }

    public void setShiftDateTo(Date shiftDateTo) {
        if (shiftDateTo == null) {
            this.shiftDateTo = null;
        } else {
            this.shiftDateTo = (Date) shiftDateTo.clone();
        }
    }
}
