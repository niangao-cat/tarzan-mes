package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmePreSelectionReturnDTO8;
import com.ruike.hme.domain.entity.HmeSelectionDetails;
import com.ruike.hme.domain.vo.HmeSelectionDetailsQueryVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预挑选明细表Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-08-19 15:44:45
 */
public interface HmeSelectionDetailsMapper extends BaseMapper<HmeSelectionDetails> {

    /**
     * 预挑选明细列表
     *
     * @param tenantId
     * @param hmeSelectionDetailsQueryVO
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO8>
     * @author sanfeng.zhang@hand-china.com 2020/12/26 14:48
     */
    List<HmePreSelectionReturnDTO8> selectionDetailsQuery(@Param("tenantId") Long tenantId, @Param("queryVO") HmeSelectionDetailsQueryVO hmeSelectionDetailsQueryVO);

    /**
     * 汇总波长和功率
     *
     * @param tenantId
     * @param virtualNumList
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO8>
     * @author sanfeng.zhang@hand-china.com 2021/8/10
     */
    List<HmePreSelectionReturnDTO8> summaryCosFunction(Long tenantId, List<String> virtualNumList);
}

