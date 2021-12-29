package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeCosSelectionCurrentDTO;
import com.ruike.hme.domain.vo.HmeCosSelectionCurrentVO;
import com.ruike.hme.domain.vo.HmeCosSelectionCurrentVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeCosSelectionCurrent;

/**
 * COS筛选电流点维护表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-08-18 11:07:41
 */
public interface HmeCosSelectionCurrentRepository extends BaseRepository<HmeCosSelectionCurrent> {

    /**
     * 分页查询COS筛选电流点维护信息
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/18 11:49:39
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeCosSelectionCurrentVO>
     */
    Page<HmeCosSelectionCurrentVO> cosSelectionCurrentPageQuery(Long tenantId, HmeCosSelectionCurrentDTO dto, PageRequest pageRequest);

    /**
     * 创建或者更新COS筛选电流点维护信息
     *
     * @param tenantId 租户ID
     * @param dto 创建或者更新信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/18 01:43:56
     * @return void
     */
    void cosSelectionCurrentCreateOrUpdate(Long tenantId, HmeCosSelectionCurrentVO dto);

    /**
     * 分页查询历史数据
     *
     * @param tenantId
     * @param cosId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/18 02:44:02
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeCosSelectionCurrentVO2>
     */
    Page<HmeCosSelectionCurrentVO2> cosSelectionCurrentHisPageQuery(Long tenantId, String cosId, PageRequest pageRequest);
}
