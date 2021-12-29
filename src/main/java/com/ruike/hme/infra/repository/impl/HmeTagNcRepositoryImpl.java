package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeTagNcDTO;
import com.ruike.hme.domain.vo.HmeTagFormulaHeadVO;
import com.ruike.hme.infra.mapper.HmeTagNcMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeTagNc;
import com.ruike.hme.domain.repository.HmeTagNcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 数据项不良判定基础表 资源库实现
 *
 * @author guiming.zhou@hand-china.com 2020-09-24 16:00:30
 */
@Component
public class HmeTagNcRepositoryImpl extends BaseRepositoryImpl<HmeTagNc> implements HmeTagNcRepository {

    @Autowired
    private HmeTagNcMapper hmeTagNcMapper;

    @Override
    public Page<HmeTagNcDTO> getTagNcList(Long tenantId, HmeTagNc hmeTagNc, PageRequest pageRequest) {
        Page<HmeTagNcDTO> page = PageHelper.doPage(pageRequest,() -> hmeTagNcMapper.getTagNcList(tenantId, hmeTagNc));
        return page;
    }
}
