package com.ruike.qms.app.service;

import com.ruike.qms.api.dto.QmsSampleSizeCodeLetterDTO;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 样本量字码表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-04-30 15:05:11
 */
public interface QmsSampleSizeCodeLetterService {

    /**
     * 获取列表
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param lotSize Long
     * @param pageRequest pageRequest
     * @return Page<QmsSampleSizeCodeLetterDTO>
     */
    Page<QmsSampleSizeCodeLetterDTO> listSampleSizeCodeLetterForUi(Long tenantId, Long  lotSize, PageRequest pageRequest);

    /**
     * 保存
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param dtoList QmsSampleSizeCodeLetterDTO
     * @return
     */
    void saveSampleSizeCodeLetterForUi(Long tenantId, List<QmsSampleSizeCodeLetterDTO> dtoList);
}
