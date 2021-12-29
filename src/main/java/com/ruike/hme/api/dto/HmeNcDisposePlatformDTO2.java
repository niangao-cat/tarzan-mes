package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;
import tarzan.actual.domain.entity.MtNcRecord;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: chaonan.hu@hand-china.com 2020-06-30 09:53:12
 **/
@Data
@ExcelSheet(zh = "不良申请单")
public class HmeNcDisposePlatformDTO2 extends MtNcRecord implements Serializable {
    private static final long serialVersionUID = -3121507459431296285L;

    @ApiModelProperty(value = "车间")
    @ExcelColumn(zh = "车间", order = 2)
    private String workShopName;

    @ApiModelProperty(value = "生产线")
    @ExcelColumn(zh = "生产线", order = 3)
    private String prodLineName;

    @ApiModelProperty(value = "工段")
    @ExcelColumn(zh = "工段", order = 4)
    private String lineWorkcellName;

    @ApiModelProperty(value = "工序")
    @ExcelColumn(zh = "工序", order = 5)
    private String processName;

    @ApiModelProperty(value = "工位")
    @ExcelColumn(zh = "责任工位", order = 6)
    private String workcellName;

    @ApiModelProperty(value = "工位类型")
    private String workcellType;

    @ApiModelProperty(value = "操作者")
    @ExcelColumn(zh = "操作者", order = 8)
    private String userName;

    @ApiModelProperty(value = "工号")
    @ExcelColumn(zh = "工号", order = 9)
    private String userCode;

    @ApiModelProperty(value = "班次")
    private String shiftName;

    @ApiModelProperty(value = "产品类型")
    private String materialType;

    @ApiModelProperty(value = "产品料号")
    @ExcelColumn(zh = "产品料号", order = 10)
    private String materialCode;

    @ApiModelProperty(value = "产品描述")
    @ExcelColumn(zh = "产品描述", order = 11)
    private String materialName;

    @ApiModelProperty(value = "序列号")
    @ExcelColumn(zh = "序列号", order = 22)
    private String materialLotCode;

    @ApiModelProperty(value = "工单号")
    @ExcelColumn(zh = "工单号", order = 23)
    private String workOrderNum;

    @ApiModelProperty(value = "不良单号")
    @ExcelColumn(zh = "不良单号", order = 1)
    private String ncNumber;

    @ApiModelProperty(value = "不良代码ID集合")
    private List<String> ncCodeIdList;

    @ApiModelProperty(value = "不良代码集合")
    private List<String> ncCodeList;

    @ApiModelProperty(value = "不良代码(用于处理平台)")
    @ExcelColumn(zh = "不良代码", order = 25)
    private String ncCode;

    @ApiModelProperty(value = "不良原因集合")
    private List<String> ncReasonList;

    @ApiModelProperty(value = "不良原因(用于处理平台)")
    @ExcelColumn(zh = "不良原因", order = 26)
    private String ncReason;

    @ApiModelProperty(value = "不良类型")
    @LovValue(value = "HME.NC_TYPE", meaningField = "ncTypeMeaning")
    private String ncType;

    @ApiModelProperty(value = "不良类型含义")
    @ExcelColumn(zh = "不良类型", order = 27)
    private String ncTypeMeaning;

    @ApiModelProperty(value = "不良组描述")
    @ExcelColumn(zh = "不良代码组", order = 24)
    private String ncGroupDesc;

    @ApiModelProperty(value = "发生时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "发生时间", order = 32,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateTime;

    @ApiModelProperty(value = "责任工位")
    @ExcelColumn(zh = "提出工位", order = 33)
    private String responseWorkcellName;

    @ApiModelProperty(value = "责任人")
    @ExcelColumn(zh = "责任人", order = 34)
    private String responseUser;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "状态含义")
    @ExcelColumn(zh = "状态", order = 7)
    private String statusMeaning;

    @ApiModelProperty(value = "处理人")
    private String disposeUserId;

    @ApiModelProperty(value = "处理人姓名")
    @ExcelColumn(zh = "处理人", order = 36)
    private String disposeUserName;

    @ApiModelProperty(value = "处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "处理时间", order = 37,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date disposeDateTime;

    @ApiModelProperty(value = "处理办法")
    private String disposeMethod;

    @ApiModelProperty(value = "处理办法含义")
    @ExcelColumn(zh = "处理办法", order = 35)
    private String disposeMethodMeaning;

    @ApiModelProperty(value = "处理人备注")
    private String disposeComment;

    @ApiModelProperty(value = "转型物料Id")
    private String transitionMaterialId;

    @ApiModelProperty(value = "转型物料编码")
    @ExcelColumn(zh = "转型物料编码", order = 28)
    private String transitionMaterialCode;

    @ApiModelProperty(value = "转型物料描述")
    @ExcelColumn(zh = "转型物料描述", order = 29)
    private String transitionMaterialName;

    @ApiModelProperty(value = "报废物料ID")
    private String scrapMaterialId;

    @ApiModelProperty(value = "报废物料编码")
    @ExcelColumn(zh = "材料编码", order = 12)
    private String scrapMaterialCode;

    @ApiModelProperty(value = "报废物料名称")
    @ExcelColumn(zh = "材料名称", order = 13)
    private String scrapMaterialName;

    @ApiModelProperty(value = "报废数量")
    @ExcelColumn(zh = "申请数量", order = 15)
    private BigDecimal scrapQty;

    @ApiModelProperty(value = "条码")
    @ExcelColumn(zh = "放行/退库条码", order = 30)
    private String barcode;

    @ApiModelProperty(value = "返修工单")
    @ExcelColumn(zh = "返修工单", order = 31)
    private String reworkOrder;

    @ApiModelProperty(value = "工艺路线")
    private String routerId;

    @ApiModelProperty(value = "处置返修方法")
    private String dispositionFunction;

    @ApiModelProperty(value = "处置返修方法id")
    private String dispositionFunctionId;

    @ApiModelProperty(value = "供应商名称")
    @ExcelColumn(zh = "供应商描述", order = 17)
    private String supplierName;

    @ApiModelProperty(value = "供应商编码")
    @ExcelColumn(zh = "供应商编码", order = 16)
    private String supplierCode;

    @ApiModelProperty(value = "供应商批次")
    @ExcelColumn(zh = "供应商批次", order = 18)
    private String supplierLot;

    @ApiModelProperty(value = "芯片位置")
    @ExcelColumn(zh = "芯片位置", order = 19)
    private String cosPosition;

    @ApiModelProperty(value = "芯片行")
    private Long loadRow;

    @ApiModelProperty(value = "芯片列")
    private Long loadColumn;

    @ApiModelProperty(value = "芯片序列")
    @ExcelColumn(zh = "芯片序列", order = 20)
    private String chipSequence;

    @ApiModelProperty(value = "热沉号")
    @ExcelColumn(zh = "COS热沉ID", order = 21)
    private String hotSinkCode;

    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(zh = "材料版本", order = 14)
    private String materialVersion;
}
