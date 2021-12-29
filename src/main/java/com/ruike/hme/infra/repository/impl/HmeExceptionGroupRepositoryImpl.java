package com.ruike.hme.infra.repository.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;

import com.ruike.hme.api.dto.HmeExceptionGroupDTO;
import com.ruike.hme.domain.entity.HmeExcGroupAssign;
import com.ruike.hme.domain.entity.HmeExcGroupRouter;
import com.ruike.hme.domain.entity.HmeExcGroupWkcAssign;
import com.ruike.hme.domain.entity.HmeExceptionGroup;
import com.ruike.hme.domain.repository.HmeExcGroupAssignRepository;
import com.ruike.hme.domain.repository.HmeExcGroupRouterRepository;
import com.ruike.hme.domain.repository.HmeExcGroupWkcAssignRepository;
import com.ruike.hme.domain.repository.HmeExceptionGroupRepository;
import com.ruike.hme.domain.vo.HmeExceptionGroupVO;
import com.ruike.hme.infra.mapper.HmeExceptionGroupMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 异常收集组基础数据表 资源库实现
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:24
 */
@Component
public class HmeExceptionGroupRepositoryImpl extends BaseRepositoryImpl<HmeExceptionGroup> implements HmeExceptionGroupRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeExcGroupAssignRepository hmeExcGroupAssignRepository;
    @Autowired
    private HmeExcGroupRouterRepository hmeExcGroupRouterRepository;
    @Autowired
    private HmeExcGroupWkcAssignRepository hmeExcGroupWkcAssignRepository;
    @Autowired
    private HmeExceptionGroupMapper mapper;
    @Autowired
    private MtUserClient userClient;

    @Override
    public List<HmeExceptionGroup> exceptionGroupUiQuery(Long tenantId, HmeExceptionGroupDTO dto) {
        List<HmeExceptionGroup> hmeExceptionGroupList = mapper.uiQuery(tenantId, dto);
        hmeExceptionGroupList.forEach(hmeExceptionGroup -> {
            // 获取当前异常组下，异常项分配异常收集组关系
            List<HmeExcGroupAssign> assignList = hmeExcGroupAssignRepository
                    .selectByExceptionGroupId(hmeExceptionGroup.getTenantId(), hmeExceptionGroup.getExceptionGroupId());
            hmeExceptionGroup.setHmeExcGroupAssignList(assignList);
            // 获取当前异常组下，异常收集组分配WKC关系
            List<HmeExcGroupWkcAssign> assignWkcList = hmeExcGroupWkcAssignRepository
                    .selectByExceptionGroupId(hmeExceptionGroup.getTenantId(), hmeExceptionGroup.getExceptionGroupId());
            hmeExceptionGroup.setHmeExcGroupWkcAssignList(assignWkcList);

            // 非数据字段赋值
            hmeExceptionGroup.setCreatedUserName(userClient.userInfoGet(tenantId, hmeExceptionGroup.getCreatedBy()).getRealName());
            hmeExceptionGroup.setLastUpdatedUserName(userClient.userInfoGet(tenantId, hmeExceptionGroup.getLastUpdatedBy()).getRealName());
        });

        return hmeExceptionGroupList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeExceptionGroup excGroupBasicPropertyUpdate(Long tenantId, HmeExceptionGroup dto) {
        String exceptionGroupId = dto.getExceptionGroupId();
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        // 校验编码重复
        HmeExceptionGroup oldExceptionGroup = new HmeExceptionGroup();
        oldExceptionGroup.setTenantId(tenantId);
        oldExceptionGroup.setExceptionGroupCode(dto.getExceptionGroupCode());
        oldExceptionGroup = mapper.selectOne(oldExceptionGroup);
        if (null != oldExceptionGroup) {
            if (StringUtils.isEmpty(exceptionGroupId) || !exceptionGroupId.equals(oldExceptionGroup.getExceptionGroupId())) {
                throw new MtException("HME_EXCEPTION_MAN_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEPTION_MAN_005", "HME"));
            }
        }

        if (StringUtils.isEmpty(exceptionGroupId)) {
            Date now = new Date();
            dto.setTenantId(tenantId);
            dto.setCreatedBy(userId);
            dto.setCreationDate(now);
            dto.setLastUpdateDate(now);
            dto.setLastUpdatedBy(userId);
            self().insertSelective(dto);
        } else {
            Date now = new Date();
            dto.setTenantId(tenantId);
            dto.setLastUpdateDate(now);
            dto.setLastUpdatedBy(userId);
            dto.setTenantId(tenantId);
            mapper.updateByPrimaryKeySelective(dto);
        }
        if (CollectionUtils.isNotEmpty(dto.getHmeExcGroupAssignList())) {
            List<HmeExcGroupAssign> hmeExcGroupAssignList =
                    hmeExcGroupAssignRepository.assignBatchUpdate(tenantId,
                            dto.getExceptionGroupId(), dto.getHmeExcGroupAssignList());
            dto.setHmeExcGroupAssignList(hmeExcGroupAssignList);
        }

        if (CollectionUtils.isNotEmpty(dto.getHmeExcGroupWkcAssignList())) {
            List<HmeExcGroupWkcAssign> hmeExcGroupWkcAssignList =
                    hmeExcGroupWkcAssignRepository.assignBatchUpdate(tenantId,
                            dto.getExceptionGroupId(), dto.getHmeExcGroupWkcAssignList());
            dto.setHmeExcGroupWkcAssignList(hmeExcGroupWkcAssignList);
        }

        return dto;
    }
}
