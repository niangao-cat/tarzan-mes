package tarzan.order.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/26 5:42 下午
 */
public class MtEoDTO11 implements Serializable {
    private static final long serialVersionUID = 7752742973607471669L;
    @ApiModelProperty(value = "执行作业ID", required = true)
    private List<String> eoIds = new ArrayList<>();
    @ApiModelProperty(value = "操作类型", required = true)
    private String operationType;

    public List<String> getEoIds() {
        return eoIds;
    }

    public void setEoIds(List<String> eoIds) {
        this.eoIds = eoIds;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
