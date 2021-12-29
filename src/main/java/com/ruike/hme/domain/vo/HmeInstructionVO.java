package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.instruction.domain.vo.MtInstructionVO6;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/8/26 17:14
 */
@Data
public class HmeInstructionVO extends MtInstructionVO6 implements Serializable {

    @ApiModelProperty("物料批信息")
    private List<HmeInStorageMaterialVO> materialLotListList;

}
