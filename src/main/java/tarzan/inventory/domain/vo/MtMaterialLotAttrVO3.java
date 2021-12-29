package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.tarzan.common.domain.vo.MtExtendVO5;

public class MtMaterialLotAttrVO3 implements Serializable {
    private static final long serialVersionUID = 8656148443186196306L;
    private String materialLotId;
    private String eventId;
    private String materialLotHisId;
    private List<MtExtendVO5> attr;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getMaterialLotHisId() {
        return materialLotHisId;
    }

    public void setMaterialLotHisId(String materialLotHisId) {
        this.materialLotHisId = materialLotHisId;
    }

    public List<MtExtendVO5> getAttr() {
        return attr;
    }

    public void setAttr(List<MtExtendVO5> attr) {
        this.attr = attr;
    }
}
