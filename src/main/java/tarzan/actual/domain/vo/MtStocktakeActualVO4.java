package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Leeloing
 * @date 2019/7/9 15:37
 */
public class MtStocktakeActualVO4 implements Serializable {

    private static final long serialVersionUID = 1659491837171066491L;

    private String stocktakeId;
    private List<String> materialLotIdList;
    private String allAdjustFlag;

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

    public List<String> getMaterialLotIdList() {
        return materialLotIdList;
    }

    public void setMaterialLotIdList(List<String> materialLotIdList) {
        this.materialLotIdList = materialLotIdList;
    }

    public String getAllAdjustFlag() {
        return allAdjustFlag;
    }

    public void setAllAdjustFlag(String allAdjustFlag) {
        this.allAdjustFlag = allAdjustFlag;
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
