package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtContainerVO36
 * @description
 * @date 2020年02月05日 11:34
 */
public class MtContainerVO36 implements Serializable {
    private static final long serialVersionUID = -2644502624804035406L;
    @ApiModelProperty(value = "容器")
    private List<String> containerId = new ArrayList<>();
    @ApiModelProperty(value = "装载对象类型")
    private String loadObjectType;
    @ApiModelProperty(value = "装载对象ID")
    private String loadObjectId;

    public List<String> getContainerId() {
        return containerId;
    }

    public void setContainerId(List<String> containerId) {
        this.containerId = containerId;
    }

    public String getLoadObjectType() {
        return loadObjectType;
    }

    public void setLoadObjectType(String loadObjectType) {
        this.loadObjectType = loadObjectType;
    }

    public String getLoadObjectId() {
        return loadObjectId;
    }

    public void setLoadObjectId(String loadObjectId) {
        this.loadObjectId = loadObjectId;
    }
}
