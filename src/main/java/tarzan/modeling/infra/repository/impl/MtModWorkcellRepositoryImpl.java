package tarzan.modeling.infra.repository.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModWorkcellVO1;
import tarzan.modeling.infra.mapper.MtModWorkcellMapper;

/**
 * 工作单元 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@Component
public class MtModWorkcellRepositoryImpl extends BaseRepositoryImpl<MtModWorkcell> implements MtModWorkcellRepository {

    private static final String TABLE_NAME = "mt_mod_workcell";
    private static final String ATTR_TABLE_NAME = "mt_mod_workcell_attr";
    private static final String Y_FLAG = "Y";

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModWorkcellMapper mtModWorkcellMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Override
    public MtModWorkcell workcellBasicPropertyGet(Long tenantId, String workcellId) {
        if (StringUtils.isEmpty(workcellId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "workcellId", "【API:workcellBasicPropertyGet】"));
        }
        MtModWorkcell cell = new MtModWorkcell();
        cell.setTenantId(tenantId);
        cell.setWorkcellId(workcellId);
        return this.mtModWorkcellMapper.selectOne(cell);
    }

    @Override
    public List<String> propertyLimitWorkcellQuery(Long tenantId, MtModWorkcellVO1 condition) {
        if (condition.getDescription() == null && condition.getEnableFlag() == null
                        && condition.getWorkcellCategory() == null && condition.getWorkcellCode() == null
                        && condition.getWorkcellLocation() == null && condition.getWorkcellName() == null
                        && condition.getWorkcellType() == null) {
            throw new MtException("MT_MODELING_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0017", "MODELING", "【API:propertyLimitWorkcellQuery】"));
        }

        MtModWorkcell mtModWorkcell = new MtModWorkcell();
        BeanUtils.copyProperties(condition, mtModWorkcell);
        mtModWorkcell.setTenantId(tenantId);

        List<MtModWorkcell> mtModWorkcells = this.mtModWorkcellMapper.selectForEmptyString(tenantId, mtModWorkcell);
        if (CollectionUtils.isEmpty(mtModWorkcells)) {
            return Collections.emptyList();
        }
        return mtModWorkcells.stream().map(MtModWorkcell::getWorkcellId).collect(Collectors.toList());
    }



    @Override
    public List<MtModWorkcell> workcellBasicPropertyBatchGet(Long tenantId, List<String> workcellIds) {
        if (CollectionUtils.isEmpty(workcellIds)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "workcellId", "【API:workcellBasicPropertyBatchGet】"));
        }
        return this.mtModWorkcellMapper.selectByIdsCustom(tenantId, workcellIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String workcellBasicPropertyUpdate(Long tenantId, MtModWorkcell dto, String fullUpdate) {
        List<String> enableFlags = Arrays.asList("Y", "N");
        String workcellId = dto.getWorkcellId();
        dto.setTenantId(tenantId);
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            // 新增逻辑
            if (StringUtils.isEmpty(dto.getWorkcellCode())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "workcellCode", "【API:workcellBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getWorkcellName())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "workcellName", "【API:workcellBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "enableFlag", "【API:workcellBasicPropertyUpdate】"));
            }
            if (!enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0035", "MODELING", "enableFlag", "【API:workcellBasicPropertyUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getWorkcellType())) {
                MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
                mtGenTypeVO2.setModule("MODELING");
                mtGenTypeVO2.setTypeGroup("WORKCELL_TYPE");
                List<MtGenType> wkcTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
                List<String> wkcTypeCodes =
                                wkcTypes.stream().map(MtGenType::getTypeCode).distinct().collect(Collectors.toList());
                if (!wkcTypeCodes.contains(dto.getWorkcellType())) {
                    throw new MtException("MT_MODELING_0002",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0002",
                                                    "MODELING", "workcellType", "【API:workcellBasicPropertyUpdate】"));
                }
            }

            MtModWorkcell mtModWorkcell = new MtModWorkcell();
            mtModWorkcell.setTenantId(tenantId);
            mtModWorkcell.setWorkcellCode(dto.getWorkcellCode());
            mtModWorkcell = this.mtModWorkcellMapper.selectOne(mtModWorkcell);
            if (null != mtModWorkcell) {
                throw new MtException("MT_MODELING_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0026", "MODELING", "workcellCode", "【API:workcellBasicPropertyUpdate】"));
            }
            dto.setTenantId(tenantId);
            self().insertSelective(dto);
            workcellId = dto.getWorkcellId();
        } else {
            // 修改逻辑
            if (null != dto.getWorkcellCode() && "".equals(dto.getWorkcellCode())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "workcellCode", "【API:workcellBasicPropertyUpdate】"));
            }
            if (null != dto.getWorkcellName() && "".equals(dto.getWorkcellName())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "workcellName", "【API:workcellBasicPropertyUpdate】"));
            }
            if (null != dto.getEnableFlag() && "".equals(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "enableFlag", "【API:workcellBasicPropertyUpdate】"));
            }
            if (null != dto.getEnableFlag() && !enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0035", "MODELING", "enableFlag", "【API:workcellBasicPropertyUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getWorkcellType())) {
                MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
                mtGenTypeVO2.setModule("MODELING");
                mtGenTypeVO2.setTypeGroup("WORKCELL_TYPE");
                List<MtGenType> wkcTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
                List<String> wkcTypeCodes =
                                wkcTypes.stream().map(MtGenType::getTypeCode).distinct().collect(Collectors.toList());
                if (!wkcTypeCodes.contains(dto.getWorkcellType())) {
                    throw new MtException("MT_MODELING_0002",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0002",
                                                    "MODELING", "workcellType", "【API:workcellBasicPropertyUpdate】"));
                }
            }

            MtModWorkcell mtModWorkcell = new MtModWorkcell();
            mtModWorkcell.setTenantId(tenantId);
            mtModWorkcell.setWorkcellId(dto.getWorkcellId());
            mtModWorkcell = this.mtModWorkcellMapper.selectOne(mtModWorkcell);
            if (null == mtModWorkcell) {
                throw new MtException("MT_MODELING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0003", "MODELING", "workcellId", "【API:workcellBasicPropertyUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getWorkcellCode())) {
                MtModWorkcell tmpMtModWorkcell = new MtModWorkcell();
                tmpMtModWorkcell.setTenantId(tenantId);
                tmpMtModWorkcell.setWorkcellCode(dto.getWorkcellCode());
                tmpMtModWorkcell = this.mtModWorkcellMapper.selectOne(tmpMtModWorkcell);
                if (null != tmpMtModWorkcell
                                && !tmpMtModWorkcell.getWorkcellId().equals(mtModWorkcell.getWorkcellId())) {
                    throw new MtException("MT_MODELING_0026",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0026",
                                                    "MODELING", "workcellCode", "【API:workcellBasicPropertyUpdate】"));
                }
            }
            dto.setTenantId(tenantId);

            if ("Y".equalsIgnoreCase(fullUpdate)) {
                self().updateByPrimaryKey(dto);
            } else {
                self().updateByPrimaryKeySelective(dto);
            }
        }

        return workcellId;
    }

    @Override
    public List<MtModWorkcell> workcellForWkcCodeQuery(Long tenantId, List<String> workcellCodes) {
        if (CollectionUtils.isEmpty(workcellCodes)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "workcellId", "【API:workcellForWkcCodeQuery】"));
        }
        return this.mtModWorkcellMapper.selectByWkcCode(tenantId, workcellCodes);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void modWorkcellAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "keyId", "【API:modWorkcellAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtModWorkcell entity = new MtModWorkcell();
        entity.setTenantId(tenantId);
        entity.setWorkcellId(dto.getKeyId());
        entity = this.mtModWorkcellMapper.selectOne(entity);
        if (entity == null) {
            throw new MtException("MT_MODELING_0048",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0048", "MODELING",
                            dto.getKeyId(), TABLE_NAME, "【API:modWorkcellAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE_NAME, dto.getKeyId(), dto.getEventId(),
                dto.getAttrs());
    }
}
