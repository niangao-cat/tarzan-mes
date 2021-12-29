package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.tarzan.common.domain.vo.MtExtendAttrVO;



public class MtWorkOrderAttrVO1 implements Serializable {

    private static final long serialVersionUID = -6419214171954719791L;
    private String workOrderId;
    private List<MtExtendAttrVO> attr;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public List<MtExtendAttrVO> getAttr() {
        return attr;
    }

    public void setAttr(List<MtExtendAttrVO> attr) {
        this.attr = attr;
    }
}
