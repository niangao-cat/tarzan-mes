package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-06 9:37
 */
public class MtProcessWorkingStrategyDetailDTO implements Serializable {
    private static final long serialVersionUID = 1259599611829565127L;

    @ApiModelProperty("主键ID,标识唯一一条记录")
    private String strategyDetailId;

    @ApiModelProperty(value = "头表主键ID", required = true)
    private String strategyId;

    @ApiModelProperty(value = "配置项", required = true)
    private String strategyItem;

    @ApiModelProperty(value = "配置值", required = true)
    private String strategyValue;

    @ApiModelProperty(value = "配置类型", required = true)
    private String strategyType;

    public String getStrategyDetailId() {
        return strategyDetailId;
    }

    public void setStrategyDetailId(String strategyDetailId) {
        this.strategyDetailId = strategyDetailId;
    }

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getStrategyItem() {
        return strategyItem;
    }

    public void setStrategyItem(String strategyItem) {
        this.strategyItem = strategyItem;
    }

    public String getStrategyValue() {
        return strategyValue;
    }

    public void setStrategyValue(String strategyValue) {
        this.strategyValue = strategyValue;
    }

    public String getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
    }
}
