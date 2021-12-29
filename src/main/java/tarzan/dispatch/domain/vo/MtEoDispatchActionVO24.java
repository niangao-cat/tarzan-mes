package tarzan.dispatch.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * MtEoDispatchActionVO24
 *
 * @author: {xieyiyang}
 * @date: 2020/2/5 16:00
 * @description:
 */
public class MtEoDispatchActionVO24 implements Serializable {
    private static final long serialVersionUID = -5446124445510371640L;

    @ApiModelProperty(value = "范围策略")
    private String rangeStrategy;
    @ApiModelProperty(value = "发布策略")
    private String publishStrategy;
    @ApiModelProperty(value = "移动策略")
    private String moveStrategy;

    public String getRangeStrategy() {
        return rangeStrategy;
    }

    public void setRangeStrategy(String rangeStrategy) {
        this.rangeStrategy = rangeStrategy;
    }

    public String getPublishStrategy() {
        return publishStrategy;
    }

    public void setPublishStrategy(String publishStrategy) {
        this.publishStrategy = publishStrategy;
    }

    public String getMoveStrategy() {
        return moveStrategy;
    }

    public void setMoveStrategy(String moveStrategy) {
        this.moveStrategy = moveStrategy;
    }
}
