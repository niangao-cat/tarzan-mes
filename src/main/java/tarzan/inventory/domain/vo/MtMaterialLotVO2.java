package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import tarzan.inventory.domain.entity.MtMaterialLot;

public class MtMaterialLotVO2 extends MtMaterialLot implements Serializable {

    private static final long serialVersionUID = -3575444417183143258L;

    private String eventId;

    private Double trxPrimaryUomQty;

    private Double trxSecondaryUomQty;

    //临时增加参数，是否不通过CODE更新 modify by yuchao.wang at 2020.10.20
    private boolean onlyInsert;

    @ApiModelProperty("外部输入编码")
    private String outsideNum;
    @ApiModelProperty("传入值参数列表")
    private List<String> incomingValueList;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Double getTrxPrimaryUomQty() {
        return trxPrimaryUomQty;
    }

    public void setTrxPrimaryUomQty(Double trxPrimaryUomQty) {
        this.trxPrimaryUomQty = trxPrimaryUomQty;
    }

    public Double getTrxSecondaryUomQty() {
        return trxSecondaryUomQty;
    }

    public void setTrxSecondaryUomQty(Double trxSecondaryUomQty) {
        this.trxSecondaryUomQty = trxSecondaryUomQty;
    }

    public String getOutsideNum() {
        return outsideNum;
    }

    public void setOutsideNum(String outsideNum) {
        this.outsideNum = outsideNum;
    }

    public List<String> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<String> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }

    public boolean isOnlyInsert() {
        return onlyInsert;
    }

    public void setOnlyInsert(boolean onlyInsert) {
        this.onlyInsert = onlyInsert;
    }
}
