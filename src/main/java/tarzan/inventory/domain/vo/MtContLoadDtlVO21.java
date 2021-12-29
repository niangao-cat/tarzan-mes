package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtContLoadDtlVO21
 *
 * @author: {xieyiyang}
 * @date: 2020/2/10 14:56
 * @description:
 */
public class MtContLoadDtlVO21 implements Serializable {
    private static final long serialVersionUID = -7215819285095684856L;

    @ApiModelProperty("容器Id")
    private String containerId;

    @ApiModelProperty("装载对象列表")
    private List<MtContLoadDtlVO6> loadDetailList = new ArrayList<>();

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public List<MtContLoadDtlVO6> getLoadDetailList() {
        return loadDetailList;
    }

    public void setLoadDetailList(List<MtContLoadDtlVO6> loadDetailList) {
        this.loadDetailList = loadDetailList;
    }
}
