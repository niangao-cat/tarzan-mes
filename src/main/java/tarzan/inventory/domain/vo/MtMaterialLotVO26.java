package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-11-21 17:44
 */
public class MtMaterialLotVO26 implements Serializable {
    private static final long serialVersionUID = 8641032709333309122L;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("外部输入编码")
    private String outsideNum;
    @ApiModelProperty("调用标准对象传入参数列表")
    private Map<String, String> materilaLotPropertyList;
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

    public Map<String, String> getMaterilaLotPropertyList() {
        return materilaLotPropertyList;
    }

    public void setMaterilaLotPropertyList(Map<String, String> materilaLotPropertyList) {
        this.materilaLotPropertyList = materilaLotPropertyList;
    }

    public List<String> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<String> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }
}
