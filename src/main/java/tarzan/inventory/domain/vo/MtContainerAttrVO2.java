package tarzan.inventory.domain.vo;


import java.io.Serializable;
import java.util.List;

import io.tarzan.common.domain.vo.MtExtendAttrVO;

/**
 * @Author: chuang.yang
 * @Date: 2019/4/17 13:58
 * @Description:
 */
public class MtContainerAttrVO2 implements Serializable {
    private static final long serialVersionUID = -2512225073416403452L;

    private String containerId;
    private List<MtExtendAttrVO> attr;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public List<MtExtendAttrVO> getAttr() {
        return attr;
    }

    public void setAttr(List<MtExtendAttrVO> attr) {
        this.attr = attr;
    }
}
