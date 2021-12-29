package com.ruike.hme.domain.repository;

import java.math.BigDecimal;
import java.util.*;

import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeOperationTimeObject;

/**
 * 时效要求关联对象表资源库
 *
 * @author chaonan.hu@hand-china.com 2020-08-11 11:44:08
 */
public interface HmeOperationTimeObjectRepository extends BaseRepository<HmeOperationTimeObject> {
    /**
     * 获取SN的时效时长
     * 逻辑：
     *  1.基于工位查找hme_operation_time表
     *      1.1.基于工位+SN获取
     *      1.2.基于工位+wo获取
     *      1.3.基于工位+物料+物料版本获取
     *      1.4.基于工位+物料获取
     *      1.5.基于工位获取
     *  2.基于工艺查找hme_operation_time表
     *      2.1.基于工艺+SN获取
     *      2.2.基于工艺+wo获取
     *      2.3.基于工艺+物料+物料版本获取
     *      2.4.基于工艺+物料获取
     *      2.5.基于工艺获取
     *  3.基于工位、工艺均为找到数据，报错
     * @author penglin.sui@hand-china.com 2020-08-18 15:46
     */
    public BigDecimal StandardReqdTimeInProcessGet(Long tenantId , HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 批量查询所有SN时效时长
     *
     * @author yuchao.wang
     * @date 2020/10/21 20:39
     * @param tenantId 租户ID
     * @param dtos 参数
     * @return java.util.Map<java.lang.String,java.math.BigDecimal>
     *
     */
    Map<String, BigDecimal> StandardReqdTimeInProcessBatchGet(Long tenantId , List<HmeEoJobSnVO3> dtos);
}
