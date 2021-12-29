package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: chuang.yang
 * @Date: 2020/6/11 16:44
 * @Description:
 */
public class MtAssembleProcessActualVO8 implements Serializable {
    private static final long serialVersionUID = 6452614469968399530L;

    @ApiModelProperty("工作单元ID")
    private String workcellId;

    @ApiModelProperty("装配确认实绩ID结合")
    private List<String> assembleConfirmActualIds;

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public List<String> getAssembleConfirmActualIds() {
        return assembleConfirmActualIds;
    }

    public void setAssembleConfirmActualIds(List<String> assembleConfirmActualIds) {
        this.assembleConfirmActualIds = assembleConfirmActualIds;
    }
}
