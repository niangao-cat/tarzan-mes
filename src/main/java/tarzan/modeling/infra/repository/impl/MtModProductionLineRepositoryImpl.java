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
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.vo.MtModProductionLineVO1;
import tarzan.modeling.infra.mapper.MtModProductionLineMapper;

/**
 * 生产线 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Component
public class MtModProductionLineRepositoryImpl extends BaseRepositoryImpl<MtModProductionLine>
                implements MtModProductionLineRepository {


    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModProductionLineMapper mtModProductionLineMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Override
    public MtModProductionLine prodLineBasicPropertyGet(Long tenantId, String prodLineId) {
        if (StringUtils.isEmpty(prodLineId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "prodLineId", "【API:prodLineBasicPropertyGet】"));
        }
        MtModProductionLine line = new MtModProductionLine();
        line.setTenantId(tenantId);
        line.setProdLineId(prodLineId);
        return this.mtModProductionLineMapper.selectOne(line);
    }

    @Override
    public List<String> propertyLimitProdLineQuery(Long tenantId, MtModProductionLineVO1 condition) {
        if (condition.getDescription() == null && condition.getEnableFlag() == null
                        && condition.getProdLineCategory() == null && condition.getProdLineCode() == null
                        && condition.getProdLineName() == null && condition.getProdLineType() == null
                        && condition.getSupplierId() == null && condition.getSupplierSiteId() == null) {
            throw new MtException("MT_MODELING_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0017", "MODELING", "【API:propertyLimitProdLineQuery】"));
        }

        MtModProductionLine mtModProductionLine = new MtModProductionLine();
        BeanUtils.copyProperties(condition, mtModProductionLine);
        mtModProductionLine.setTenantId(tenantId);
        List<MtModProductionLine> mtModProductionLines =
                        this.mtModProductionLineMapper.selectForEmptyString(tenantId, mtModProductionLine);
        if (CollectionUtils.isEmpty(mtModProductionLines)) {
            return Collections.emptyList();
        }

        return mtModProductionLines.stream().map(MtModProductionLine::getProdLineId).collect(Collectors.toList());
    }


    @Override
    public List<MtModProductionLine> prodLineBasicPropertyBatchGet(Long tenantId, List<String> prodLineIds) {
        if (CollectionUtils.isEmpty(prodLineIds)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "prodLineId", "【API:prodLineBasicPropertyBatchGet】"));
        }
        return this.mtModProductionLineMapper.selectByIdsCustom(tenantId, prodLineIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String prodLineBasicPropertyUpdate(Long tenantId, MtModProductionLine dto, String fullUpdate) {
        List<String> enableFlags = Arrays.asList("Y", "N");
        String prodLineId = dto.getProdLineId();
        if (StringUtils.isEmpty(dto.getProdLineId())) {
            // 新增逻辑
            if (StringUtils.isEmpty(dto.getProdLineCode())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "prodLineCode", "【API:prodLineBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getProdLineName())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "prodLineName", "【API:prodLineBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getProdLineType())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "prodLineType", "【API:prodLineBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "enableFlag", "【API:prodLineBasicPropertyUpdate】"));
            }
            if (!enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0035", "MODELING", "enableFlag", "【API:prodLineBasicPropertyUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getProdLineType())) {
                MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
                mtGenTypeVO2.setModule("MODELING");
                mtGenTypeVO2.setTypeGroup("PROD_LINE_TYPE");
                List<MtGenType> proLineTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
                List<String> proLineTypeCodes = proLineTypes.stream().map(MtGenType::getTypeCode).distinct()
                                .collect(Collectors.toList());
                if (!proLineTypeCodes.contains(dto.getProdLineType())) {
                    throw new MtException("MT_MODELING_0002",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0002",
                                                    "MODELING", "prodLineType", "【API:prodLineBasicPropertyUpdate】"));
                }
            }

            MtModProductionLine mtModProductionLine = new MtModProductionLine();
            mtModProductionLine.setTenantId(tenantId);
            mtModProductionLine.setProdLineCode(dto.getProdLineCode());
            mtModProductionLine = this.mtModProductionLineMapper.selectOne(mtModProductionLine);
            if (null != mtModProductionLine) {
                throw new MtException("MT_MODELING_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0026", "MODELING", "prodLineCode", "【API:prodLineBasicPropertyUpdate】"));
            }

            dto.setTenantId(tenantId);
            self().insertSelective(dto);
            prodLineId = dto.getProdLineId();
        } else {
            // 修改逻辑
            if (null != dto.getProdLineCode() && "".equals(dto.getProdLineCode())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "prodLineCode", "【API:prodLineBasicPropertyUpdate】"));
            }
            if (null != dto.getProdLineName() && "".equals(dto.getProdLineName())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "prodLineName", "【API:prodLineBasicPropertyUpdate】"));
            }
            if (null != dto.getProdLineType() && "".equals(dto.getProdLineType())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "prodLineType", "【API:prodLineBasicPropertyUpdate】"));
            }
            if (null != dto.getEnableFlag() && "".equals(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "enableFlag", "【API:prodLineBasicPropertyUpdate】"));
            }
            if (null != dto.getEnableFlag() && !enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0035", "MODELING", "enableFlag", "【API:prodLineBasicPropertyUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getProdLineType())) {
                MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
                mtGenTypeVO2.setModule("MODELING");
                mtGenTypeVO2.setTypeGroup("PROD_LINE_TYPE");
                List<MtGenType> proLineTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
                List<String> proLineTypeCodes = proLineTypes.stream().map(MtGenType::getTypeCode).distinct()
                                .collect(Collectors.toList());
                if (!proLineTypeCodes.contains(dto.getProdLineType())) {
                    throw new MtException("MT_MODELING_0002",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0002",
                                                    "MODELING", "prodLineType", "【API:prodLineBasicPropertyUpdate】"));
                }
            }
            MtModProductionLine mtModProductionLine = new MtModProductionLine();
            mtModProductionLine.setTenantId(tenantId);
            mtModProductionLine.setProdLineId(dto.getProdLineId());
            mtModProductionLine = this.mtModProductionLineMapper.selectOne(mtModProductionLine);
            if (null == mtModProductionLine) {
                throw new MtException("MT_MODELING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0003", "MODELING", "prodLineId", "【API:prodLineBasicPropertyUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getProdLineCode())) {
                MtModProductionLine tmpMtModProductionLine = new MtModProductionLine();
                tmpMtModProductionLine.setTenantId(tenantId);
                tmpMtModProductionLine.setProdLineCode(dto.getProdLineCode());
                tmpMtModProductionLine = this.mtModProductionLineMapper.selectOne(tmpMtModProductionLine);
                if (null != tmpMtModProductionLine && !tmpMtModProductionLine.getProdLineId()
                                .equals(mtModProductionLine.getProdLineId())) {
                    throw new MtException("MT_MODELING_0026",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0026",
                                                    "MODELING", "prodLineCode", "【API:prodLineBasicPropertyUpdate】"));
                }
            }

            dto.setTenantId(tenantId);
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                self().updateByPrimaryKey(dto);
            } else {
                self().updateByPrimaryKeySelective(dto);
            }
        }

        return prodLineId;
    }

    @Override
    public List<MtModProductionLine> prodLineByproLineCodes(Long tenantId, List<String> prodLineCodes) {
        if (CollectionUtils.isEmpty(prodLineCodes)) {
            return Collections.emptyList();
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("t1.PROD_LINE_CODE", prodLineCodes, 1000);
        return mtModProductionLineMapper.selectByCodesCustom(tenantId, whereInValuesSql);
    }
}
