package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @description:
 * @Date: 2020-02-05
 * @Author: chuang.yang
 */
public class MtProcessWorkingStrategyDTO implements Serializable {
    private static final long serialVersionUID = 2822644390857547862L;

    @ApiModelProperty("主键ID,标识唯一一条记录")
    private String strategyId;

    @ApiModelProperty("功能编码")
    private String function;

    @ApiModelProperty("配置编码")
    private String strategyCode;

    @ApiModelProperty("配置描述")
    private String description;

    @ApiModelProperty("备注")
    private String comments;

    @ApiModelProperty("是否有效")
    private String enableFlag;

    @ApiModelProperty("默认标识")
    private String defaultFlag;

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getStrategyCode() {
        return strategyCode;
    }

    public void setStrategyCode(String strategyCode) {
        this.strategyCode = strategyCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
    }
}
