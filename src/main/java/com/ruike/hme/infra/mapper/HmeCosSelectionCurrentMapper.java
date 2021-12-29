package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosSelectionCurrentDTO;
import com.ruike.hme.domain.entity.HmeCosSelectionCurrent;
import com.ruike.hme.domain.vo.HmeCosSelectionCurrentVO;
import com.ruike.hme.domain.vo.HmeCosSelectionCurrentVO2;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * COS筛选电流点维护表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-08-18 11:07:41
 */
public interface HmeCosSelectionCurrentMapper extends BaseMapper<HmeCosSelectionCurrent> {

    /**
     * 查询COS筛选电流点维护信息
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/18 11:53:59
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosSelectionCurrentVO>
     */
    List<HmeCosSelectionCurrentVO> cosSelectionCurrentPageQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeCosSelectionCurrentDTO dto);

    /**
     * 根据COSID查询历史数据
     *
     * @param tenantId 租户ID
     * @param cosId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/18 02:32:12
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosSelectionCurrentVO2>
     */
    List<HmeCosSelectionCurrentVO2> cosSelectionCurrentHisPageQuery(@Param("tenantId") Long tenantId, @Param("cosId") String cosId);
}
