package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;


/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-12 11:14
 */
public class MtProcessWorkingStrategyDetailDTO1 implements Serializable {
    private static final long serialVersionUID = 1597832080889332045L;
    
    @ApiModelProperty("配置项头ID")
    private String strategyId;
    
    @ApiModelProperty(value = "配置项", required = true)
    private String strategyItem;

    @ApiModelProperty(value = "配置值", required = true)
    private String strategyValue;

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

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }
    
}
