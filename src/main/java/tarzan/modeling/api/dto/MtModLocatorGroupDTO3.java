package tarzan.modeling.api.dto;

import java.io.Serializable;
import java.util.List;

import io.tarzan.common.api.dto.MtExtendAttrDTO3;

public class MtModLocatorGroupDTO3 implements Serializable {
    private static final long serialVersionUID = -4873435832890536041L;

    private MtModLocatorGroupDTO locatorGroupInfo;
    private List<MtExtendAttrDTO3> attrs;

    public MtModLocatorGroupDTO getLocatorGroupInfo() {
        return locatorGroupInfo;
    }

    public void setLocatorGroupInfo(MtModLocatorGroupDTO locatorGroupInfo) {
        this.locatorGroupInfo = locatorGroupInfo;
    }

    public List<MtExtendAttrDTO3> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<MtExtendAttrDTO3> attrs) {
        this.attrs = attrs;
    }
}
