package tarzan.modeling.infra.repository.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModAreaDistribution;
import tarzan.modeling.domain.entity.MtModLocatorOrgRel;
import tarzan.modeling.domain.repository.MtModAreaDistributionRepository;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.vo.MtModAreaDistributionVO;
import tarzan.modeling.domain.vo.MtModAreaDistributionVO2;
import tarzan.modeling.domain.vo.MtModAreaDistributionVO3;
import tarzan.modeling.infra.mapper.MtModAreaDistributionMapper;
import tarzan.modeling.infra.mapper.MtModLocatorOrgRelMapper;
import tarzan.pull.domain.entity.MtModDisLocatorDeteal;
import tarzan.pull.infra.mapper.MtModDisLocatorDetealMapper;

/**
 * 区域配送属性 资源库实现
 *
 * @author yiyang.xie 2020-02-04 11:36:01
 */
@Component
public class MtModAreaDistributionRepositoryImpl extends BaseRepositoryImpl<MtModAreaDistribution>
                implements MtModAreaDistributionRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtModLocatorOrgRelMapper mtModLocatorOrgRelMapper;

    @Autowired
    private MtModAreaDistributionMapper mapper;

    @Autowired
    private MtModDisLocatorDetealMapper mtModDisLocatorDetealMapper;

    @Override
    public MtModAreaDistributionVO2 areaDistributionPropertyGet(Long tenantId, MtModAreaDistributionVO dto) {
        // 第一步，判断参数合规性
        if (StringUtils.isEmpty(dto.getAreaDistributionId()) && StringUtils.isEmpty(dto.getAreaId())) {
            throw new MtException("MT_MODELING_0059",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0059", "MODELING",
                                            "areaDistributionId ", "areaId", "【API:areaDistributionPropertyGet】"));
        }
        // 若areaId有传入则在表MT_MOD_AREA表中限制AREA_ID = areaId,获取数据
        if (StringUtils.isNotEmpty(dto.getAreaId())) {
            MtModArea mtModArea = mtModAreaRepository.areaBasicPropertyGet(tenantId, dto.getAreaId());
            if (mtModArea == null) {
                throw new MtException("MT_MODELING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0003", "MODELING", "areaId", "【API:areaDistributionPropertyGet】"));
            }
        }
        // 第二步，根据输入参数组合获取输出参数
        // 限制传入的areaDistributionId或areaId等于表MT_MOD_AREA_DISTRIBUTION中的
        MtModAreaDistributionVO2 result = new MtModAreaDistributionVO2();
        MtModAreaDistribution mtModAreaDistribution = new MtModAreaDistribution();
        mtModAreaDistribution.setTenantId(tenantId);
        mtModAreaDistribution.setAreaDistributionId(dto.getAreaDistributionId());
        mtModAreaDistribution.setAreaId(dto.getAreaId());
        mtModAreaDistribution = mapper.selectOne(mtModAreaDistribution);
        if (mtModAreaDistribution == null) {
            return result;
        }

        result.setAreaDistributionId(mtModAreaDistribution.getAreaDistributionId());
        result.setAreaId(mtModAreaDistribution.getAreaId());
        result.setDistributionMode(mtModAreaDistribution.getDistributionMode());
        result.setPullTimeIntervalFlag(mtModAreaDistribution.getPullTimeIntervalFlag());
        result.setDistributionCycle(mtModAreaDistribution.getDistributionCycle());
        result.setBusinessType(mtModAreaDistribution.getBusinessType());
        result.setInstructCreatedByEo(mtModAreaDistribution.getInstructCreatedByEo());

        MtModLocatorOrgRel mtModLocatorOrgRel = new MtModLocatorOrgRel();
        mtModLocatorOrgRel.setTenantId(tenantId);
        mtModLocatorOrgRel.setOrganizationType("AREA");
        mtModLocatorOrgRel.setOrganizationId(mtModAreaDistribution.getAreaId());
        List<MtModLocatorOrgRel> orgRels = mtModLocatorOrgRelMapper.select(mtModLocatorOrgRel);
        if (CollectionUtils.isNotEmpty(orgRels)) {
            Long minSequence = orgRels.stream().map(MtModLocatorOrgRel::getSequence).filter(t -> t != null)
                            .min(Comparator.naturalOrder()).get();
            List<MtModLocatorOrgRel> minList = orgRels.stream().filter(o -> o.getSequence().equals(minSequence))
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(minList) && minList.size() > 1) {
                throw new MtException("MT_MODELING_0060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0060", "MODELING", "【API:areaDistributionPropertyGet】"));
            }
            if (CollectionUtils.isNotEmpty(minList)) {
                result.setSourceLocatorId(minList.get(0).getLocatorId());
            }
        }
        mtModLocatorOrgRel = new MtModLocatorOrgRel();
        mtModLocatorOrgRel.setTenantId(tenantId);
        mtModLocatorOrgRel.setOrganizationType("AREA");
        mtModLocatorOrgRel.setOrganizationId(mtModAreaDistribution.getAreaId());
        mtModLocatorOrgRel.setLocatorId(dto.getLocatorId());
        mtModLocatorOrgRel = mtModLocatorOrgRelMapper.selectOne(mtModLocatorOrgRel);
        if (mtModLocatorOrgRel != null) {
            result.setSequence(mtModLocatorOrgRel.getSequence());

            MtModDisLocatorDeteal locatorDeteal = new MtModDisLocatorDeteal();
            locatorDeteal.setTenantId(tenantId);
            locatorDeteal.setLocatorOrganizationRelId(mtModLocatorOrgRel.getLocatorOrganizationRelId());
            locatorDeteal = mtModDisLocatorDetealMapper.selectOne(locatorDeteal);
            if (locatorDeteal != null) {
                result.setPullToArrive(locatorDeteal.getPullT0Arrive());
            }
        }

        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String areaDistributionPropertyUpdate(Long tenantId, MtModAreaDistributionVO3 dto, String fullUpdate) {

        // 第一步，判断参数合规性
        if (StringUtils.isEmpty(dto.getAreaId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "areaId", "【API:areaDistributionPropertyUpdate】"));
        }
        
        MtModArea mtModArea = mtModAreaRepository.areaBasicPropertyGet(tenantId, dto.getAreaId());
        if (mtModArea == null) {
            throw new MtException("MT_MODELING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0003", "MODELING", "areaId", "【API:areaDistributionPropertyUpdate】"));
        }

        // 第二步，根据传入参数判断新增或更新模式
        MtModAreaDistribution mtModAreaDistribution;
        String resultId;
        if (StringUtils.isNotEmpty(dto.getAreaDistributionId())) {
            mtModAreaDistribution = new MtModAreaDistribution();
            mtModAreaDistribution.setTenantId(tenantId);
            mtModAreaDistribution.setAreaDistributionId(dto.getAreaDistributionId());
            mtModAreaDistribution = mapper.selectOne(mtModAreaDistribution);
            if (mtModAreaDistribution == null) {
                throw new MtException("MT_MODELING_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0003",
                                                "MODELING", "areaDistributionId",
                                                "【API:areaDistributionPropertyUpdate】"));
            }

        } else {
            mtModAreaDistribution = new MtModAreaDistribution();
            mtModAreaDistribution.setTenantId(tenantId);
            mtModAreaDistribution.setAreaId(dto.getAreaId());
            mtModAreaDistribution = mapper.selectOne(mtModAreaDistribution);
        }

        if (mtModAreaDistribution == null) {
            // 新增
            mtModAreaDistribution = new MtModAreaDistribution();
            mtModAreaDistribution.setTenantId(tenantId);
            mtModAreaDistribution.setAreaId(dto.getAreaId());
            mtModAreaDistribution.setDistributionMode(dto.getDistributionMode());
            mtModAreaDistribution.setPullTimeIntervalFlag(dto.getPullTimeIntervalFlag());
            mtModAreaDistribution.setDistributionCycle(dto.getDistributionCycle());
            mtModAreaDistribution.setBusinessType(dto.getBusinessType());
            mtModAreaDistribution.setInstructCreatedByEo(dto.getInstructCreatedByEo());
            self().insertSelective(mtModAreaDistribution);
            resultId = mtModAreaDistribution.getAreaDistributionId();
        } else {
            // 更新
            mtModAreaDistribution.setAreaId(dto.getAreaId());
            mtModAreaDistribution.setDistributionMode(dto.getDistributionMode());
            mtModAreaDistribution.setPullTimeIntervalFlag(dto.getPullTimeIntervalFlag());
            mtModAreaDistribution.setDistributionCycle(dto.getDistributionCycle());
            mtModAreaDistribution.setBusinessType(dto.getBusinessType());
            mtModAreaDistribution.setInstructCreatedByEo(dto.getInstructCreatedByEo());
            if (MtBaseConstants.YES.equals(fullUpdate)) {
                mtModAreaDistribution =
                                (MtModAreaDistribution) ObjectFieldsHelper.setStringFieldsEmpty(mtModAreaDistribution);
                self().updateByPrimaryKey(mtModAreaDistribution);
            } else {
                self().updateByPrimaryKeySelective(mtModAreaDistribution);
            }
            resultId = mtModAreaDistribution.getAreaDistributionId();
        }
        return resultId;
    }
}
