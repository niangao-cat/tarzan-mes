package com.ruike.itf.domain.vo;

import com.sap.conn.jco.JCoTable;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

/**
 * 查询物料凭证
 */
@Getter
@Setter
@ExcelSheet(zh = "SAP和MES凭证差异")
public class ItfMaterialVoucherVO {

    @ExcelColumn(zh = "凭证号", order = 1)
    private String voucherNo;// 凭证号

    @ExcelColumn(zh = "凭证行号", order = 2)
    private String voucherLineNo;// 凭证行号

    @ExcelColumn(zh = "年度", order = 3)
    private String years;// 年度

    @ExcelColumn(zh = "移动类型", order = 4)
    private String moveType;// 移动类型

    @ExcelColumn(zh = "物料编码", order = 5)
    private String materialCode;// 物料编码

    @ExcelColumn(zh = "移动数量", order = 6)
    private String materialQty;// 移动数量

    @ExcelColumn(zh = "工单号", order = 7)
    private String workOrderNum;// 工单号

    @ExcelColumn(zh = "仓库", order = 8)
    private String locatorCode;// 仓库

    @ExcelColumn(zh = "过账时间", order = 9)
    private String postingTime;// 过账时间

    @ExcelColumn(zh = "项目号", order = 10)
    private String projectNo;// 项目号

    @ExcelColumn(zh = "项目行号", order = 11)
    private String projectLineNo;// 项目行号

    @ExcelColumn(zh = "凭证抬头文", order = 12)
    private String voucherTitleTxt;// 凭证抬头文

    @ExcelColumn(zh = "卸货点", order = 13)
    private String unloadingPoint;// 卸货点

    @ExcelColumn(zh = "收货方", order = 14)
    private String shipTo;// 收货方

    public ItfMaterialVoucherVO() {
    }

    public ItfMaterialVoucherVO(JCoTable result) {
        this.voucherNo = Strings.isEmpty(result.getString("MBLNR")) ? "" : result.getString("MBLNR");
        this.voucherLineNo = Strings.isEmpty(result.getString("LINE_ID")) ? "" : result.getString("LINE_ID");
        this.years = Strings.isEmpty(result.getString("MJAHR")) ? "" : result.getString("MJAHR");
        this.moveType = Strings.isEmpty(result.getString("BWART")) ? "" : result.getString("BWART");
        this.materialCode = Strings.isEmpty(result.getString("MATNR")) ? "" : result.getString("MATNR");
        this.materialQty = Strings.isEmpty(result.getString("MENGE")) ? "" : result.getString("MENGE");
        this.workOrderNum = Strings.isEmpty(result.getString("AUFNR")) ? "" : result.getString("AUFNR");
        this.locatorCode = Strings.isEmpty(result.getString("LGORT")) ? "" : result.getString("MBLNR");
        this.postingTime = Strings.isEmpty(result.getString("BUDAT")) ? "" : result.getString("BUDAT");
        this.projectNo = Strings.isEmpty(result.getString("RSNUM")) ? "" : result.getString("RSNUM");
        this.projectLineNo = Strings.isEmpty(result.getString("RSPOS")) ? "" : result.getString("RSPOS");
        this.voucherTitleTxt = Strings.isEmpty(result.getString("BKTXT")) ? "" : result.getString("BKTXT");
        this.unloadingPoint = Strings.isEmpty(result.getString("ABLAD")) ? "" : result.getString("ABLAD");
        this.shipTo = Strings.isEmpty(result.getString("WEMPF")) ? "" : result.getString("WEMPF");


    }

}
