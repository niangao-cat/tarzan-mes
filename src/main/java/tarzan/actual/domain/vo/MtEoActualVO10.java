package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/11/26 9:40
 * @Author: ${yiyang.xie}
 */
public class MtEoActualVO10 implements Serializable {
    private static final long serialVersionUID = 2753667610338890609L;

    @ApiModelProperty("主键")
    private String eoActualId;
    @ApiModelProperty("EO主键，标识唯一EO")
    private String eoId;
    @ApiModelProperty("累计完工数量")
    private Double completedQty;
    @ApiModelProperty("累计报废数量")
    private Double scrappedQty;
    @ApiModelProperty("累计保留数量")
    private Double holdQty;
    @ApiModelProperty("实际开始时间")
    private String actualStartTime;// 实际开始时间 格式：(yyyy-MM-dd HH:mm:ss)
    @ApiModelProperty("实际结束时间")
    private String actualEndTime; // 实际完成时间 格式：(yyyy-MM-dd HH:mm:ss)

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

    public String getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(String actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public String getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(String actualEndTime) {
        this.actualEndTime = actualEndTime;
    }
}
