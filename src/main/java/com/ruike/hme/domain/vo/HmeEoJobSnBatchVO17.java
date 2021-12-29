package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;

/**
 *
 * @Description 已投信息
 *
 * @author penglin.sui
 * @date 2020/11/26 17:20
 *
 */
@Data
public class HmeEoJobSnBatchVO17 implements Serializable {
    private static final long serialVersionUID = 6873731684423820919L;
    Map<String,HmeEoJobSnBatchVO15> releasedRecordMap;
    Map<String, MtEoComponentActual> mtEoComponentActualMap;
    Map<String, MtWorkOrderComponentActual> mtWoComponentActualMap;
}
