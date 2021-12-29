package tarzan.inventory.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/4/25 20:11
 */
public class MtMaterialLotVO11 implements Serializable {

    private static final long serialVersionUID = -6781731072152943075L;

    private String materialLotId;
    private String primaryFlag;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(String primaryFlag) {
        this.primaryFlag = primaryFlag;
    }
}
