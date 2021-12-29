package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/11/3 20:31
 */
public class MtEoStepWipVO22 implements Serializable {
    private static final long serialVersionUID = -2614050883860302053L;

    @ApiModelProperty(value = "执行作业工艺路线实绩唯一标识")
    private String eoStepActualId;

    @ApiModelProperty(value = "wkc")
    private String workcellId;

    @ApiModelProperty(value = "执行更新的数量")
    private Double updateQty;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Double getUpdateQty() {
        return updateQty;
    }

    public void setUpdateQty(Double updateQty) {
        this.updateQty = updateQty;
    }
}
