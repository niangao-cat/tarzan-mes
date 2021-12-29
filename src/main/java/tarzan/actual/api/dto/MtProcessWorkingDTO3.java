package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/12 10:46 上午
 */
public class MtProcessWorkingDTO3 implements Serializable {
    private static final long serialVersionUID = -5378357378274709490L;
    @ApiModelProperty("tab页类型")
    private String tabType;

    @ApiModelProperty("tab数据")
    private List<MtProcessWorkingDTO2> tabs;

    public String getTabType() {
        return tabType;
    }

    public void setTabType(String tabType) {
        this.tabType = tabType;
    }

    public List<MtProcessWorkingDTO2> getTabs() {
        return tabs;
    }

    public void setTabs(List<MtProcessWorkingDTO2> tabs) {
        this.tabs = tabs;
    }
}
