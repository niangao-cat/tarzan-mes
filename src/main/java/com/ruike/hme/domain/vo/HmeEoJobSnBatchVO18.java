package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ruike.hme.domain.entity.HmeEoJobLotMaterial;
import com.ruike.hme.domain.entity.HmeEoJobMaterial;
import com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial;
import com.ruike.hme.domain.entity.HmeEoJobTimeMaterial;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.actual.domain.vo.MtAssembleProcessActualVO11;

@Data
public class HmeEoJobSnBatchVO18 implements Serializable {
    private static final long serialVersionUID = 6524552934189826639L;
    @ApiModelProperty(value = "事务列表")
    List<WmsObjectTransactionRequestVO> objectTransactionRequestList;
    @ApiModelProperty(value = "序列物料更新列表")
    List<HmeEoJobMaterial> updateSnDataList;
    @ApiModelProperty(value = "批次物料删除列表")
    List<HmeEoJobLotMaterial> deleteLotDataList;
    @ApiModelProperty(value = "时效物料删除列表")
    List<HmeEoJobTimeMaterial> deleteTimeDataList;
    @ApiModelProperty(value = "投料记录列表")
    List<HmeEoJobSnLotMaterial> eoJobSnInsertDataList;
    @ApiModelProperty(value = "批量装配API条码列表")
    List<MtAssembleProcessActualVO11> materialInfoLis;
    @ApiModelProperty(value = "升级物料列表")
    List<HmeEoJobSnVO21> upGradeMaterialLotList;
    @ApiModelProperty(value = "正常料的组件物料")
    List<HmeEoJobSnBatchVO4> normalList;
}
