package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeMaterialLotLabCode;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

@Data
public class HmeEoJobSnSingleVO2 implements Serializable {
    private static final long serialVersionUID = 3871727501421517829L;
    @ApiModelProperty(value = "条码扩展属性")
    Map<String,String> materialLotAttrMap;
    @ApiModelProperty(value = "条码实验代码")
    Map<String,List<HmeMaterialLotLabCode>> hmeMaterialLotLabCodeMap;
    @ApiModelProperty(value = "虚拟件组件")
    Map<String,List<HmeEoJobSnBatchVO6>> virtualComponentMaterialLotMap;
    @ApiModelProperty(value = "虚拟件工序作业")
    Map<String,String> virtualJobMap;
    @ApiModelProperty(value = "虚拟件条码工序作业")
    Map<String, HmeEoJobSn> virtualEoJobSnMap;
}
