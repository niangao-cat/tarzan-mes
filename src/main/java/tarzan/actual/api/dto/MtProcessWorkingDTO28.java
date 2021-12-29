package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/20 2:51 下午
 */
public class MtProcessWorkingDTO28 implements Serializable {
    private static final long serialVersionUID = 8288703698882570620L;
    @ApiModelProperty(value = "工作单元Id", required = true)
    private String workcellId;

    @ApiModelProperty("班次编码")
    private String shiftCode;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("日历日期")
    private Date shiftDate;

    @ApiModelProperty("执行作业ID-卡片获取")
    private String eoId;

    @ApiModelProperty("eo步骤实绩ID-卡片获取")
    private String eoStepActualId;

    @ApiModelProperty("工序ID-登录获取")
    private String operationId;

    @ApiModelProperty("收集组Id")
    private String tagGroupId;

    @ApiModelProperty("数据项")
    private List<MtProcessWorkingDTO29> tagData = new ArrayList<>();

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public Date getShiftDate() {
        if (shiftDate == null) {
            return null;
        } else {
            return (Date) shiftDate.clone();
        }
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    public List<MtProcessWorkingDTO29> getTagData() {
        return tagData;
    }

    public void setTagData(List<MtProcessWorkingDTO29> tagData) {
        this.tagData = tagData;
    }
}
