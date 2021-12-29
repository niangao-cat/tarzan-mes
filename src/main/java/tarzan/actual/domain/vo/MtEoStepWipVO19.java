package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/11/3 19:47
 */
public class MtEoStepWipVO19 implements Serializable {
    private static final long serialVersionUID = 3566465686636109046L;
    @ApiModelProperty(value = "执行作业工艺路线实绩唯一标识")
    private String eoStepActualId;

    @ApiModelProperty(value = "目标更新数量")
    private Double qty;

    @ApiModelProperty(value = "工作单元")
    private List<String> workcellIds;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public List<String> getWorkcellIds() {
        return workcellIds;
    }

    public void setWorkcellIds(List<String> workcellIds) {
        this.workcellIds = workcellIds;
    }
}
