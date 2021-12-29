package com.ruike.hme.domain.vo;

import com.ruike.hme.api.dto.HmeEoJobDataCalculationResultDTO;
import com.ruike.hme.domain.entity.*;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import lombok.Data;
import tarzan.actual.domain.vo.MtAssembleProcessActualVO11;
import tarzan.actual.domain.vo.MtEoComponentActualVO5;
import tarzan.actual.domain.vo.MtWoComponentActualVO1;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotVO1;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;

import java.io.*;
import java.util.*;

@Data
public class HmeEoJobSnSingleVO4 implements Serializable {
    private static final long serialVersionUID = -1918918021917889974L;
    @ApiModelProperty(value = "事务API参数")
    List<WmsObjectTransactionRequestVO> objectTransactionRequestList;
    @ApiModelProperty(value = "物料批消耗参数")
    Map<String, MtMaterialLotVO1> materialLotConsumeMap;
    @ApiModelProperty(value = "序列物料更新参数")
    List<HmeEoJobMaterial> updateSnDataList;
    @ApiModelProperty(value = "批次物料更新参数")
    List<HmeEoJobLotMaterial> deleteLotDataList;
    @ApiModelProperty(value = "时效物料更新参数")
    List<HmeEoJobTimeMaterial> deleteTimeDataList;
    @ApiModelProperty(value = "投料记录参数-批次、时效")
    List<HmeEoJobSnLotMaterial> eoJobSnInsertDataList;
    @ApiModelProperty(value = "投料记录参数-SN")
    List<HmeEoJobMaterial> eoJobMaterialInsertDataList;
    @ApiModelProperty(value = "升级物料条码参数")
    List<HmeEoJobSnVO21> upGradeMaterialLotList;
    @ApiModelProperty(value = "是否执行投料")
    Boolean execReleaseFlag;
    @ApiModelProperty(value = "物料信息")
    List<MtAssembleProcessActualVO11> materialInfoList;
    @ApiModelProperty(value = "条码更新参数")
    List<MtMaterialLotVO20> updateMaterialLotList;
    @ApiModelProperty(value = "更新投料记录剩余数量参数")
    List<HmeEoJobSnLotMaterial> updateRemainQtyList;
    @ApiModelProperty(value = "更新现有量参数")
    List<MtInvOnhandQuantityVO9> mtInvOnhandQuantityVO9List;
    @ApiModelProperty(value = "COS类型虚拟号")
    List<HmeEoJobSnSingleVO6> cosVirtualNumList;
    @ApiModelProperty(value = "数据采集项计算")
    List<HmeEoJobDataCalculationResultDTO> hmeEoJobDataCalculationResultDTOList;
    @ApiModelProperty(value = "装载位置")
    List<HmeMaterialLotLoad> hmeMaterialLotLoadList;
    @ApiModelProperty(value = "新增条码实验代码")
    List<HmeMaterialLotLabCode> insertMaterialLotLabCodeList;
    @ApiModelProperty(value = "更新条码实验代码")
    List<HmeMaterialLotLabCode> updateMaterialLotLabCodeList;
    @ApiModelProperty(value = "指定工艺路线返修-EO装配参数")
    List<MtEoComponentActualVO5> eoComponentActualVO5List;
    @ApiModelProperty(value = "指定工艺路线返修-WO装配参数")
    List<MtWoComponentActualVO1> woComponentActualVO1List;
    @ApiModelProperty(value = "条码扩展属性")
    List<MtCommonExtendVO6> materialLotAttrList;
}
