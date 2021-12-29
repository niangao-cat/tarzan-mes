package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.vo.HmeTagFormulaHeadVO;
import com.ruike.hme.domain.vo.HmeTagFormulaLineVO;
import com.ruike.hme.infra.mapper.HmeTagFormulaHeadMapper;
import com.ruike.hme.infra.mapper.HmeTagFormulaLineMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeTagFormulaLine;
import com.ruike.hme.domain.repository.HmeTagFormulaLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 数据采集项公式行表 资源库实现
 *
 * @author guiming.zhou@hand-china.com 2020-09-23 10:04:56
 */
@Component
public class HmeTagFormulaLineRepositoryImpl extends BaseRepositoryImpl<HmeTagFormulaLine> implements HmeTagFormulaLineRepository {

    @Autowired
    private HmeTagFormulaLineMapper hmeTagFormulaLineMapper;

    @Override
    public Page<HmeTagFormulaLineVO> getLineList(Long tenantId, String tagFormulaHeadId, PageRequest pageRequest) {
        Page<HmeTagFormulaLineVO> page = PageHelper.doPage(pageRequest,() -> hmeTagFormulaLineMapper.getTagLineList(tenantId, tagFormulaHeadId));
        return page;
    }

    @Override
    public void deleteByHeadId(Long tenantId, String tagFormulaHeadId) {
        hmeTagFormulaLineMapper.deleteByHeadId(tenantId,tagFormulaHeadId);
    }
}
