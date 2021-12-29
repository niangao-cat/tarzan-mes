package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/18 9:43 上午
 */
public class MtProcessWorkingDTO19 implements Serializable {
    private static final long serialVersionUID = 6845355455148881972L;
    @ApiModelProperty(value = "工作单元Id", required = true)
    private String workcellId;
    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;
    @ApiModelProperty(value = "执行作业Id", required = true)
    private String eoId;
    @ApiModelProperty(value = "装配清单Id", required = true)
    private String bomId;
    @ApiModelProperty("步骤Id")
    private String routerStepId;
    @ApiModelProperty("工序ID")
    private String operationId;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("日历日期")
    private Date shiftDate;

    @ApiModelProperty("装配-表格数据")
    private List<MtProcessWorkingDTO20> assembleList = new ArrayList<>();

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
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

    public List<MtProcessWorkingDTO20> getAssembleList() {
        return assembleList;
    }

    public void setAssembleList(List<MtProcessWorkingDTO20> assembleList) {
        this.assembleList = assembleList;
    }
}
