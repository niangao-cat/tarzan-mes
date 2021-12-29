package tarzan.general.app.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtTagHisDTO;
import tarzan.general.api.dto.MtTagHisDTO1;
import tarzan.general.app.service.MtTagHisService;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtTagHis;
import tarzan.general.domain.repository.MtTagHisRepository;
import tarzan.general.infra.mapper.MtTagHisMapper;

/**
 * 数据收集项历史表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@Service
public class MtTagHisServiceImpl implements MtTagHisService {

    @Autowired
    private MtTagHisRepository mtTagHisRepository;

    @Autowired
    private MtTagHisMapper mtTagHisMapper;

    @Override
    public Page<MtTagHisDTO> queryTagHistory(Long tenantId, MtTagHisDTO1 dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> mtTagHisMapper.queryTagHistory(tenantId, dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTagHistory(Long tenantId, MtTag mtTag, String eventId) {
        MtTagHis mtTagHis = new MtTagHis();
        BeanUtils.copyProperties(mtTag, mtTagHis);
        mtTagHis.setEventId(eventId);
        mtTagHis.setTenantId(tenantId);
        mtTagHisRepository.insertSelective(mtTagHis);
    }
}
