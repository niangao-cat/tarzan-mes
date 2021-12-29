package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeEoJobSn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.order.domain.entity.MtEo;

import java.io.*;
import java.util.Map;

/**
 * @Classname HmeEoJobSnVO24
 * @Description HmeEoJobSnVO24
 * @Date 2021/1/8 11:40
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobSnVO24 implements Serializable {
    private static final long serialVersionUID = -7261297931601699199L;

    private Long userId;

    @ApiModelProperty("返修条码信息")
    private HmeMaterialLotVO3 reworkMaterialLot;

    @ApiModelProperty("当前作业")
    private HmeEoJobSn hmeEoJobSnEntity;

    @ApiModelProperty("EO 工艺路线 装配清单关系")
    HmeEoRouterBomRelVO eoRouterBomRel;

    @ApiModelProperty("返修条码对应的EO")
    private MtEo reworkEo;

    @ApiModelProperty("不良记录数据项")
    Map<String,HmeEoJobDataRecordVO2> ncJobDataRecordMap;

    @ApiModelProperty("key:WoId, value:wo")
    private Map<String, HmeWorkOrderVO2> woMap;
}