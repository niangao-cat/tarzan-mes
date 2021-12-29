package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Leeloing
 * @date 2019/7/8 17:26
 */
public class MtStocktakeActualVO2 implements Serializable {
    private static final long serialVersionUID = 1612918428316704215L;

    private String stocktakeId;
    private String materialLotId;
    private String firstCountAdjustFlag;
    private String recountAdjustFlag;

    private String issueNumObjectTypeCode;
    private Map<String, String> issueNumCallObjectCodeList;
    private List<String> issueNumIncomingValueList;

    private String receiptNumObjectTypeCode;
    private Map<String, String> receiptNumCallObjectCodeList;
    private List<String> receiptNumIncomingValueList;

    private String miscellaneousIssueBusinessType;
    private String miscellaneousIssueCostCenterId;
    private String miscellaneousIssueRemark;
    private String miscellaneousReceiptBusinessType;
    private String miscellaneousReceiptCostCenterId;
    private String miscellaneousReceiptRemark;

    public String getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(String stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getFirstCountAdjustFlag() {
        return firstCountAdjustFlag;
    }

    public void setFirstCountAdjustFlag(String firstCountAdjustFlag) {
        this.firstCountAdjustFlag = firstCountAdjustFlag;
    }

    public String getRecountAdjustFlag() {
        return recountAdjustFlag;
    }

    public void setRecountAdjustFlag(String recountAdjustFlag) {
        this.recountAdjustFlag = recountAdjustFlag;
    }

    public String getIssueNumObjectTypeCode() {
        return issueNumObjectTypeCode;
    }

    public void setIssueNumObjectTypeCode(String issueNumObjectTypeCode) {
        this.issueNumObjectTypeCode = issueNumObjectTypeCode;
    }

    public Map<String, String> getIssueNumCallObjectCodeList() {
        return issueNumCallObjectCodeList;
    }

    public void setIssueNumCallObjectCodeList(Map<String, String> issueNumCallObjectCodeList) {
        this.issueNumCallObjectCodeList = issueNumCallObjectCodeList;
    }

    public List<String> getIssueNumIncomingValueList() {
        return issueNumIncomingValueList;
    }

    public void setIssueNumIncomingValueList(List<String> issueNumIncomingValueList) {
        this.issueNumIncomingValueList = issueNumIncomingValueList;
    }

    public String getReceiptNumObjectTypeCode() {
        return receiptNumObjectTypeCode;
    }

    public void setReceiptNumObjectTypeCode(String receiptNumObjectTypeCode) {
        this.receiptNumObjectTypeCode = receiptNumObjectTypeCode;
    }

    public Map<String, String> getReceiptNumCallObjectCodeList() {
        return receiptNumCallObjectCodeList;
    }

    public void setReceiptNumCallObjectCodeList(Map<String, String> receiptNumCallObjectCodeList) {
        this.receiptNumCallObjectCodeList = receiptNumCallObjectCodeList;
    }

    public List<String> getReceiptNumIncomingValueList() {
        return receiptNumIncomingValueList;
    }

    public void setReceiptNumIncomingValueList(List<String> receiptNumIncomingValueList) {
        this.receiptNumIncomingValueList = receiptNumIncomingValueList;
    }

    public String getMiscellaneousIssueBusinessType() {
        return miscellaneousIssueBusinessType;
    }

    public void setMiscellaneousIssueBusinessType(String miscellaneousIssueBusinessType) {
        this.miscellaneousIssueBusinessType = miscellaneousIssueBusinessType;
    }

    public String getMiscellaneousIssueCostCenterId() {
        return miscellaneousIssueCostCenterId;
    }

    public void setMiscellaneousIssueCostCenterId(String miscellaneousIssueCostCenterId) {
        this.miscellaneousIssueCostCenterId = miscellaneousIssueCostCenterId;
    }

    public String getMiscellaneousIssueRemark() {
        return miscellaneousIssueRemark;
    }

    public void setMiscellaneousIssueRemark(String miscellaneousIssueRemark) {
        this.miscellaneousIssueRemark = miscellaneousIssueRemark;
    }

    public String getMiscellaneousReceiptBusinessType() {
        return miscellaneousReceiptBusinessType;
    }

    public void setMiscellaneousReceiptBusinessType(String miscellaneousReceiptBusinessType) {
        this.miscellaneousReceiptBusinessType = miscellaneousReceiptBusinessType;
    }

    public String getMiscellaneousReceiptCostCenterId() {
        return miscellaneousReceiptCostCenterId;
    }

    public void setMiscellaneousReceiptCostCenterId(String miscellaneousReceiptCostCenterId) {
        this.miscellaneousReceiptCostCenterId = miscellaneousReceiptCostCenterId;
    }

    public String getMiscellaneousReceiptRemark() {
        return miscellaneousReceiptRemark;
    }

    public void setMiscellaneousReceiptRemark(String miscellaneousReceiptRemark) {
        this.miscellaneousReceiptRemark = miscellaneousReceiptRemark;
    }

}
