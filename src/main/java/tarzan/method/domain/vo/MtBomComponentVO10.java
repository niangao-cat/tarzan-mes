package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtBomComponentVO10 implements Serializable {

    private static final long serialVersionUID = -5588082011959314918L;
    private String bomId;
    private List<MtBomComponentVO9> bomComponents;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public List<MtBomComponentVO9> getBomComponents() {
        return bomComponents;
    }

    public void setBomComponents(List<MtBomComponentVO9> bomComponents) {
        this.bomComponents = bomComponents;
    }
}
