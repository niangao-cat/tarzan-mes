package com.ruike.qms.app.service;

import com.ruike.qms.api.dto.*;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 抽样方案表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-04-30 15:05:10
 */
public interface QmsSampleSchemeService {

    /**
     * 获取列表
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param dto QmsSampleSchemeDTO2
     * @param pageRequest pageRequest
     * @return Page<QmsSampleSizeCodeLetterDTO>
     */
    Page<QmsSampleSchemeDTO> listSampleSchemeForUi(Long tenantId, QmsSampleSchemeDTO2 dto, PageRequest pageRequest);

    /**
     * 保存
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param dto QmsSampleSchemeDTO
     * @return
     */
    String saveSampleSchemeForUi(Long tenantId, QmsSampleSchemeDTO3 dto);

    /**
     * 抽样方案LOV查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/15 08:48:38
     * @return io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsSampleSchemeDTO>
     */
    Page<QmsSampleSchemeDTO> sampleSchemeLovQuery(Long tenantId, QmsSampleSchemeDTO4 dto, PageRequest pageRequest);
}
