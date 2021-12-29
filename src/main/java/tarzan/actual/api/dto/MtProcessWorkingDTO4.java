package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/12 10:53 上午
 */
public class MtProcessWorkingDTO4 implements Serializable {
    private static final long serialVersionUID = 7900606288992775628L;

    @ApiModelProperty(value = "工作单元Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "工序ID", required = true)
    private String operationId;

    @ApiModelProperty(value = "tab页列表")
    private List<String> tabList;

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public List<String> getTabList() {
        return tabList;
    }

    public void setTabList(List<String> tabList) {
        this.tabList = tabList;
    }
}
