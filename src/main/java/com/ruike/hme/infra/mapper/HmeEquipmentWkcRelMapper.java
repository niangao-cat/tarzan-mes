package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeEquipmentWkcRelDTO;
import com.ruike.hme.domain.entity.HmeEquipmentWkcRel;
import com.ruike.hme.domain.vo.HmeEquipWkcRelReturnVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备工位关系表Mapper
 *
 * @author han.zhang03@hand-china.com 2020-06-09 11:32:08
 */
public interface HmeEquipmentWkcRelMapper extends BaseMapper<HmeEquipmentWkcRel> {
    /**
     * 查询基础数据
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipWkcRelReturnVO>
     */
    List<HmeEquipWkcRelReturnVO> queryBaseData(@Param("tenantId") Long tenantId, @Param("dto") HmeEquipmentWkcRelDTO dto);
}
