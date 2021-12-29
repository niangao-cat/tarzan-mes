package tarzan.modeling.infra.repository.impl;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.vo.MtModAreaVO1;
import tarzan.modeling.infra.mapper.MtModAreaMapper;

/**
 * 区域 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Component
public class MtModAreaRepositoryImpl extends BaseRepositoryImpl<MtModArea> implements MtModAreaRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModAreaMapper mtModAreaMapper;

    @Override
    public MtModArea areaBasicPropertyGet(Long tenantId, String areaId) {
        if (StringUtils.isEmpty(areaId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "areaId", "【API:areaBasicPropertyGet】"));
        }

        MtModArea area = new MtModArea();
        area.setTenantId(tenantId);
        area.setAreaId(areaId);

        return this.mtModAreaMapper.selectOne(area);
    }

    @Override
    public List<String> propertyLimitAreaQuery(Long tenantId, MtModAreaVO1 condition) {
        if (condition.getAddress() == null && condition.getAreaCategory() == null && condition.getAreaCode() == null
                        && condition.getAreaName() == null && condition.getCity() == null
                        && condition.getCountry() == null && condition.getCounty() == null
                        && condition.getDescription() == null && condition.getEnableFlag() == null
                        && condition.getProvince() == null) {
            throw new MtException("MT_MODELING_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0017", "MODELING", "【API:propertyLimitAreaQuery】"));
        }

        MtModArea mtModArea = new MtModArea();
        BeanUtils.copyProperties(condition, mtModArea);
        mtModArea.setTenantId(tenantId);
        List<MtModArea> mtModAreas = this.mtModAreaMapper.select(mtModArea);
        if (CollectionUtils.isEmpty(mtModAreas)) {
            return Collections.emptyList();
        }

        return mtModAreas.stream().map(MtModArea::getAreaId).collect(toList());
    }


    @Override
    public List<MtModArea> areaBasicPropertyBatchGet(Long tenantId, List<String> areaIds) {
        if (CollectionUtils.isEmpty(areaIds)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "areaId", "【API:areaBasicPropertyBatchGet】"));
        }
        return this.mtModAreaMapper.selectByIdsCustom(tenantId, areaIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String areaBasicPropertyUpdate(Long tenantId, MtModArea dto, String fullUpdate) {
        List<String> enableFlags = Arrays.asList("Y", "N");
        String areaId = dto.getAreaId();

        if (StringUtils.isEmpty(dto.getAreaId())) {
            // 新增逻辑
            if (StringUtils.isEmpty(dto.getAreaCode())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "areaCode", "【API:areaBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getAreaName())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "areaName", "【API:areaBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "enableFlag", "【API:areaBasicPropertyUpdate】"));
            }
            if (!enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0035", "MODELING", "enableFlag", "【API:areaBasicPropertyUpdate】"));
            }

            MtModArea mtModArea = new MtModArea();
            mtModArea.setTenantId(tenantId);
            mtModArea.setAreaCode(dto.getAreaCode());
            mtModArea = this.mtModAreaMapper.selectOne(mtModArea);
            if (null != mtModArea) {
                throw new MtException("MT_MODELING_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0026", "MODELING", "areaCode", "【API:areaBasicPropertyUpdate】"));
            }

            dto.setTenantId(tenantId);
            self().insertSelective(dto);
            areaId = dto.getAreaId();
        } else {
            // 修改逻辑
            if (null != dto.getAreaCode() && "".equals(dto.getAreaCode())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "areaCode", "【API:areaBasicPropertyUpdate】"));
            }
            if (null != dto.getAreaName() && "".equals(dto.getAreaName())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "areaName", "【API:areaBasicPropertyUpdate】"));
            }
            if (null != dto.getEnableFlag() && "".equals(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "enableFlag", "【API:areaBasicPropertyUpdate】"));
            }
            if (null != dto.getEnableFlag() && !enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0035", "MODELING", "enableFlag", "【API:areaBasicPropertyUpdate】"));
            }

            MtModArea mtModArea = new MtModArea();
            mtModArea.setTenantId(tenantId);
            mtModArea.setAreaId(dto.getAreaId());
            mtModArea = this.mtModAreaMapper.selectOne(mtModArea);
            if (null == mtModArea) {
                throw new MtException("MT_MODELING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0003", "MODELING", "areaId", "【API:areaBasicPropertyUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getAreaCode())) {
                MtModArea tmpMtModArea = new MtModArea();
                tmpMtModArea.setTenantId(tenantId);
                tmpMtModArea.setAreaCode(dto.getAreaCode());
                tmpMtModArea = this.mtModAreaMapper.selectOne(tmpMtModArea);
                if (null != tmpMtModArea && !tmpMtModArea.getAreaId().equals(mtModArea.getAreaId())) {
                    throw new MtException("MT_MODELING_0026",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0026",
                                                    "MODELING", "areaCode", "【API:areaBasicPropertyUpdate】"));
                }
            }

            if ("Y".equalsIgnoreCase(fullUpdate)) {
                dto.setTenantId(tenantId);
                self().updateByPrimaryKey(dto);
            } else {
                dto.setTenantId(tenantId);
                self().updateByPrimaryKeySelective(dto);
            }
        }

        return areaId;
    }
}
