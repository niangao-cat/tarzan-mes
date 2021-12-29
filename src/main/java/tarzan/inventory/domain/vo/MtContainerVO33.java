package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/11/26 17:35
 * @Author: ${yiyang.xie}
 */
public class MtContainerVO33 implements Serializable {
    private static final long serialVersionUID = -1142643660769893978L;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("外部输入编码")
    private String outsideNum;
    @ApiModelProperty("调用标准对象传入参数列表")
    private Map<String, String> containerPropertyList;
    @ApiModelProperty("传入值参数列表")
    private List<String> incomingValueList;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOutsideNum() {
        return outsideNum;
    }

    public void setOutsideNum(String outsideNum) {
        this.outsideNum = outsideNum;
    }

    public Map<String, String> getContainerPropertyList() {
        return containerPropertyList;
    }

    public void setContainerPropertyList(Map<String, String> containerPropertyList) {
        this.containerPropertyList = containerPropertyList;
    }

    public List<String> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<String> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }
}
