package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/26 10:00
 * @Description:
 */
public class MtEoStepWipVO2 implements Serializable {

    private static final long serialVersionUID = -459406474438407041L;

    @ApiModelProperty(value = "执行作业工艺路线实绩唯一标识")
    private String eoStepActualId;

    @ApiModelProperty(value = "执行更新的数量")
    private Double updateQty;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public Double getUpdateQty() {
        return updateQty;
    }

    public void setUpdateQty(Double updateQty) {
        this.updateQty = updateQty;
    }
}
