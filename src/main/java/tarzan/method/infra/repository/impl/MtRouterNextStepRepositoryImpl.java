package tarzan.method.infra.repository.impl;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtRouterNextStep;
import tarzan.method.domain.repository.MtRouterNextStepRepository;
import tarzan.method.domain.vo.MtRouterNextStepVO;
import tarzan.method.domain.vo.MtRouterNextStepVO3;
import tarzan.method.infra.mapper.MtRouterNextStepMapper;

/**
 * 下一步骤 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Component
public class MtRouterNextStepRepositoryImpl extends BaseRepositoryImpl<MtRouterNextStep>
                implements MtRouterNextStepRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtRouterNextStepMapper mtRouterNextStepMapper;

    @Autowired
    private ProfileClient profileClient;

    @Override
    public List<MtRouterNextStep> routerNextStepQuery(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerNextStepQuery】"));
        }

        List<MtRouterNextStep> resultList = new ArrayList<>();

        // 1. 获取是否启用关键步骤标识
        String keyStepToGetNextStep = profileClient.getProfileValueByOptions(tenantId,
                DetailsHelper.getUserDetails().getUserId(),
                DetailsHelper.getUserDetails().getRoleId(), "KEY_STEP_TO_GET_NEXT_STEP");

        if (!"Y".equalsIgnoreCase(keyStepToGetNextStep)) {
            MtRouterNextStep mtRouterNextStep = new MtRouterNextStep();
            mtRouterNextStep.setTenantId(tenantId);
            mtRouterNextStep.setRouterStepId(routerStepId);
            resultList = mtRouterNextStepMapper.select(mtRouterNextStep);
            return resultList;
        } else {
            List<String> routerStepIds = Collections.singletonList(routerStepId);
            boolean flag = true;
            while (flag) {
                // 获取该步骤的所有下一步骤
                List<MtRouterNextStepVO> nextStepList =
                                mtRouterNextStepMapper.selectKeyStepFlagByStepIds(tenantId, routerStepIds);

                // 筛选KeyStep为Y的数据
                List<MtRouterNextStepVO> keyStepList = nextStepList.stream()
                                .filter(t -> "Y".equalsIgnoreCase(t.getKeyStepFlag())).collect(toList());
                resultList.addAll(keyStepList);

                // 筛选KeyStep不为Y的数据
                List<MtRouterNextStepVO> unKeyStepList = nextStepList.stream()
                                .filter(t -> !"Y".equalsIgnoreCase(t.getKeyStepFlag())).collect(toList());
                if (CollectionUtils.isEmpty(unKeyStepList)) {
                    flag = false;
                } else {
                    routerStepIds = unKeyStepList.stream().map(MtRouterNextStepVO::getNextStepId).collect(toList());
                }
            }
            return resultList;
        }
    }

    @Override
    public List<MtRouterNextStep> routerPreviousStepQuery(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerPreviousStepQuery】"));
        }

        MtRouterNextStep mtRouterNextStep = new MtRouterNextStep();
        mtRouterNextStep.setTenantId(tenantId);
        mtRouterNextStep.setNextStepId(routerStepId);
        return this.mtRouterNextStepMapper.select(mtRouterNextStep);
    }

    @Override
    public List<MtRouterNextStep> decisionTypeLimitRouterNextStepQuery(Long tenantId, MtRouterNextStepVO3 dto) {
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:decisionTypeLimitRouterNextStepQuery】"));
        }
        if (StringUtils.isEmpty(dto.getNextDecisionType())) {
            throw new MtException("MT_ROUTER_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001", "ROUTER",
                                            "nextDecisionType", "【API:decisionTypeLimitRouterNextStepQuery】"));
        }

        MtRouterNextStep mtRouterNextStep = new MtRouterNextStep();
        mtRouterNextStep.setRouterStepId(dto.getRouterStepId());
        mtRouterNextStep.setNextDecisionType(dto.getNextDecisionType());
        mtRouterNextStep.setNextDecisionValue(dto.getNextDecisionValue());
        return mtRouterNextStepMapper.select(mtRouterNextStep);
    }
}
