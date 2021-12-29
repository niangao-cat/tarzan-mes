package com.ruike.hme.infra.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeExcGroupWkcAssign;
import com.ruike.hme.domain.repository.HmeExcGroupWkcAssignRepository;
import com.ruike.hme.infra.mapper.HmeExcGroupWkcAssignMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 异常收集组分配WKC关系表 资源库实现
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:23
 */
@Component
public class HmeExcGroupWkcAssignRepositoryImpl extends BaseRepositoryImpl<HmeExcGroupWkcAssign> implements HmeExcGroupWkcAssignRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeExcGroupWkcAssignMapper mapper;
    @Autowired
    private MtUserClient userClient;

    @Override
    public List<HmeExcGroupWkcAssign> assignBatchUpdate(Long tenantId, String exceptionGroupId, List<HmeExcGroupWkcAssign> dtoList) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        // 需要更新的异常项分配关系为空，则报错
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new MtException("HME_EXCEPTION_MAN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_MAN_002", "HME"));
        }

        // 校验1：同一异常组下的工序工位具备唯一性
        Map<String, List<HmeExcGroupWkcAssign>> collect = dtoList.stream().collect(Collectors.groupingBy(HmeExcGroupWkcAssign::getWorkcellId));
        for (String exceptionId : collect.keySet()) {
            List<HmeExcGroupWkcAssign> groupedExceptionList = collect.get(exceptionId);
            if (groupedExceptionList.size() > 1) {
                throw new MtException("HME_EXCEPTION_MAN_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEPTION_MAN_010", "HME"));
            }
        }

        //查询异常组下最大的序号
        Long serialNumber = mapper.queryMaxSerialNumber(tenantId, exceptionGroupId);
        //2020/12/09 add by sanfeng.zhang 前端只传改动的 新增的序号递增 无需做序列号唯一（编辑不改变序号）
        for (HmeExcGroupWkcAssign groupAssign : dtoList) {
            groupAssign.setTenantId(tenantId);
            groupAssign.setExceptionGroupId(exceptionGroupId);
            if (StringUtils.isBlank(groupAssign.getExcGroupWkcAssignId())) {
                Date now = new Date();

                groupAssign.setCreatedBy(userId);
                groupAssign.setCreationDate(now);
                groupAssign.setLastUpdateDate(now);
                groupAssign.setLastUpdatedBy(userId);
                groupAssign.setSerialNumber(serialNumber++);
                self().insertSelective(groupAssign);
            } else {
                Date now = new Date();
                groupAssign.setLastUpdateDate(now);
                groupAssign.setLastUpdatedBy(userId);
                mapper.updateByPrimaryKeySelective(groupAssign);
            }
            groupAssign.setCreatedUserName(userClient.userInfoGet(tenantId, groupAssign.getCreatedBy()).getRealName());
            groupAssign.setLastUpdatedUserName(userClient.userInfoGet(tenantId, groupAssign.getLastUpdatedBy()).getRealName());
        }
        return dtoList;
    }

    @Override
    public List<HmeExcGroupWkcAssign> selectByExceptionGroupId(Long tenantId, String exceptionGroupId) {
        List<HmeExcGroupWkcAssign> hmeExcGroupWkcAssignList = mapper.selectByExceptionGroupId(tenantId, exceptionGroupId);
        // 非数据字段赋值
        hmeExcGroupWkcAssignList.forEach(hmeException -> {
            hmeException.setCreatedUserName(userClient.userInfoGet(tenantId, hmeException.getCreatedBy()).getRealName());
            hmeException.setLastUpdatedUserName(userClient.userInfoGet(tenantId, hmeException.getLastUpdatedBy()).getRealName());
        });
        return hmeExcGroupWkcAssignList;
    }

}
