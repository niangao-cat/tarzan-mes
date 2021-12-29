package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;

import com.ruike.hme.domain.entity.HmeEoJobSn;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.vo.MtEoVO20;
import tarzan.order.domain.vo.MtWorkOrderVO8;

/**
 * 批次物料、时效物料投料参数
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HmeEoJobSnLotMaterialVO4 implements Serializable {
    private static final long serialVersionUID = -6426019254316284036L;
    @ApiModelProperty("工序作业")
    HmeEoJobSnVO3 dto;
    @ApiModelProperty("SN作业记录")
    HmeEoJobSn hmeEoJobSn;
    @ApiModelProperty("事件")
    HmeEoJobSnLotMaterialVO3 eventDto;
    @ApiModelProperty("eo组件清单")
    List<MtEoVO20> eoComponentList;
    @ApiModelProperty("wo组件清单")
    List<MtWorkOrderVO8> woComponentList;
    @ApiModelProperty("wo")
    MtWorkOrder mtWorkOrder;
    @ApiModelProperty("WO工艺步骤ID")
    String woRouterStepId;
}
