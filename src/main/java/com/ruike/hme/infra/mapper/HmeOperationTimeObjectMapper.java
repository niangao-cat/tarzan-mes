package com.ruike.hme.infra.mapper;

import java.math.BigDecimal;

import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import feign.Param;
import com.ruike.hme.domain.entity.HmeOperationTimeObject;
import io.choerodon.mybatis.common.BaseMapper;

/**
 * 时效要求关联对象表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020-08-11 11:44:08
 */
public interface HmeOperationTimeObjectMapper extends BaseMapper<HmeOperationTimeObject> {
    /**
     * 基于工位/工艺+SN获取时效时长
     * @param tenantId   租户
     * @param dto HmeEoJobSnVO3
     * @param type 类型
     * @return
     * @author penglin.sui@hand-china.com 2020-08-20 16:09
     */
    BigDecimal queryStandardReqdTimeInProcessOfWkcSn(@Param("tenantId") Long tenantId, @Param("dto") HmeEoJobSnVO3 dto, @Param("type") String type);

    /**
     * 基于工位+wo获取时效时长
     * @param tenantId   租户
     * @param dto HmeEoJobSnVO3
     * @param type 类型
     * @return
     * @author penglin.sui@hand-china.com 2020-08-20 16:09
     */
    BigDecimal queryStandardReqdTimeInProcessOfWkcWo(@Param("tenantId") Long tenantId, @Param("dto") HmeEoJobSnVO3 dto, @Param("type") String type);

    /**
     * 基于工位+物料+版本获取时效时长
     * @param tenantId   租户
     * @param dto HmeEoJobSnVO3
     * @param type 类型
     * @return
     * @author penglin.sui@hand-china.com 2020-08-20 16:09
     */
    BigDecimal queryStandardReqdTimeInProcessOfWkcMaterialVersion(@Param("tenantId") Long tenantId, @Param("dto") HmeEoJobSnVO3 dto, @Param("type") String type);

    /**
     * 基于工位+物料获取时效时长
     * @param tenantId   租户
     * @param dto HmeEoJobSnVO3
     * @param type 类型
     * @return
     * @author penglin.sui@hand-china.com 2020-08-20 16:09
     */
    BigDecimal queryStandardReqdTimeInProcessOfWkcMaterial(@Param("tenantId") Long tenantId, @Param("dto") HmeEoJobSnVO3 dto, @Param("type") String type);

    /**
     * 基于工位/工艺+SN获取时效时长
     * @param tenantId   租户
     * @param dto HmeEoJobSnVO3
     * @param type 类型
     * @return
     * @author penglin.sui@hand-china.com 2020-08-20 16:09
     */
    BigDecimal queryStandardReqdTimeInProcessOfWkcOrOperation(@Param("tenantId") Long tenantId, @Param("dto") HmeEoJobSnVO3 dto, @Param("type") String type);
}
