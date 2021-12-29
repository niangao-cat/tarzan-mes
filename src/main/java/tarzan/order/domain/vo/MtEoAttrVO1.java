package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.tarzan.common.domain.vo.MtExtendAttrVO;


/**
 * @author Leeloing
 * @date 2019/4/16 19:50
 */
public class MtEoAttrVO1 implements Serializable {

    private static final long serialVersionUID = -4620331655607307456L;

    private String eoId;
    private List<MtExtendAttrVO> attr;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public List<MtExtendAttrVO> getAttr() {
        return attr;
    }

    public void setAttr(List<MtExtendAttrVO> attr) {
        this.attr = attr;
    }
}
