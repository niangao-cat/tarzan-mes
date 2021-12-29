package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ruike.hme.domain.entity.HmeEoJobMaterial;
import com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HmeEoJobSnLotMaterialVO2 implements Serializable {

    private static final long serialVersionUID = -9061732901068791355L;
    @ApiModelProperty("eo批次/时效物料投料记录")
    List<HmeEoJobSnLotMaterial> jobSnLotList;
    @ApiModelProperty("eo批次/时效物料记录")
    List<HmeEoJobSnLotMaterialVO> hmeEoJobSnLotMaterialVOList;
    @ApiModelProperty("序列物料记录")
    List<HmeEoJobMaterial> hmeEoJobMaterialList;
    @ApiModelProperty("序列物料虚拟件记录")
    List<HmeEoJobMaterial> hmeEoJobVirtualMaterialList;
}
