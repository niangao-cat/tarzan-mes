package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeFreezePrivilegeQueryDTO;
import com.ruike.hme.domain.entity.HmeFreezePrivilege;
import com.ruike.hme.domain.repository.HmeFreezePrivilegeRepository;
import com.ruike.hme.domain.vo.HmeFreezePrivilegeVO;
import com.ruike.hme.infra.mapper.HmeFreezePrivilegeMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 条码冻结权限 资源库实现
 *
 * @author yonghui.zhu@hand-china.com 2021-02-26 17:41:20
 */
@Component
public class HmeFreezePrivilegeRepositoryImpl extends BaseRepositoryImpl<HmeFreezePrivilege> implements HmeFreezePrivilegeRepository {

    private final HmeFreezePrivilegeMapper mapper;

    public HmeFreezePrivilegeRepositoryImpl(HmeFreezePrivilegeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @ProcessLovValue
    public Page<HmeFreezePrivilegeVO> byCondition(Long tenantId, HmeFreezePrivilegeQueryDTO dto, PageRequest pageRequest) {
        Page<HmeFreezePrivilegeVO> page = PageHelper.doPage(pageRequest, () -> mapper.selectRepresentationList(tenantId, dto));
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            AtomicInteger seqGen = new AtomicInteger(0);
            page.getContent().forEach(r -> r.setSequenceNum(seqGen.incrementAndGet()));
        }
        return page;
    }

    @Override
    public HmeFreezePrivilegeVO byId(Long tenantId, String privilegeId) {
        HmeFreezePrivilegeQueryDTO dto = new HmeFreezePrivilegeQueryDTO();
        dto.setPrivilegeId(privilegeId);
        List<HmeFreezePrivilegeVO> list = mapper.selectRepresentationList(tenantId, dto);
        return CollectionUtils.isEmpty(list) ? new HmeFreezePrivilegeVO() : list.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public HmeFreezePrivilegeVO save(HmeFreezePrivilege entity) {
        if (StringUtils.isBlank(entity.getPrivilegeId())) {
            this.insertSelective(entity);
        } else {
            mapper.updateByPrimaryKeySelective(entity);
        }
        return byId(entity.getTenantId(), entity.getPrivilegeId());
    }

    @Override
    public HmeFreezePrivilege byUserId(Long tenantId, Long userId) {
        HmeFreezePrivilege condition = new HmeFreezePrivilege();
        condition.setUserId(userId);
        return this.selectOne(condition);
    }
}
