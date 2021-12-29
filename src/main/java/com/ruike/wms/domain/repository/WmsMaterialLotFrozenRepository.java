package com.ruike.wms.domain.repository;

import java.util.List;

import com.ruike.wms.api.dto.WmsMaterialLotFrozenDTO;
import com.ruike.wms.domain.vo.WmsMaterialLotVO;
import com.ruike.wms.domain.vo.WmsMaterialLotVO2;
import com.ruike.wms.domain.vo.WmsMaterialLotVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * WmsMaterialLotFrozenRepository
 *
 * @author liyuan.lv@hand-china.com 2020/04/28 10:22
 */
public interface WmsMaterialLotFrozenRepository {
    /**
     * 获取符合条件的条码信息
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return
     */
    Page<WmsMaterialLotVO2> queryForMaterialLot(Long tenantId, WmsMaterialLotFrozenDTO dto, PageRequest pageRequest);

    /**
     * 根据条件查询物料批
     * @param tenantId
     * @param dto
     * @return
     */
    List<WmsMaterialLotVO2> selectMaterialLotByCondition(Long tenantId, WmsMaterialLotVO dto);

    /**
     * 根据物料批Id列表查询物料批
     * @param tenantId
     * @param materialLotIds
     * @return
     */
    List<WmsMaterialLotVO2> selectMaterialLotByIds(Long tenantId, List<String> materialLotIds);

    /**
     * 执行冻结/解冻
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<WmsMaterialLotVO2> executeFreeze(Long tenantId, WmsMaterialLotVO3 dto);
}
