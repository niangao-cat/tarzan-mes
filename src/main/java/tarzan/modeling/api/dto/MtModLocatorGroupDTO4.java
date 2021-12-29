package tarzan.modeling.api.dto;

import java.io.Serializable;
import java.util.List;

import io.tarzan.common.api.dto.MtExtendAttrDTO;
import tarzan.modeling.domain.vo.MtModLocatorGroupVO2;

public class MtModLocatorGroupDTO4 implements Serializable {
    private static final long serialVersionUID = 4190205165482000773L;
    private MtModLocatorGroupVO2 locatorGroupInfo;
    private List<MtExtendAttrDTO> attrs;

    public MtModLocatorGroupVO2 getLocatorGroupInfo() {
        return locatorGroupInfo;
    }

    public void setLocatorGroupInfo(MtModLocatorGroupVO2 locatorGroupInfo) {
        this.locatorGroupInfo = locatorGroupInfo;
    }

    public List<MtExtendAttrDTO> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<MtExtendAttrDTO> attrs) {
        this.attrs = attrs;
    }
}
