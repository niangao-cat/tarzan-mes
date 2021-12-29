package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: chuang.yang
 * @Date: 2020/1/14 14:28
 * @Description:
 */
public class MtInstructionActualDetailVO3 implements Serializable {
    private static final long serialVersionUID = 6361892588467235354L;

    @ApiModelProperty("指令实绩ID")
    private String actualId;

    @ApiModelProperty("实绩明细ID集合")
    private List<String> actualDetailIds;

    public String getActualId() {
        return actualId;
    }

    public void setActualId(String actualId) {
        this.actualId = actualId;
    }

    public List<String> getActualDetailIds() {
        return actualDetailIds;
    }

    public void setActualDetailIds(List<String> actualDetailIds) {
        this.actualDetailIds = actualDetailIds;
    }
}
