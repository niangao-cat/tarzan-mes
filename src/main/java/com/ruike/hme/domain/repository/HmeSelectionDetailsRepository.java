package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmePreSelectionReturnDTO8;
import com.ruike.hme.domain.vo.HmeSelectionDetailsQueryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeSelectionDetails;

import java.util.List;

/**
 * 预挑选明细表资源库
 *
 * @author wenzhang.yu@hand-china.com 2020-08-19 15:44:45
 */
public interface HmeSelectionDetailsRepository extends BaseRepository<HmeSelectionDetails> {

    /**
     * 预挑选明细列表
     *
     * @param tenantId
     * @param pageRequest
     * @param hmeSelectionDetailsQueryVO
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmePreSelectionReturnDTO8>
     * @author sanfeng.zhang@hand-china.com 2020/12/26 14:41
     */
    Page<HmePreSelectionReturnDTO8> selectionDetailsQuery(Long tenantId, PageRequest pageRequest, HmeSelectionDetailsQueryVO hmeSelectionDetailsQueryVO);

    /**
     * 预挑选明细导出
     *
     * @param tenantId
     * @param hmeSelectionDetailsQueryVO
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO8>
     * @author sanfeng.zhang@hand-china.com 2020/12/26 14:42
     */
    List<HmePreSelectionReturnDTO8> selectionDetailsExport(Long tenantId, HmeSelectionDetailsQueryVO hmeSelectionDetailsQueryVO);
}
