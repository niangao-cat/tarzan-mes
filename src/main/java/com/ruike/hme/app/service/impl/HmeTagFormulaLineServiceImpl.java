package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeTagFormulaLineService;
import com.ruike.hme.domain.repository.HmeTagFormulaLineRepository;
import com.ruike.hme.domain.vo.HmeTagFormulaLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 数据采集项公式行表应用服务默认实现
 *
 * @author guiming.zhou@hand-china.com 2020-09-23 10:04:56
 */
@Service
public class HmeTagFormulaLineServiceImpl implements HmeTagFormulaLineService {

    @Autowired
    private HmeTagFormulaLineRepository hmeTagFormulaLineRepository;

    @Override
    public Page<HmeTagFormulaLineVO> getTagLineList(Long tenantId, String tagFormulaHeadId, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> hmeTagFormulaLineRepository.getLineList(tenantId, tagFormulaHeadId, pageRequest));
    }

    @Override
    public void deleteHmeTagFormulaLine(Long tenantId, String tagFormulaHeadId) {
        //删除公式头
        hmeTagFormulaLineRepository.deleteByHeadId(tenantId,tagFormulaHeadId);
    }
}
