package com.ruike.hme.infra.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ruike.hme.infra.constant.HmeConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;

import com.ruike.hme.api.dto.HmeExceptionRouterDTO;
import com.ruike.hme.domain.entity.HmeExcGroupRouter;
import com.ruike.hme.domain.entity.HmeExceptionRouter;
import com.ruike.hme.domain.repository.HmeExceptionRouterRepository;
import com.ruike.hme.infra.mapper.HmeExceptionRouterMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;

import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 异常反馈路线基础数据表 资源库实现
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:25
 */
@Component
public class HmeExceptionRouterRepositoryImpl extends BaseRepositoryImpl<HmeExceptionRouter> implements HmeExceptionRouterRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeExceptionRouterMapper mapper;
    @Autowired
    private MtUserClient userClient;

    @Override
    public List<HmeExceptionRouter> queryRouterByExceptionId(Long tenantId, String exceptionId) {
        List<HmeExceptionRouter> exceptionRouterList = mapper.selectRouterByExceptionId(tenantId, exceptionId);
        // 非数据字段赋值
        exceptionRouterList.forEach(hmeException -> {
            hmeException.setCreatedUserName(userClient.userInfoGet(tenantId, hmeException.getCreatedBy()).getRealName());
            hmeException.setLastUpdatedUserName(userClient.userInfoGet(tenantId, hmeException.getLastUpdatedBy()).getRealName());
        });
        return exceptionRouterList;
    }

    @Override
    public List<HmeExceptionRouter> routerBatchUpdate(Long tenantId, List<HmeExceptionRouter> dtoList) {

        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        // 需要更新的异常反馈路线为空，则报错
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new MtException("HME_EXCEPTION_MAN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_MAN_002", "HME"));
        }
        // 校验1：同一异常类型下的反馈岗位具备唯一性
        Map<Long, List<HmeExceptionRouter>> collect = dtoList.stream().collect(Collectors.groupingBy(HmeExceptionRouter::getRespondPositionId));
        for (Long positionId : collect.keySet()) {
            if (positionId != null) {
                List<HmeExceptionRouter> groupedRouterList = collect.get(positionId);
                if (groupedRouterList.size() > 1) {
                    throw new MtException("HME_EXCEPTION_MAN_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEPTION_MAN_006", "HME"));
                }
            }
        }

        // 校验2：同一异常类型的同一异常等级的升级时长必须一致
        Map<Long, List<HmeExceptionRouter>> levelCollect = dtoList.stream().collect(Collectors.groupingBy(HmeExceptionRouter::getExceptionLevel));
        for (Long exceptionLevel : levelCollect.keySet()) {
            if (exceptionLevel != null) {
                List<HmeExceptionRouter> groupedRouterList = levelCollect.get(exceptionLevel);
                if (groupedRouterList.size() > 1) {
                    // 比较因子初始化为-1L
                    Long upgradeTime = -1L;
                    for (HmeExceptionRouter upgradeTimeRouter : groupedRouterList) {
                        if (upgradeTime == -1L && !Objects.isNull(upgradeTimeRouter.getUpgradeTime())) {
                            upgradeTime = upgradeTimeRouter.getUpgradeTime();
                        } else {
                            // 存在相同等级的异常反馈路线，升级时间不一致则报错（包括非空判断）
                            if (Objects.isNull(upgradeTimeRouter.getUpgradeTime()) && upgradeTime > 0L) {
                                throw new MtException("HME_EXCEPTION_MAN_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EXCEPTION_MAN_007", "HME"));
                            } else {
                                // 存在为空的异常等级，比较因子设为0L
                                upgradeTime = 0L;
                            }
                        }
                    }

                }
            }
        }

        for (HmeExceptionRouter router : dtoList) {
            // 校验3：升级时长和是否最高等级其一必输，且只能输入其中之一
            if (HmeConstants.ConstantValue.YES.equals(router.getIsTop()) && router.getUpgradeTime() != null) {
                throw new MtException("HME_EXCEPTION_MAN_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEPTION_MAN_008", "HME"));
            }

            // 校验3：升级时长和是否最高等级其一必输，且只能输入其中之一
            if (HmeConstants.ConstantValue.NO.equals(router.getIsTop()) && router.getUpgradeTime() == null) {
                throw new MtException("HME_EXCEPTION_MAN_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEPTION_MAN_008", "HME"));
            }
            // 升级时长给值
            router.setUpgradeTime(router.getUpgradeTime());

            if (StringUtils.isBlank(router.getExceptionRouterId())) {
                Date now = new Date();
                router.setTenantId(tenantId);
                router.setCreatedBy(userId);
                router.setCreationDate(now);
                router.setLastUpdateDate(now);
                router.setLastUpdatedBy(userId);
                self().insertSelective(router);
            } else {
                Date now = new Date();
                router.setTenantId(tenantId);
                router.setLastUpdateDate(now);
                router.setLastUpdatedBy(userId);
                router.setTenantId(tenantId);

                mapper.updateByPrimaryKeySelective(router);
            }
            router.setCreatedUserName(userClient.userInfoGet(tenantId, router.getCreatedBy()).getRealName());
            router.setLastUpdatedUserName(userClient.userInfoGet(tenantId, router.getLastUpdatedBy()).getRealName());
        }

        return dtoList;
    }

    @Override
    public void deleteById(Long tenantId, HmeExceptionRouterDTO dto) {

        int recordCount = mapper.selectCountByCondition(Condition
                .builder(HmeExceptionRouter.class)
                .andWhere(Sqls.custom().andEqualTo(HmeExceptionRouter.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeExceptionRouter.FIELD_EXCEPTION_ID, dto.getExceptionId())
                        .andEqualTo(HmeExceptionRouter.FIELD_IS_TOP, HmeConstants.ConstantValue.YES)
                .andNotEqualTo(HmeExceptionRouter.FIELD_EXCEPTION_ROUTER_ID, dto.getExceptionRouterId()))
                .build());
        // 校验：删除后同一异常类型必须存在最高等级
        if (recordCount <= 0) {
            throw new MtException("HME_EXCEPTION_MAN_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_MAN_009", "HME"));
        }
        mapper.deleteByPrimaryKey(dto.getExceptionRouterId());
    }
}
