package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/26 16:48
 * @Description:
 */
public class MtEoStepWipVO15 implements Serializable {
    private static final long serialVersionUID = -3044894354311442933L;

    @ApiModelProperty(value = "执行作业工艺路线实绩唯一标识")
    private String eoStepActualId;
    @ApiModelProperty("工作单元唯一标识")
    private String workcellId;
    @ApiModelProperty(value = "目标更新数量")
    private Double qty;


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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public MtEoStepWipVO15() {
    }

    public MtEoStepWipVO15(String eoStepActualId, Double qty) {
        this.eoStepActualId = eoStepActualId;
        this.qty = qty;
    }
}
