package com.ruike.hme.domain.repository;

import java.util.List;

import com.ruike.hme.api.dto.HmeEoWorkingDTO;
import com.ruike.hme.domain.vo.HmeEoWorkingVO;
import com.ruike.hme.domain.vo.HmeEoWorkingVO2;
import com.ruike.hme.domain.vo.HmeModAreaVO2;
import com.ruike.hme.domain.vo.HmeProductionLineVO;

/**
 * HmeEoWorkingRepository
 *
 * @author liyuan.lv@hand-china.com 2020/04/24 15:58
 */
public interface HmeEoWorkingRepository {

    /**
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return List<HmeEoWorkingVO>
     */
    List<HmeEoWorkingVO> queryForEoWorking(Long tenantId, HmeEoWorkingDTO dto);

    /**
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return List<HmeEoWorkingVO>
     */
    List<HmeEoWorkingVO> queryForEoWorkingNew(Long tenantId, HmeEoWorkingDTO dto);

    /**
     *
     * @param tenantId 租户ID
     * @param dto 查询参数
     * @return List<HmeModAreaVO2>
     */
    List<HmeModAreaVO2> queryForWorkshop(Long tenantId, HmeEoWorkingDTO dto);

    /**
     *
     * @param tenantId 租户ID
     * @param dto 查询参数
     * @return List<HmeProductionLineVO>
     */
    List<HmeProductionLineVO> queryForProductionLine(Long tenantId, HmeEoWorkingDTO dto);
}
