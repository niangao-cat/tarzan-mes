package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.tarzan.common.domain.vo.MtExtendAttrVO;


/**
 * @author Leeloing
 * @date 2019/4/17 11:23
 */
public class MtContainerTypeAttrVO2 implements Serializable {

    private static final long serialVersionUID = -745775199023725552L;

    private String containerTypeId;
    private List<MtExtendAttrVO> attr;

    public String getContainerTypeId() {
        return containerTypeId;
    }

    public void setContainerTypeId(String containerTypeId) {
        this.containerTypeId = containerTypeId;
    }

    public List<MtExtendAttrVO> getAttr() {
        return attr;
    }

    public void setAttr(List<MtExtendAttrVO> attr) {
        this.attr = attr;
    }
}
