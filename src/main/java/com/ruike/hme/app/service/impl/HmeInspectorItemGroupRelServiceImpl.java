package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeInspectorItemGroupRelDTO;
import com.ruike.hme.app.service.HmeInspectorItemGroupRelService;
import com.ruike.hme.domain.entity.HmeInspectorItemGroupRel;
import com.ruike.hme.domain.repository.HmeInspectorItemGroupRelRepository;
import com.ruike.hme.domain.vo.HmeInspectorItemGroupRelVO;
import com.ruike.hme.infra.mapper.HmeInspectorItemGroupRelMapper;
import com.ruike.wms.domain.entity.WmsItemGroup;
import com.ruike.wms.domain.repository.WmsItemGroupRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 检验员与物料组关系表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-03-29 13:44:29
 */
@Service
public class HmeInspectorItemGroupRelServiceImpl implements HmeInspectorItemGroupRelService {

    @Autowired
    private HmeInspectorItemGroupRelMapper hmeInspectorItemGroupRelMapper;
    @Autowired
    private HmeInspectorItemGroupRelRepository hmeInspectorItemGroupRelRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtUserRepository mtUserRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private WmsItemGroupRepository wmsItemGroupRepository;

    @Override
    @ProcessLovValue
    public Page<HmeInspectorItemGroupRelVO> relPageQuery(Long tenantId, HmeInspectorItemGroupRelDTO dto, PageRequest pageRequest) {
        Page<HmeInspectorItemGroupRelVO> resultPage = PageHelper.doPage(pageRequest, () -> hmeInspectorItemGroupRelMapper.relPageQuery(tenantId, dto));
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeInspectorItemGroupRelVO> relCreateOrUpdate(Long tenantId, List<HmeInspectorItemGroupRelVO> dtoList) {
        for (HmeInspectorItemGroupRelVO dto:dtoList) {
            HmeInspectorItemGroupRel hmeInspectorItemGroupRelDb = hmeInspectorItemGroupRelRepository.selectOne(new HmeInspectorItemGroupRel() {{
                setTenantId(tenantId);
                setUserId(Long.parseLong(dto.getUserId()));
                setPrivilegeType(dto.getPrivilegeType());
                setItemGroupId(dto.getItemGroupCode());
            }});
            if(StringUtils.isBlank(dto.getRelId())){
                //新建，唯一性校验
                if(Objects.nonNull(hmeInspectorItemGroupRelDb)){
                    //如果用户+权限类型+物料组已存在，则报错
                    MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.parseLong(dto.getUserId()));
                    String privilegeTypeMeaning = lovAdapter.queryLovMeaning("QMS.INSPECT_POWER_TYPE", tenantId, dto.getPrivilegeType());
                    throw new MtException("HME_INSPECTOR_ITEM_GROUP_REL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_INSPECTOR_ITEM_GROUP_REL_0001", "HME", mtUserInfo.getLoginName(), privilegeTypeMeaning, dto.getItemGroupCode()));
                }
                HmeInspectorItemGroupRel hmeInspectorItemGroupRel = new HmeInspectorItemGroupRel();
                hmeInspectorItemGroupRel.setTenantId(tenantId);
                BeanUtils.copyProperties(dto, hmeInspectorItemGroupRel);
                hmeInspectorItemGroupRel.setUserId(Long.parseLong(dto.getUserId()));
                hmeInspectorItemGroupRel.setItemGroupId(dto.getItemGroupCode());
                hmeInspectorItemGroupRelRepository.insertSelective(hmeInspectorItemGroupRel);
            }else{
                //更新，唯一性校验
                if(Objects.nonNull(hmeInspectorItemGroupRelDb) && !hmeInspectorItemGroupRelDb.getRelId().equals(dto.getRelId())){
                    //如果用户+权限类型+物料组已存在，则报错
                    MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.parseLong(dto.getUserId()));
                    String privilegeTypeMeaning = lovAdapter.queryLovMeaning("QMS.INSPECT_POWER_TYPE", tenantId, dto.getPrivilegeType());
                    throw new MtException("HME_INSPECTOR_ITEM_GROUP_REL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_INSPECTOR_ITEM_GROUP_REL_0001", "HME", mtUserInfo.getLoginName(), privilegeTypeMeaning, dto.getItemGroupCode()));
                }
                HmeInspectorItemGroupRel hmeInspectorItemGroupRel = hmeInspectorItemGroupRelRepository.selectByPrimaryKey(dto.getRelId());
                BeanUtils.copyProperties(dto, hmeInspectorItemGroupRel);
                hmeInspectorItemGroupRel.setUserId(Long.parseLong(dto.getUserId()));
                hmeInspectorItemGroupRel.setItemGroupId(dto.getItemGroupCode());
                hmeInspectorItemGroupRelMapper.updateByPrimaryKeySelective(hmeInspectorItemGroupRel);
            }
        }
        return dtoList;
    }
}
