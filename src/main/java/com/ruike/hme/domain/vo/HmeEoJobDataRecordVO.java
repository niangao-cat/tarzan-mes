package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * MtEoStepJobDataRecordVO
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobDataRecordVO extends HmeEoJobDataRecord implements Serializable {

    private static final long serialVersionUID = -4588915971212979000L;
    @ApiModelProperty(value = "作业平台编码")
    private String jobType;
    @ApiModelProperty(value = "采集组ID")
    private String tagGroupId;
    @ApiModelProperty(value = "采集项ID")
    private String tagId;
    @ApiModelProperty(value = "数据项编码")
    private String tagCode;
    @ApiModelProperty(value = "数据项描述")
    private String tagDescription;
    @ApiModelProperty(value = "采集项类型")
    private String tagType;
    @ApiModelProperty(value = "结果类型")
    private String resultType;
    @ApiModelProperty(value = "是否批量处理")
    private String batchFlag;

    @ApiModelProperty(value = "工序作业ID列表")
    private List<String> jobIdList;

    @ApiModelProperty(value = "设备类别")
    private String equipmentCategory;
    @ApiModelProperty(value = "取值字段")
    private String valueField;
    @ApiModelProperty(value = "限制条件1")
    private String limitCond1;
    @ApiModelProperty(value = "条件1限制值")
    private String cond1Value;
    @ApiModelProperty(value = "限制条件2")
    private String limitCond2;
    @ApiModelProperty(value = "条件2限制值")
    private String cond2Value;

    @ApiModelProperty(value = "序号")
    private Double serialNumber;
    @ApiModelProperty(value = "补充数据采集标识")
    private String isSupplement;
    @ApiModelProperty(value = "单位")
    private String uomCode;
    @ApiModelProperty(value = "单位名称")
    private String uomName;
    @ApiModelProperty(value = "允许缺失值")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "valueAllowMissingMeaning")
    private String valueAllowMissing;
    @ApiModelProperty(value = "允许缺失值描述")
    private String valueAllowMissingMeaning;

    @ApiModelProperty(value = "eoID")
    private String eoId;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;
    @ApiModelProperty(value = "条码ID")
    private String materialLotId;
    @ApiModelProperty(value = "返修标识")
    private String reworkFlag;

    @ApiModelProperty(value = "行序列")
    private String loadSequence;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "采集项标准值")
    private BigDecimal recordStandardValue;

    @ApiModelProperty(value = "标准值")
    private String standardValue;
}
