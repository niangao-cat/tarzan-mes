package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeMaterialLotLabCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnBatchVO12 implements Serializable {
    private static final long serialVersionUID = -134664469600210679L;
    @ApiModelProperty(value = "条码扩展属性")
    Map<String,String> materialLotAttrMap;
    @ApiModelProperty(value = "虚拟件组件")
    Map<String, List<HmeEoJobSnBatchVO6>> virtualComponentMaterialLotMap;
    @ApiModelProperty(value = "虚拟件工序作业")
    Map<String,String> virtualJobMap;
    @ApiModelProperty(value = "虚拟件条码工序作业")
    Map<String, HmeEoJobSn> virtualEoJobSnMap;
    @ApiModelProperty(value = "进站条码实验代码")
    List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList;
    @ApiModelProperty(value = "投料SN条码实验代码")
    List<HmeMaterialLotLabCode> releaseMaterialLotLabCodeList;
}
