package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/9/16 19:31
 * @Description:
 */
public class MtEoVO29 implements Serializable {
    private static final long serialVersionUID = -5365847525569681106L;

    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("执行作业历史ID")
    private String eoHisId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEoHisId() {
        return eoHisId;
    }

    public void setEoHisId(String eoHisId) {
        this.eoHisId = eoHisId;
    }
}
