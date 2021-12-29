package tarzan.dispatch.infra.repository.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.dispatch.domain.entity.MtDispatchStrategyOrgRel;
import tarzan.dispatch.domain.repository.MtDispatchStrategyOrgRelRepository;
import tarzan.dispatch.domain.vo.*;
import tarzan.dispatch.infra.mapper.MtDispatchStrategyOrgRelMapper;

/**
 * 调度策略与组织关系表 资源库实现
 *
 * @author yiyang.xie 2020-02-03 19:42:38
 */
@Component
public class MtDispatchStrategyOrgRelRepositoryImpl extends BaseRepositoryImpl<MtDispatchStrategyOrgRel>
                implements MtDispatchStrategyOrgRelRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtDispatchStrategyOrgRelMapper dispatchStrategyOrgRelMapper;

    /**
     * dispatchStrategyOrganizationRelationUpdate-新增更新调度策略与组织关系
     *
     * @param tenantId
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String dispatchStrategyOrganizationRelationUpdate(Long tenantId, MtDispatchStrategyOrgRelVO1 vo) {

        // 返回结果id
        String resultId;
        MtDispatchStrategyOrgRel rel;
        MtGenTypeVO2 queryType;
        if (StringUtils.isEmpty(vo.getStrategyOrgRelId())) {
            // 新增情况
            // 校验输入数据
            if (StringUtils.isEmpty(vo.getOrganizationId())) {
                throw new MtException("MT_DISPATCH_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001",
                                                "DISPATCH", "organizationId",
                                                "【API：dispatchStrategyOrganizationRelationUpdate】"));
            }
            if (StringUtils.isEmpty(vo.getOrganizationType())) {
                throw new MtException("MT_DISPATCH_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001",
                                                "DISPATCH", "organizationType",
                                                "【API：dispatchStrategyOrganizationRelationUpdate】"));
            }
            if (StringUtils.isEmpty(vo.getRangeStrategy())) {
                throw new MtException("MT_DISPATCH_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001",
                                                "DISPATCH", "rangeStrategy",
                                                "【API：dispatchStrategyOrganizationRelationUpdate】"));
            }
            if (StringUtils.isEmpty(vo.getPublishStrategy())) {
                throw new MtException("MT_DISPATCH_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001",
                                                "DISPATCH", "publishStrategy",
                                                "【API：dispatchStrategyOrganizationRelationUpdate】"));
            }
            if (StringUtils.isEmpty(vo.getEnableFlag())) {
                throw new MtException("MT_DISPATCH_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001",
                                                "DISPATCH", "enableFlag",
                                                "【API：dispatchStrategyOrganizationRelationUpdate】"));
            }

            // 校验唯一性
            rel = new MtDispatchStrategyOrgRel();
            rel.setTenantId(tenantId);
            rel.setOrganizationId(vo.getOrganizationId());
            rel.setOrganizationType(vo.getOrganizationType());
            rel = dispatchStrategyOrgRelMapper.selectOne(rel);
            if (rel != null) {
                throw new MtException("MT_DISPATCH_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0026", "DISPATCH", "mt_dispatch_strategy_org_rel",
                                "organizationId、organizationType", "【API：dispatchStrategyOrganizationRelationUpdate】"));
            }

            // rangeStrategy检验
            queryType = new MtGenTypeVO2();
            queryType.setModule("DISPATCH");
            queryType.setTypeGroup("RANGE_STRATEGY");
            List<MtGenType> rangeTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);
            if (rangeTypes.stream().noneMatch(t -> t.getTypeCode().equals(vo.getRangeStrategy()))) {
                throw new MtException("MT_DISPATCH_0035",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0035",
                                                "DISPATCH", "rangeStrategy",
                                                rangeTypes.stream().map(MtGenType::getTypeCode)
                                                                .collect(Collectors.toList()).toString(),
                                                "【API：dispatchStrategyOrganizationRelationUpdate】"));
            }
            // publishStrategy检验
            queryType = new MtGenTypeVO2();
            queryType.setModule("DISPATCH");
            queryType.setTypeGroup("PUBLISH_STRATEGY");
            List<MtGenType> publishTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);
            if (publishTypes.stream().noneMatch(t -> t.getTypeCode().equals(vo.getPublishStrategy()))) {
                throw new MtException("MT_DISPATCH_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0035", "DISPATCH", "publishStrategy", publishTypes.stream()
                                                .map(MtGenType::getTypeCode).collect(Collectors.toList()).toString(),
                                "【API：dispatchStrategyOrganizationRelationUpdate】"));
            }
            // moveStrategy检验
            if (StringUtils.isNotEmpty(vo.getMoveStrategy())) {
                queryType = new MtGenTypeVO2();
                queryType.setModule("DISPATCH");
                queryType.setTypeGroup("MOVE_STRATEGY");
                List<MtGenType> moveTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);
                if (moveTypes.stream().noneMatch(t -> t.getTypeCode().equals(vo.getMoveStrategy()))) {
                    throw new MtException("MT_DISPATCH_0035",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0035",
                                                    "DISPATCH", "moveStrategy",
                                                    moveTypes.stream().map(MtGenType::getTypeCode)
                                                                    .collect(Collectors.toList()).toString(),
                                                    "【API：dispatchStrategyOrganizationRelationUpdate】"));
                }
            }

            // 校验通过新增
            rel = new MtDispatchStrategyOrgRel();
            rel.setTenantId(tenantId);
            rel.setOrganizationType(vo.getOrganizationType());
            rel.setOrganizationId(vo.getOrganizationId());
            rel.setRangeStrategy(vo.getRangeStrategy());
            rel.setPublishStrategy(vo.getPublishStrategy());
            rel.setMoveStrategy(vo.getMoveStrategy());
            rel.setEnableFlag(vo.getEnableFlag());
            self().insertSelective(rel);
            resultId = rel.getStrategyOrgRelId();
        } else {
            // 更新
            if ("".equals(vo.getOrganizationId())) {
                throw new MtException("MT_DISPATCH_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001",
                                                "DISPATCH", "organizationId",
                                                "【API：dispatchStrategyOrganizationRelationUpdate】"));
            }
            if ("".equals(vo.getOrganizationType())) {
                throw new MtException("MT_DISPATCH_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001",
                                                "DISPATCH", "organizationType",
                                                "【API：dispatchStrategyOrganizationRelationUpdate】"));
            }
            if ("".equals(vo.getRangeStrategy())) {
                throw new MtException("MT_DISPATCH_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001",
                                                "DISPATCH", "rangeStrategy",
                                                "【API：dispatchStrategyOrganizationRelationUpdate】"));
            }
            if ("".equals(vo.getPublishStrategy())) {
                throw new MtException("MT_DISPATCH_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001",
                                                "DISPATCH", "publishStrategy",
                                                "【API：dispatchStrategyOrganizationRelationUpdate】"));
            }
            if ("".equals(vo.getEnableFlag())) {
                throw new MtException("MT_DISPATCH_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001",
                                                "DISPATCH", "enableFlag",
                                                "【API：dispatchStrategyOrganizationRelationUpdate】"));
            }

            rel = new MtDispatchStrategyOrgRel();
            rel.setTenantId(tenantId);
            rel.setStrategyOrgRelId(vo.getStrategyOrgRelId());
            rel = dispatchStrategyOrgRelMapper.selectOne(rel);
            // 校验唯一性
            if (StringUtils.isNotEmpty(vo.getOrganizationId())) {
                rel.setOrganizationId(vo.getOrganizationId());
            }
            if (StringUtils.isNotEmpty(vo.getOrganizationType())) {
                rel.setOrganizationType(vo.getOrganizationType());
            }

            List<MtDispatchStrategyOrgRel> rels = dispatchStrategyOrgRelMapper.selectByCondition(Condition
                            .builder(MtDispatchStrategyOrgRel.class)
                            .andWhere(Sqls.custom().andEqualTo(MtDispatchStrategyOrgRel.FIELD_TENANT_ID, tenantId)
                                            .andNotEqualTo(MtDispatchStrategyOrgRel.FIELD_STRATEGY_ORG_REL_ID,
                                                            vo.getStrategyOrgRelId())
                                            .andEqualTo(MtDispatchStrategyOrgRel.FIELD_ORGANIZATION_ID,
                                                            rel.getOrganizationId())
                                            .andEqualTo(MtDispatchStrategyOrgRel.FIELD_ORGANIZATION_TYPE,
                                                            rel.getOrganizationType()))
                            .build());
            if (CollectionUtils.isNotEmpty(rels)) {
                throw new MtException("MT_DISPATCH_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0026", "DISPATCH", "mt_dispatch_strategy_org_rel",
                                "organizationId、organizationType", "【API：dispatchStrategyOrganizationRelationUpdate】"));
            }


            // rangeStrategy检验
            if (StringUtils.isNotEmpty(vo.getRangeStrategy())) {
                queryType = new MtGenTypeVO2();
                queryType.setModule("DISPATCH");
                queryType.setTypeGroup("RANGE_STRATEGY");
                List<MtGenType> rangeTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);
                if (rangeTypes.stream().noneMatch(t -> t.getTypeCode().equals(vo.getRangeStrategy()))) {
                    throw new MtException("MT_DISPATCH_0035",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0035",
                                                    "DISPATCH", "rangeStrategy",
                                                    rangeTypes.stream().map(MtGenType::getTypeCode)
                                                                    .collect(Collectors.toList()).toString(),
                                                    "【API：dispatchStrategyOrganizationRelationUpdate】"));
                }
            }

            // publishStrategy检验
            if (StringUtils.isNotEmpty(vo.getPublishStrategy())) {
                queryType = new MtGenTypeVO2();
                queryType.setModule("DISPATCH");
                queryType.setTypeGroup("PUBLISH_STRATEGY");
                List<MtGenType> publishTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);
                if (publishTypes.stream().noneMatch(t -> t.getTypeCode().equals(vo.getPublishStrategy()))) {
                    throw new MtException("MT_DISPATCH_0035",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0035",
                                                    "DISPATCH", "publishStrategy",
                                                    publishTypes.stream().map(MtGenType::getTypeCode)
                                                                    .collect(Collectors.toList()).toString(),
                                                    "【API：dispatchStrategyOrganizationRelationUpdate】"));
                }
            }

            // moveStrategy检验
            if (StringUtils.isNotEmpty(vo.getMoveStrategy())) {
                queryType = new MtGenTypeVO2();
                queryType.setModule("DISPATCH");
                queryType.setTypeGroup("MOVE_STRATEGY");
                List<MtGenType> moveTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);
                if (moveTypes.stream().noneMatch(t -> t.getTypeCode().equals(vo.getMoveStrategy()))) {
                    throw new MtException("MT_DISPATCH_0035",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0035",
                                                    "DISPATCH", "moveStrategy",
                                                    moveTypes.stream().map(MtGenType::getTypeCode)
                                                                    .collect(Collectors.toList()).toString(),
                                                    "【API：dispatchStrategyOrganizationRelationUpdate】"));
                }
            }
            // 校验通过更新
            // 设值
            rel.setRangeStrategy(vo.getRangeStrategy());
            rel.setPublishStrategy(vo.getPublishStrategy());
            rel.setMoveStrategy(vo.getMoveStrategy());
            rel.setEnableFlag(vo.getEnableFlag());
            self().updateByPrimaryKeySelective(rel);
            resultId = vo.getStrategyOrgRelId();
        }
        return resultId;
    }

    @Override
    public List<MtDispatchStrategyOrgRelVO3> organizationLimitDispatchStrategyQuery(Long tenantId,
                    MtDispatchStrategyOrgRelVO2 vo) {
        return dispatchStrategyOrgRelMapper.selectByIdAndType(tenantId, vo);
    }

    @Override
    public List<MtDispatchStrategyOrgRelVO5> propertyLimitDispatchStrategyOrganizationRelationQuery(Long tenantId,
                    MtDispatchStrategyOrgRelVO4 vo) {
        return dispatchStrategyOrgRelMapper.selectByMyCondition(tenantId, vo);
    }
}
