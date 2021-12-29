package tarzan.inventory.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/9/17 16:33
 * @Description:
 */
public class MtMaterialLotVO13 implements Serializable {

    private static final long serialVersionUID = 4540278869323504341L;
    /**
     * 物料批
     */
    private String materialLotId;
    /**
     * 物料批历史ID
     */
    private String materialLotHisId;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getMaterialLotHisId() {
        return materialLotHisId;
    }

    public void setMaterialLotHisId(String materialLotHisId) {
        this.materialLotHisId = materialLotHisId;
    }
}
