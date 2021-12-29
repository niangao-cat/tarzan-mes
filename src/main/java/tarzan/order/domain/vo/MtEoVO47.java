package tarzan.order.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/30 11:25
 * @Description:
 */
public class MtEoVO47 implements Serializable {
    private static final long serialVersionUID = 7187070036807467287L;

    private List<MtEoVO11> eoCompleteInfoList;

    private  String workcellId;
    private  String eventRequestId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate shiftDate;
    private String shiftCode;

    public List<MtEoVO11> getEoCompleteInfoList() {
        return eoCompleteInfoList;
    }

    public void setEoCompleteInfoList(List<MtEoVO11> eoCompleteInfoList) {
        this.eoCompleteInfoList = eoCompleteInfoList;
    }

    public  String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId( String workcellId) {
        this.workcellId = workcellId;
    }

    public  String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId( String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }
}
