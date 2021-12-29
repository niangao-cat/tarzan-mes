package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/31 18:35
 */
@Data
@ExcelSheet(title = "数据收集组&关联对象")
public class HmeTagGroupObjectExportVO implements Serializable {

    private static final long serialVersionUID = -5623438012482739388L;

    @ApiModelProperty(value = "数据收集组主键")
    private String tagGroupId;

    @ApiModelProperty(value = "数据收集组编码")
    @ExcelColumn(title = "数据收集组编码", order = 0)
    private String tagGroupCode;

    @ApiModelProperty(value = "数据收集组描述")
    @ExcelColumn(title = "数据收集组描述", order = 1)
    private String tagGroupDescription;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "状态含义")
    @ExcelColumn(title = "状态", order = 2)
    private String statusMeaning;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "业务类型含义")
    @ExcelColumn(title = "业务类型", order = 3)
    private String businessTypeMeaning;

    @ApiModelProperty(value = "收集组类型")
    private String tagGroupType;

    @ApiModelProperty(value = "收集组类型含义")
    @ExcelColumn(title = "收集组类型", order = 4)
    private String tagGroupTypeMeaning;

    @ApiModelProperty(value = "源数据收集组")
    @ExcelColumn(title = "源数据收集组", order = 5)
    private String sourceGroupId;

    @ApiModelProperty(value = "数据收集时点")
    private String collectionTimeControl;

    @ApiModelProperty(value = "数据收集时点")
    @ExcelColumn(title = "数据收集时点", order = 6)
    private String collectionTimeControlMeaning;

    @ApiModelProperty(value = "需要用户验证")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "userVerificationMeaning")
    private String userVerification;

    @ApiModelProperty(value = "需要用户验证")
    @ExcelColumn(title = "需要用户验证", order = 7)
    private String userVerificationMeaning;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(title = "物料编码", order = 8)
    private String materialCode;

    @ApiModelProperty(value = "工艺编码")
    @ExcelColumn(title = "工艺编码", order = 9)
    private String operationName;

    @ApiModelProperty(value = "工艺路线")
    @ExcelColumn(title = "工艺路线", order = 10)
    private String routerName;

    @ApiModelProperty(value = "工艺路线步骤")
    @ExcelColumn(title = "工艺路线步骤", order = 11)
    private String routerStepName;

    @ApiModelProperty(value = "工作单元编码")
    @ExcelColumn(title = "工作单元编码", order = 12)
    private String workcellCode;

    @ApiModelProperty(value = "NC代码编码")
    @ExcelColumn(title = "NC代码编码", order = 13)
    private String ncCode;

    @ApiModelProperty(value = "装配清单")
    @ExcelColumn(title = "装配清单", order = 14)
    private String bomName;

    @ApiModelProperty(value = "装配清单组件")
    @ExcelColumn(title = "装配清单组件", order = 15)
    private String bomComponentId;

    @ApiModelProperty(value = "WO")
    @ExcelColumn(title = "WO", order = 16)
    private String workOrderNum;

    @ApiModelProperty(value = "EO")
    @ExcelColumn(title = "EO", order = 17)
    private String eoNum;

    @ApiModelProperty(value = "扩展字段名")
    @ExcelColumn(title = "扩展字段名", order = 18)
    private String attrName;

    @ApiModelProperty(value = "扩展字段值")
    @ExcelColumn(title = "扩展字段值", order = 19)
    private String attrValue;
}
