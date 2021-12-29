package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeQuantityAnalyzeDocDTO;
import com.ruike.hme.app.service.HmeQuantityAnalyzeDocService;
import com.ruike.hme.domain.repository.HmeQuantityAnalyzeDocRepository;
import com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO;
import com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 质量文件头表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-01-19 11:10:06
 */
@Service
public class HmeQuantityAnalyzeDocServiceImpl implements HmeQuantityAnalyzeDocService {

    @Autowired
    private HmeQuantityAnalyzeDocRepository hmeQuantityAnalyzeDocRepository;

    @Override
    @Deprecated
    public Page<HmeQuantityAnalyzeDocVO> quantityAnalyzeDocQuery(Long tenantId, HmeQuantityAnalyzeDocDTO dto, PageRequest pageRequest) {
        return hmeQuantityAnalyzeDocRepository.quantityAnalyzeDocQuery(tenantId, dto, pageRequest);
    }

    @Override
    public Page<HmeQuantityAnalyzeDocVO2> quantityAnalyzeLineQuery(Long tenantId, String docId, PageRequest pageRequest) {
        return hmeQuantityAnalyzeDocRepository.quantityAnalyzeLineQuery(tenantId, docId, pageRequest);
    }
}
