package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import org.hzero.mybatis.common.query.Where;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/8 10:13
 * @Author: ${yiyang.xie}
 */
public class MtEoActualVO8 implements Serializable {
    private static final long serialVersionUID = 5249738914513678387L;
    @ApiModelProperty("执行作业ID")
    @Where
    private String eoId;
    @ApiModelProperty("执行作业实绩ID")
    @Where
    private String eoActualId;
    @ApiModelProperty("实绩开始时间从")
    @Where
    private Date actualStartDateFrom;
    @ApiModelProperty("实绩开始时间至")
    @Where
    private Date actualStartDateTo;
    @ApiModelProperty("实绩结束时间从")
    @Where
    private Date actualEndDateFrom;
    @ApiModelProperty("实绩结束时间至")
    @Where
    private Date actualEndDateTo;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEoActualId() {
        return eoActualId;
    }

    public void setEoActualId(String eoActualId) {
        this.eoActualId = eoActualId;
    }

    public Date getActualStartDateFrom() {
        if (actualStartDateFrom != null) {
            return (Date) actualStartDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setActualStartDateFrom(Date actualStartDateFrom) {
        if (actualStartDateFrom == null) {
            this.actualStartDateFrom = null;
        } else {
            this.actualStartDateFrom = (Date) actualStartDateFrom.clone();
        }
    }

    public Date getActualStartDateTo() {
        if (actualStartDateTo != null) {
            return (Date) actualStartDateTo.clone();
        } else {
            return null;
        }
    }

    public void setActualStartDateTo(Date actualStartDateTo) {
        if (actualStartDateTo == null) {
            this.actualStartDateTo = null;
        } else {
            this.actualStartDateTo = (Date) actualStartDateTo.clone();
        }
    }

    public Date getActualEndDateFrom() {
        if (actualEndDateFrom != null) {
            return (Date) actualEndDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setActualEndDateFrom(Date actualEndDateFrom) {
        if (actualEndDateFrom == null) {
            this.actualEndDateFrom = null;
        } else {
            this.actualEndDateFrom = (Date) actualEndDateFrom.clone();
        }
    }

    public Date getActualEndDateTo() {
        if (actualEndDateTo != null) {
            return (Date) actualEndDateTo.clone();
        } else {
            return null;
        }
    }

    public void setActualEndDateTo(Date actualEndDateTo) {
        if (actualEndDateTo == null) {
            this.actualEndDateTo = null;
        } else {
            this.actualEndDateTo = (Date) actualEndDateTo.clone();
        }
    }
}
