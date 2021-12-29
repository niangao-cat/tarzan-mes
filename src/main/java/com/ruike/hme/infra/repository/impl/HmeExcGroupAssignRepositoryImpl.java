package com.ruike.hme.infra.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeExcGroupAssign;
import com.ruike.hme.domain.entity.HmeExcGroupRouter;
import com.ruike.hme.domain.repository.HmeExcGroupAssignRepository;
import com.ruike.hme.domain.repository.HmeExcGroupRouterRepository;
import com.ruike.hme.domain.vo.HmeExceptionGroupVO;
import com.ruike.hme.infra.mapper.HmeExcGroupAssignMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 异常项分配异常收集组关系表 资源库实现
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:24
 */
@Component
public class HmeExcGroupAssignRepositoryImpl extends BaseRepositoryImpl<HmeExcGroupAssign> implements HmeExcGroupAssignRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeExcGroupRouterRepository hmeExcGroupRouterRepository;
    @Autowired
    private HmeExcGroupAssignMapper mapper;
    @Autowired
    private MtUserClient userClient;

    @Override
    public List<HmeExcGroupAssign> assignBatchUpdate(Long tenantId, String exceptionGroupId, List<HmeExcGroupAssign> dtoList) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        // 需要更新的异常项分配关系为空，则报错
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new MtException("HME_EXCEPTION_MAN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_MAN_002", "HME"));
        }
        // 校验1：同一异常组下的异常项具备唯一性
        Map<String, List<HmeExcGroupAssign>> collect = dtoList.stream().collect(Collectors.groupingBy(HmeExcGroupAssign::getExceptionId));
        for (String exceptionId : collect.keySet()) {
            List<HmeExcGroupAssign> groupedExceptionList = collect.get(exceptionId);
            if (groupedExceptionList.size() > 1) {
                throw new MtException("HME_EXCEPTION_MAN_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEPTION_MAN_003", "HME"));
            }
        }

        // 校验2：同一异常组下的序号具备唯一性
        Map<Long, List<HmeExcGroupAssign>> serialCollect = dtoList.stream().collect(Collectors.groupingBy(HmeExcGroupAssign::getSerialNumber));
        for (Long serialNum : serialCollect.keySet()) {
            List<HmeExcGroupAssign> groupedExceptionList = serialCollect.get(serialNum);
            if (groupedExceptionList.size() > 1) {
                throw new MtException("HME_EXCEPTION_MAN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEPTION_MAN_004", "HME"));
            }
        }

        for (HmeExcGroupAssign groupAssign : dtoList) {
            groupAssign.setTenantId(tenantId);
            groupAssign.setExceptionGroupId(exceptionGroupId);
            if (StringUtils.isBlank(groupAssign.getExceptionGroupAssignId())) {
                Date now = new Date();
                groupAssign.setCreatedBy(userId);
                groupAssign.setCreationDate(now);
                groupAssign.setLastUpdateDate(now);
                groupAssign.setLastUpdatedBy(userId);
                self().insertSelective(groupAssign);

                HmeExceptionGroupVO exceptionGroupVO = new HmeExceptionGroupVO();
                exceptionGroupVO.setExceptionGroupId(groupAssign.getExceptionGroupId());
                exceptionGroupVO.setExceptionGroupAssignId(groupAssign.getExceptionGroupAssignId());
                exceptionGroupVO.setExceptionId(groupAssign.getExceptionId());
                List<HmeExcGroupRouter> routerList = hmeExcGroupRouterRepository.initExcGroupRouter(tenantId, exceptionGroupVO);

                groupAssign.setHmeExcGroupRouterList(routerList);
            } else {
                Date now = new Date();
                groupAssign.setLastUpdateDate(now);
                groupAssign.setLastUpdatedBy(userId);
                mapper.updateByPrimaryKeySelective(groupAssign);
                if (CollectionUtils.isNotEmpty(groupAssign.getHmeExcGroupRouterList())) {
                    List<HmeExcGroupRouter> routerList =
                            hmeExcGroupRouterRepository.excGroupRouterBatchUpdate(tenantId, groupAssign.getHmeExcGroupRouterList());
                    groupAssign.setHmeExcGroupRouterList(routerList);
                }
            }
            groupAssign.setCreatedUserName(userClient.userInfoGet(tenantId, groupAssign.getCreatedBy()).getRealName());
            groupAssign.setLastUpdatedUserName(userClient.userInfoGet(tenantId, groupAssign.getLastUpdatedBy()).getRealName());
        }
        return dtoList;
    }

    @Override
    public List<HmeExcGroupAssign> selectByExceptionGroupId(Long tenantId, String exceptionGroupId) {
        List<HmeExcGroupAssign> hmeExcGroupAssignList = mapper.selectByExceptionGroupId(tenantId, exceptionGroupId);
        // 非数据字段赋值
        hmeExcGroupAssignList.forEach(hmeException -> {
            hmeException.setCreatedUserName(userClient.userInfoGet(tenantId, hmeException.getCreatedBy()).getRealName());
            hmeException.setLastUpdatedUserName(userClient.userInfoGet(tenantId, hmeException.getLastUpdatedBy()).getRealName());
        });
        return hmeExcGroupAssignList;
    }
}
