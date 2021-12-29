package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtContLoadDtlVO20
 *
 * @author: {xieyiyang}
 * @date: 2020/2/7 17:40
 * @description:
 */
public class MtContLoadDtlVO20 implements Serializable {
    private static final long serialVersionUID = -7027043875986577050L;

    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("已装载数量")
    private Double loadQty;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public Double getLoadQty() {
        return loadQty;
    }

    public void setLoadQty(Double loadQty) {
        this.loadQty = loadQty;
    }
}
