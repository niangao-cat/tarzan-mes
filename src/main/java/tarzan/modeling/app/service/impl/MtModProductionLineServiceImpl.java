package tarzan.modeling.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import tarzan.modeling.api.dto.MtModProductionLineDTO2;
import tarzan.modeling.api.dto.MtModProductionLineDTO3;
import tarzan.modeling.api.dto.MtModProductionLineDTO4;
import tarzan.modeling.app.service.MtModProductionLineService;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.*;
import tarzan.modeling.domain.vo.MtModProdLineManufacturingVO2;
import tarzan.modeling.domain.vo.MtModProdLineScheduleVO2;
import tarzan.modeling.domain.vo.MtModProductionLineVO3;
import tarzan.modeling.domain.vo.MtModProductionLineVO4;
import tarzan.modeling.infra.mapper.MtModProductionLineMapper;

/**
 * 生产线应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Service
public class MtModProductionLineServiceImpl implements MtModProductionLineService {

    private static final String MT_MOD_PRODUCTION_LINE_ATTR = "mt_mod_production_line_attr";

    @Autowired
    private MtModProductionLineMapper mtModProductionLineMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtSupplierRepository mtSupplierRepository;

    @Autowired
    private MtSupplierSiteRepository mtSupplierSiteRepository;

    @Autowired
    private MtModProdLineScheduleRepository mtModProdLineScheduleRepository;

    @Autowired
    private MtModProdLineManufacturingRepository mtModProdLineManufacturingRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Override
    public Page<MtModProductionLineVO3> queryForUi(Long tenantId, MtModProductionLineDTO2 dto,
                    PageRequest pageRequest) {
        MtModProductionLine mtModProductionLine = new MtModProductionLine();
        mtModProductionLine.setTenantId(tenantId);
        mtModProductionLine.setDescription(dto.getDescription());
        mtModProductionLine.setEnableFlag(dto.getEnableFlag());
        mtModProductionLine.setProdLineCode(dto.getProdLineCode());
        mtModProductionLine.setProdLineName(dto.getProdLineName());
        mtModProductionLine.setProdLineType(dto.getProdLineType());

        Criteria criteria = new Criteria(mtModProductionLine);
        List<WhereField> whereFields = new ArrayList<WhereField>();

        whereFields.add(new WhereField(MtModProductionLine.FIELD_TENANT_ID, Comparison.EQUAL));

        if (dto.getDescription() != null) {
            whereFields.add(new WhereField(MtModProductionLine.FIELD_DESCRIPTION, Comparison.LIKE));
        }
        if (dto.getEnableFlag() != null) {
            whereFields.add(new WhereField(MtModProductionLine.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        }
        if (dto.getProdLineCode() != null) {
            whereFields.add(new WhereField(MtModProductionLine.FIELD_PROD_LINE_CODE, Comparison.LIKE));
        }
        if (dto.getProdLineName() != null) {
            whereFields.add(new WhereField(MtModProductionLine.FIELD_PROD_LINE_NAME, Comparison.LIKE));
        }
        if (dto.getProdLineType() != null) {
            whereFields.add(new WhereField(MtModProductionLine.FIELD_PROD_LINE_TYPE, Comparison.EQUAL));
        }

        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        Page<MtModProductionLine> list = PageHelper.doPageAndSort(pageRequest,
                        () -> this.mtModProductionLineMapper.selectOptional(mtModProductionLine, criteria));
        List<MtGenType> prodLineTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "PROD_LINE_TYPE");

        List<MtModProductionLineVO3> proLines = new ArrayList<MtModProductionLineVO3>();
        MtModProductionLineVO3 mtModProductionLineVO3 = null;

        for (MtModProductionLine productionLine : list) {
            mtModProductionLineVO3 = new MtModProductionLineVO3();
            BeanUtils.copyProperties(productionLine, mtModProductionLineVO3);
            if (null != mtModProductionLineVO3.getProdLineType()) {
                String proLineType = mtModProductionLineVO3.getProdLineType();
                Optional<MtGenType> optional =
                                prodLineTypes.stream().filter(t -> t.getTypeCode().equals(proLineType)).findFirst();
                if (optional.isPresent()) {
                    mtModProductionLineVO3.setProdLineTypeDesc(optional.get().getDescription());
                }
            }
            proLines.add(mtModProductionLineVO3);
        }

        Page<MtModProductionLineVO3> result = new Page<MtModProductionLineVO3>();
        result.setNumber(list.getNumber());
        result.setSize(list.getSize());
        result.setTotalElements(list.getTotalElements());
        result.setTotalPages(list.getTotalPages());
        result.setNumberOfElements(list.getNumberOfElements());
        result.setContent(proLines);

        return result;
    }

    @Override
    public MtModProductionLineDTO3 queryInfoForUi(Long tenantId, String prodLineId) {
        if (StringUtils.isEmpty(prodLineId)) {
            return null;
        }

        MtModProductionLineDTO3 dto = new MtModProductionLineDTO3();
        MtModProductionLine mtModProductionLine =
                        this.mtModProductionLineRepository.prodLineBasicPropertyGet(tenantId, prodLineId);
        if (null == mtModProductionLine) {
            return dto;
        }

        MtModProductionLineVO4 mtModProductionLineVO4 = new MtModProductionLineVO4();
        BeanUtils.copyProperties(mtModProductionLine, mtModProductionLineVO4);
        if (StringUtils.isNotEmpty(mtModProductionLineVO4.getProdLineType())) {
            List<MtGenType> proLineTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "PROD_LINE_TYPE");
            Optional<MtGenType> proLineType = proLineTypes.stream()
                            .filter(t -> t.getTypeCode().equals(mtModProductionLineVO4.getProdLineType())).findFirst();
            if (proLineType.isPresent()) {
                mtModProductionLineVO4.setProdLineTypeDesc(proLineType.get().getDescription());
            }
        }
        if (StringUtils.isNotEmpty(mtModProductionLineVO4.getSupplierId())) {
            MtSupplier mtSupplier = new MtSupplier();
            mtSupplier.setTenantId(tenantId);
            mtSupplier.setSupplierId(mtModProductionLineVO4.getSupplierId());
            mtSupplier = this.mtSupplierRepository.selectOne(mtSupplier);
            if (null != mtSupplier) {
                mtModProductionLineVO4.setSupplierCode(mtSupplier.getSupplierCode());
                mtModProductionLineVO4.setSupplierName(mtSupplier.getSupplierName());
            }
        }
        if (StringUtils.isNotEmpty(mtModProductionLineVO4.getSupplierSiteId())) {
            MtSupplierSite mtSupplierSite = new MtSupplierSite();
            mtSupplierSite.setTenantId(tenantId);
            mtSupplierSite.setSupplierSiteId(mtModProductionLineVO4.getSupplierSiteId());
            mtSupplierSite = this.mtSupplierSiteRepository.selectOne(mtSupplierSite);
            if (null != mtSupplierSite) {
                mtModProductionLineVO4.setSupplierSiteCode(mtSupplierSite.getSupplierSiteCode());
                mtModProductionLineVO4.setSupplierSiteName(mtSupplierSite.getSupplierSiteName());
            }
        }
        dto.setProductionLine(mtModProductionLineVO4);

        MtModProdLineSchedule mtModProdLineSchedule =
                        this.mtModProdLineScheduleRepository.prodLineSchedulePropertyGet(tenantId, prodLineId);
        if (null != mtModProdLineSchedule) {
            MtModProdLineScheduleVO2 mtModProdLineScheduleVO2 = new MtModProdLineScheduleVO2();
            BeanUtils.copyProperties(mtModProdLineSchedule, mtModProdLineScheduleVO2);
            if (StringUtils.isNotEmpty(mtModProdLineScheduleVO2.getRateType())) {
                List<MtGenType> rateTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "RATE_TYPE");
                Optional<MtGenType> rateType = rateTypes.stream()
                                .filter(t -> t.getTypeCode().equals(mtModProdLineScheduleVO2.getRateType()))
                                .findFirst();
                if (rateType.isPresent()) {
                    mtModProdLineScheduleVO2.setRateTypeDesc(rateType.get().getDescription());
                }
            }
            dto.setProdLineSchedule(mtModProdLineScheduleVO2);
        }

        MtModProdLineManufacturing mtModProdLineManufacturing = this.mtModProdLineManufacturingRepository
                        .prodLineManufacturingPropertyGet(tenantId, prodLineId);
        if (null != mtModProdLineManufacturing) {
            MtModProdLineManufacturingVO2 MtModProdLineManufacturingVO2 = new MtModProdLineManufacturingVO2();
            BeanUtils.copyProperties(mtModProdLineManufacturing, MtModProdLineManufacturingVO2);
            if (StringUtils.isNotEmpty(MtModProdLineManufacturingVO2.getDispatchMethod())) {
                List<MtGenType> dispatchMethods =
                                this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "DISPATCH_METHOD");
                Optional<MtGenType> dispatchMethod = dispatchMethods.stream()
                                .filter(t -> t.getTypeCode().equals(MtModProdLineManufacturingVO2.getDispatchMethod()))
                                .findFirst();
                if (dispatchMethod.isPresent()) {
                    MtModProdLineManufacturingVO2.setDispatchMethodDesc(dispatchMethod.get().getDescription());
                }
            }

            List<String> locatorIds = new ArrayList<String>();
            if (StringUtils.isNotEmpty(MtModProdLineManufacturingVO2.getIssuedLocatorId())) {
                locatorIds.add(mtModProdLineManufacturing.getIssuedLocatorId());
            }
            if (StringUtils.isNotEmpty(MtModProdLineManufacturingVO2.getCompletionLocatorId())) {
                locatorIds.add(mtModProdLineManufacturing.getCompletionLocatorId());
            }
            if (StringUtils.isNotEmpty(MtModProdLineManufacturingVO2.getInventoryLocatorId())) {
                locatorIds.add(mtModProdLineManufacturing.getInventoryLocatorId());
            }
            if (CollectionUtils.isNotEmpty(locatorIds)) {
                List<MtModLocator> locators =
                                this.mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, locatorIds);
                for (MtModLocator locator : locators) {
                    if (locator.getLocatorId().equals(MtModProdLineManufacturingVO2.getIssuedLocatorId())) {
                        MtModProdLineManufacturingVO2.setIssuedLocatorCode(locator.getLocatorCode());
                        MtModProdLineManufacturingVO2.setIssuedLocatorName(locator.getLocatorName());
                    }
                    if (locator.getLocatorId().equals(MtModProdLineManufacturingVO2.getCompletionLocatorId())) {
                        MtModProdLineManufacturingVO2.setCompletionLocatorCode(locator.getLocatorCode());
                        MtModProdLineManufacturingVO2.setCompletionLocatorName(locator.getLocatorName());
                    }
                    if (locator.getLocatorId().equals(MtModProdLineManufacturingVO2.getInventoryLocatorId())) {
                        MtModProdLineManufacturingVO2.setInventoryLocatorCode(locator.getLocatorCode());
                        MtModProdLineManufacturingVO2.setInventoryLocatorName(locator.getLocatorName());
                    }
                }
            }
            dto.setProdLineManufacturing(MtModProdLineManufacturingVO2);
        }

        List<MtExtendAttrDTO> prodLineAttrs =
                        mtExtendSettingsService.attrQuery(tenantId, prodLineId, MT_MOD_PRODUCTION_LINE_ATTR);
        dto.setProdLineAttrs(prodLineAttrs);
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveForUi(Long tenantId, MtModProductionLineDTO4 dto) {
        if (null == dto || null == dto.getProductionLine()) {
            return null;
        }

        MtModProductionLine mtModProductionLine = new MtModProductionLine();
        BeanUtils.copyProperties(dto.getProductionLine(), mtModProductionLine);

        mtModProductionLine.setProdLineCategory("");
        String prodLineId = this.mtModProductionLineRepository.prodLineBasicPropertyUpdate(tenantId,
                        mtModProductionLine, "Y");
        if (CollectionUtils.isNotEmpty(dto.getProdLineAttrs())) {
            mtExtendSettingsService.attrSave(tenantId, MT_MOD_PRODUCTION_LINE_ATTR, prodLineId, null,
                            dto.getProdLineAttrs());
        }

        if (null != dto.getProdLineSchedule()) {
            dto.getProdLineSchedule().setProdLineId(prodLineId);
            this.mtModProdLineScheduleRepository.prodLineSchedulePropertyUpdate(tenantId, dto.getProdLineSchedule(),
                            "Y");
        }

        if (null != dto.getProdLineManufacturing()) {
            dto.getProdLineManufacturing().setProdLineId(prodLineId);
            this.mtModProdLineManufacturingRepository.prodLineManufacturingPropertyUpdate(tenantId,
                            dto.getProdLineManufacturing(), "Y");
        }

        return prodLineId;
    }

}
