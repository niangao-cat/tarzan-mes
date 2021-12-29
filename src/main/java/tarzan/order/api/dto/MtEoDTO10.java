package tarzan.order.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/24 3:50 下午
 */
public class MtEoDTO10 implements Serializable {
    private static final long serialVersionUID = -2449965738302833525L;
    @ApiModelProperty("主合并来源执行作业")
    private String primaryEoId;
    @ApiModelProperty("副来源执行作业")
    private List<String> secondaryEoIds = new ArrayList<>();

    public String getPrimaryEoId() {
        return primaryEoId;
    }

    public void setPrimaryEoId(String primaryEoId) {
        this.primaryEoId = primaryEoId;
    }

    public List<String> getSecondaryEoIds() {
        return secondaryEoIds;
    }

    public void setSecondaryEoIds(List<String> secondaryEoIds) {
        this.secondaryEoIds = secondaryEoIds;
    }
}
