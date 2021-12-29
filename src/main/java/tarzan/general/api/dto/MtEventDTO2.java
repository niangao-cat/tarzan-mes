package tarzan.general.api.dto;

import java.util.List;

public class MtEventDTO2 extends MtEventDTO3 {
    private static final long serialVersionUID = 8502092307803571061L;
    private List<MtEventDTO3> children;

    public List<MtEventDTO3> getChildren() {
        return children;
    }

    public void setChildren(List<MtEventDTO3> children) {
        this.children = children;
    }
}
