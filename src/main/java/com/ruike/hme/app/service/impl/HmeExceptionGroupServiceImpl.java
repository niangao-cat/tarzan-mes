package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeExceptionGroupDTO;
import com.ruike.hme.app.service.HmeExceptionGroupService;
import com.ruike.hme.domain.entity.HmeExceptionGroup;
import com.ruike.hme.domain.repository.HmeExceptionGroupRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import liquibase.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.modeling.domain.entity.MtModOrganizationRel;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;

import java.util.List;

/**
 * 异常收集组基础数据表应用服务默认实现
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:24
 */
@Service
public class HmeExceptionGroupServiceImpl implements HmeExceptionGroupService {
    private final HmeExceptionGroupRepository repository;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    public HmeExceptionGroupServiceImpl(HmeExceptionGroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<HmeExceptionGroup> listForUi(Long tenantId, HmeExceptionGroupDTO dto, PageRequest pageRequest) {
        if(StringUtils.isNotEmpty(dto.getWorkcellId())){
            //当前台选择工位时，先根据工位找到上层工序
            List<MtModOrganizationRel> mtModOrganizationRelList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
                setTenantId(tenantId);
                setOrganizationId(dto.getWorkcellId());
                setOrganizationType("WORKCELL");
                setParentOrganizationType("WORKCELL");
            }});
            if(CollectionUtils.isNotEmpty(mtModOrganizationRelList)){
                dto.setWorkcellProcessId(mtModOrganizationRelList.get(0).getParentOrganizationId());
            }else{
                dto.setWorkcellProcessId("");
            }
        }
        return PageHelper.doPage(pageRequest, () -> repository.exceptionGroupUiQuery(tenantId, dto));
    }

    @Override
    public HmeExceptionGroup saveForUi(Long tenantId, HmeExceptionGroup dto) {
        return repository.excGroupBasicPropertyUpdate(tenantId, dto);
    }
}
