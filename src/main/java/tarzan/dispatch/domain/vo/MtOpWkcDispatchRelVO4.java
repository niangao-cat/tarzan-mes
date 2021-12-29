package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class MtOpWkcDispatchRelVO4 implements Serializable {
    private static final long serialVersionUID = -419013872296302203L;

    @ApiModelProperty("工艺路线步骤ID")
    private String routerStepId;
    @ApiModelProperty("班次日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @ApiModelProperty("是否同时发布")
    private String needPublishFlag;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("前道工序ID")
    private String preWorkcellId;


    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public Date getShiftDate() {
        if (shiftDate == null) {
            return null;
        }
        return (Date) shiftDate.clone();
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public String getNeedPublishFlag() {
        return needPublishFlag;
    }

    public void setNeedPublishFlag(String needPublishFlag) {
        this.needPublishFlag = needPublishFlag;
    }

    public String getPreWorkcellId() {
        return preWorkcellId;
    }

    public void setPreWorkcellId(String preWorkcellId) {
        this.preWorkcellId = preWorkcellId;
    }
}

