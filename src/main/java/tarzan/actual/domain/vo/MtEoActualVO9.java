package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import org.hzero.mybatis.common.query.Where;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/8 10:29
 * @Author: ${yiyang.xie}
 */
public class MtEoActualVO9 implements Serializable {
    private static final long serialVersionUID = 4066082889759690445L;
    @ApiModelProperty("执行作业实绩ID")
    @Where
    private String eoActualId;
    @ApiModelProperty("执行作业ID")
    @Where
    private String eoId;
    @ApiModelProperty("执行作业编码")
    @Where
    private String eoNum;
    @ApiModelProperty("累计完成数量")
    @Where
    private Double completedQty;
    @ApiModelProperty("累计报废数量")
    @Where
    private Double scrappedQty;
    @ApiModelProperty("保留数量")
    @Where
    private Double holdQty;
    @ApiModelProperty("实绩开始时间")
    @Where
    private Date actualStartDate;
    @ApiModelProperty("实绩结束时间")
    @Where
    private Date actualEndDate;

    public String getEoActualId() {
        return eoActualId;
    }

    public void setEoActualId(String eoActualId) {
        this.eoActualId = eoActualId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    public Double getHoldQty() {
        return holdQty;
    }

    public void setHoldQty(Double holdQty) {
        this.holdQty = holdQty;
    }

    public Date getActualStartDate() {
        if (actualStartDate != null) {
            return (Date) actualStartDate.clone();
        } else {
            return null;
        }
    }

    public void setActualStartDate(Date actualStartDate) {
        if (actualStartDate == null) {
            this.actualStartDate = null;
        } else {
            this.actualStartDate = (Date) actualStartDate.clone();
        }
    }

    public Date getActualEndDate() {
        if (actualEndDate != null) {
            return (Date) actualEndDate.clone();
        } else {
            return null;
        }
    }

    public void setActualEndDate(Date actualEndDate) {
        if (actualEndDate == null) {
            this.actualEndDate = null;
        } else {
            this.actualEndDate = (Date) actualEndDate.clone();
        }
    }
}
