package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeQuantityAnalyzeDocDTO;
import com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO;
import com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 质量文件头表应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-01-19 11:10:06
 */
public interface HmeQuantityAnalyzeDocService {

    /**
     * 质量文件头表数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/19 14:04:58
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO>
     */
    @Deprecated
    Page<HmeQuantityAnalyzeDocVO> quantityAnalyzeDocQuery(Long tenantId, HmeQuantityAnalyzeDocDTO dto, PageRequest pageRequest);

    /**
     * 质量文件表数据查询
     *
     * @param tenantId 租户ID
     * @param docId 头ID
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/19 15:42:04
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO2>
     */
    Page<HmeQuantityAnalyzeDocVO2> quantityAnalyzeLineQuery(Long tenantId, String docId, PageRequest pageRequest);
}
