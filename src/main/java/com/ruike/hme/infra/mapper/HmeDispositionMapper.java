package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeDispositionFunctionDTO;
import com.ruike.hme.api.dto.HmeDispositionGroupDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description 处置方法与处置组功能Mapper
 *
 * @author quan.luo@hand-china.com 2020/11/24 10:02
 */
public interface HmeDispositionMapper {

    /**
     * 列表条件查询
     *
     * @param tenantId                  租户id
     * @param hmeDispositionFunctionDTO 条件
     * @return 数据
     */
    List<HmeDispositionFunctionDTO> queryFunctionByCondition(@Param("tenantId") Long tenantId,
                                                             @Param("hmeDispositionFunctionDTO") HmeDispositionFunctionDTO hmeDispositionFunctionDTO);

    /**
     * 条件查询数据
     *
     * @param tenantId               租户id
     * @param hmeDispositionGroupDTO 查询数据
     * @return 数据
     */
    List<HmeDispositionGroupDTO> queryGroupByCondition(@Param("tenantId") Long tenantId,
                                                       @Param("hmeDispositionGroupDTO") HmeDispositionGroupDTO hmeDispositionGroupDTO);

    /**
     * 处置方法组行信息查询
     *
     * @param tenantId           租户id
     * @param dispositionGroupId 头id
     * @return 数据
     */
    List<HmeDispositionFunctionDTO> groupDetailQuery(@Param("tenantId") Long tenantId, @Param("dispositionGroupId") String dispositionGroupId);
}
