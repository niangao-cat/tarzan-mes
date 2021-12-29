package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeOperationTimeDto;
import com.ruike.hme.api.dto.HmeOperationTimeDto2;
import com.ruike.hme.api.dto.HmeOperationTimeDto3;
import com.ruike.hme.api.dto.HmeOperationTimeDto4;
import com.ruike.hme.domain.entity.HmeOperationTime;
import com.ruike.hme.domain.vo.*;
import feign.Param;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 工艺时效要求表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020-08-11 11:44:06
 */
public interface HmeOperationTimeMapper extends BaseMapper<HmeOperationTime> {

    /**
     * 分页查询工艺时效要求表
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/11 15:30:54
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOperationTimeVO>
     */
    List<HmeOperationTimeVO> query(@Param("tenantId") Long tenantId, @Param("dto") HmeOperationTimeDto4 dto);

    /**
     * 关联物料查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/11 20:23:12
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOperationTimeVO2>
     */
    List<HmeOperationTimeVO2> queryMaterial(@Param("tenantId") Long tenantId,
                                            @Param("dto") HmeOperationTimeDto dto);

    /**
     * 关联对象查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/12 10:29:00
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOperationTimeVO3>
     */
    List<HmeOperationTimeVO3> queryObject(@Param("tenantId") Long tenantId, @Param("dto") HmeOperationTimeDto2 dto);

    /**
     * 工艺时效历史查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/12 14:00:48
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOperationTimeVO4>
     */
    List<HmeOperationTimeVO4> queryHis(@Param("tenantId") Long tenantId, @Param("dto") HmeOperationTimeDto3 dto);

    /**
     * 关联物料历史查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/12 14:20:56
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOperationTimeVO5>
     */
    List<HmeOperationTimeVO5> queryMaterialHis(@Param("tenantId") Long tenantId, @Param("dto") HmeOperationTimeDto3 dto);

    /**
     * 关联对象历史查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/12 14:32:32
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOperationTimeVO6>
     */
    List<HmeOperationTimeVO6> queryObjectHis(@Param("tenantId") Long tenantId, @Param("dto") HmeOperationTimeDto3 dto);
}
