package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeEquipment;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hzero.boot.platform.lov.annotation.LovValue;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * description
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */
@Setter
@Getter
@ToString
public class HmeEquipmentVO extends HmeEquipment implements Serializable {

    private static final long serialVersionUID = -6728942947629866377L;

    @ApiModelProperty("入账日期从")
    private String postingDateStart;

    @ApiModelProperty("入账日期至")
    private String postingDateEnd;

    @ApiModelProperty(value = "资产类别描述")
    private String assetClassDes;

    @ApiModelProperty(value = "设备类别描述")
    private String equipmentCategoryDes;

    @ApiModelProperty(value = "设备类型描述")
    private String equipmentTypeDes;

    @ApiModelProperty(value = "应用类型描述")
    private String applyTypeDes;

    @ApiModelProperty(value = "设备状态描述")
    private String equipmentStatusDes;

    @ApiModelProperty(value = "是否计量描述")
    private String measureFlagDes;

    @ApiModelProperty(value = "使用频次描述")
    private String frequencyDes;

    @ApiModelProperty(value = "币种")
    private String currencySymbol;

    @ApiModelProperty(value = "组织编码")
    private String siteCode;

    @ApiModelProperty(value = "设备所在工位ID")
    private String stationId;

    @ApiModelProperty(value = "当前工位ID")
    private String workcellCodeId;

    @ApiModelProperty(value = "当前工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "当前工位")
    private String workcellName;

    @ApiModelProperty(value = "管理模式")
    @LovValue(value = "HME.EQUIPMENT_MANAGE_MODEL", meaningField = "attribute1Meaning")
    private String attribute1;

    @ApiModelProperty(value = "管理模式含义")
    private String attribute1Meaning;

    @ApiModelProperty(value = "保管部门名称")
    private String businessName;

    @ApiModelProperty(value = "台账类别")
    @LovValue(value = "HME.LEDGER_TYPE", meaningField = "ledgerTypeMeaning")
    private String ledgerType;

    @ApiModelProperty(value = "台账类别含义")
    private String ledgerTypeMeaning;

    @ApiModelProperty(value = "设备状态")
    private List<String> equipmentStatusList;
    @ApiModelProperty(value = "台账类别")
    private List<String> ledgerTypeList;
    @ApiModelProperty(value = "资产类别")
    private List<String> assetClassList;
    @ApiModelProperty(value = "使用频次")
    private List<String> frequencyList;

    @ApiModelProperty(value = "保管部门ID")
    private List<String> businessIdList;

    @ApiModelProperty(value = "当前工位ID")
    private List<String> workcellCodeIdList;

    @ApiModelProperty(value = "设备类别")
    private List<String> equipmentCategoryList;

    @ApiModelProperty(value = "设备类型")
    private List<String> equipmentTypeList;

    @ApiModelProperty(value = "资产编码-加密")
    private String encryptionAssetEncoding;

    @ApiModelProperty(value = "资产编码集合")
    private List<String> assetEncodingList;

    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "盘点单ID")
    private String stocktakeId;
    @ApiModelProperty(value = "盘点状态")
    private String stocktakeStatus;
    @ApiModelProperty(value = "盘点类型", required = true)
    private String stocktakeType;
    @ApiModelProperty(value = "盘点范围")
    private Integer stocktakeRange;

    public void initParam() {
        assetEncodingList = StringUtils.isNotBlank(this.getAssetEncoding()) ? Arrays.asList(StringUtils.split(this.getAssetEncoding(), ",")) : null;
    }
}
