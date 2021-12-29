package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/11/22 14:19
 * @Author: ${yiyang.xie}
 */
public class MtEoBomVO4 implements Serializable {
    private static final long serialVersionUID = 8620118228118367065L;

    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("目标装配清单")
    private String bomId;

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
}
