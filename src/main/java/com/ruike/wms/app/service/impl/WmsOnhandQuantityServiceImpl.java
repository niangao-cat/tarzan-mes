package com.ruike.wms.app.service.impl;

import com.ruike.wms.app.service.WmsOnhandQuantityService;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.inventory.api.dto.MtInvJournalDTO3;
import tarzan.inventory.api.dto.MtInvOnhandQuantityDTO;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO4;
import tarzan.inventory.infra.mapper.MtInvOnhandQuantityMapper;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.modeling.api.dto.MtModOrganizationRelDTO7;
import tarzan.modeling.app.service.MtCustomerService;
import tarzan.modeling.app.service.MtModOrganizationRelService;
import tarzan.modeling.app.service.MtSupplierService;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.MtModOrganizationChildrenVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO3;

import java.util.*;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author li.zhang 2021/09/24 16:26
 */
@Service
public class WmsOnhandQuantityServiceImpl implements WmsOnhandQuantityService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtInvOnhandQuantityMapper mtInvOnhandQuantityMapper;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtSupplierService mtSupplierService;
    @Autowired
    private MtCustomerService mtCustomerService;
    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;
    @Autowired
    private MtModOrganizationRelService mtModOrganizationRelService;

    @Override
    public List<MtInvOnhandQuantityVO4> export(Long tenantId, MtInvOnhandQuantityDTO dto, ExportParam exportParam) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0001", "INVENTORY", "siteId", "【API：invOnhandQueryForUi】"));
        }
        if (!(StringUtils.isEmpty(dto.getOrgType()) && StringUtils.isEmpty(dto.getOrgId())
                || StringUtils.isNotEmpty(dto.getOrgType()) && StringUtils.isNotEmpty(dto.getOrgId()))) {
            throw new MtException("MT_INVENTORY_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0021", "INVENTORY"));
        }
        List<MtModOrganizationVO3> orgList = new ArrayList<>();
        MtModOrganizationRelDTO7 mtModOrganizationRelDTO7 = new MtModOrganizationRelDTO7();
        mtModOrganizationRelDTO7.setTopSiteId(dto.getSiteId());
        mtModOrganizationRelDTO7.setParentOrganizationId(dto.getOrgId());
        mtModOrganizationRelDTO7.setParentOrganizationType(dto.getOrgType());
        MtModOrganizationChildrenVO mtModOrganizationChildrenVO =  mtModOrganizationRelService.getChildrenByParentForUi(tenantId,mtModOrganizationRelDTO7);
        MtModOrganizationVO3 mtModOrganizationVO3 = new MtModOrganizationVO3();
        mtModOrganizationVO3.setOrgId(mtModOrganizationChildrenVO.getId());
        mtModOrganizationVO3.setOrgType(mtModOrganizationChildrenVO.getType());
        orgList.add(mtModOrganizationVO3);
        List<MtModOrganizationVO3> orgList1 = checkedOrganization(mtModOrganizationChildrenVO);
        if(CollectionUtils.isNotEmpty(orgList1)){
            orgList.addAll(orgList1);
        }
        dto.setOrgList(orgList);

        List<String> locatorIdList = null;
        if (CollectionUtils.isNotEmpty(dto.getOrgList())) {
            locatorIdList = dto.getOrgList().stream().filter(o -> "LOCATOR".equals(o.getOrgType()))
                    .map(MtModOrganizationVO3::getOrgId).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(locatorIdList)) {
            return Collections.emptyList();
        }

        // query inventory onhand quantity
        List<String> finalLocatorIdList = locatorIdList;
        List<MtInvOnhandQuantityVO4> result = mtInvOnhandQuantityMapper.queryInventoryOnhandQuantityForUi(tenantId, dto, finalLocatorIdList, dto.getLotCode());

        // site & locator & material & owner
        List<MtModSite> siteList = Collections.emptyList();
        List<MtModLocator> locatorList = Collections.emptyList();
        List<MtMaterialVO> materialList = Collections.emptyList();
        List<MtCustomer> customerList = Collections.emptyList();
        List<MtSupplier> supplierList = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(result)) {
            List<String> originSiteIdList = result.stream().map(MtInvOnhandQuantityVO4::getSiteId).distinct()
                    .collect(Collectors.toList());
            siteList = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, originSiteIdList);
            List<String> originLocatorIdList = result.stream().map(MtInvOnhandQuantityVO4::getLocatorId).distinct()
                    .collect(Collectors.toList());
            locatorList = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, originLocatorIdList);
            List<String> originMaterialIdList =
                    result.stream().map(MtInvOnhandQuantityVO4::getMaterialId).collect(Collectors.toList());
            materialList = mtMaterialRepository.materialPropertyBatchGet(tenantId, originMaterialIdList);
            Map<String, List<MtInvJournalDTO3>> ownerMapPerOwnerType =
                    result.stream().map(t -> new MtInvJournalDTO3(t.getOwnerType(), t.getOwnerId()))
                            .collect(Collectors.groupingBy(MtInvJournalDTO3::getOwnerType));
            List<MtInvJournalDTO3> supList = new ArrayList<>();
            List<MtInvJournalDTO3> cusList = new ArrayList<>();
            for (Map.Entry<String, List<MtInvJournalDTO3>> t : ownerMapPerOwnerType.entrySet()) {
                if ("SI".equals(t.getKey()) || "IIS".equals(t.getKey())) {
                    supList.addAll(t.getValue());
                }
                if ("CI".equals(t.getKey()) || "IIC".equals(t.getKey())) {
                    cusList.addAll(t.getValue());
                }
            }
            customerList = mtCustomerService.batchSelectCustomerByIdList(tenantId,
                    cusList.stream().map(MtInvJournalDTO3::getOwnerId).collect(Collectors.toList()));
            supplierList = mtSupplierService.batchSelectSupplierByIdList(tenantId,
                    supList.stream().map(MtInvJournalDTO3::getOwnerId).collect(Collectors.toList()));
        }

        MtGenTypeVO2 mtGenTypeVO = new MtGenTypeVO2();
        mtGenTypeVO.setModule("INVENTORY");
        mtGenTypeVO.setTypeGroup("INVENTORY_OWNER_TYPE");
        List<MtGenType> invOwnerTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO);

        // set journal info
        for (MtInvOnhandQuantityVO4 t : result) {
            Optional<MtModSite> site = siteList.stream().filter(tt -> tt.getSiteId().equals(t.getSiteId())).findFirst();
            site.ifPresent(s -> t.setSiteCode(s.getSiteCode()));

            Optional<MtModLocator> locator =
                    locatorList.stream().filter(tt -> tt.getLocatorId().equals(t.getLocatorId())).findFirst();
            locator.ifPresent(l -> {
                t.setLocatorCode(l.getLocatorCode());
                t.setLocatorDesc(l.getLocatorName());
            });

            Optional<MtMaterialVO> material = materialList.stream()
                    .filter(tt -> tt.getMaterialId().equals(t.getMaterialId())).findFirst();
            material.ifPresent(m -> {
                t.setMaterialCode(m.getMaterialCode());
                t.setMaterialDesc(m.getMaterialName());
            });

            switch (t.getOwnerType()) {
                case "SI":
                case "IIS":
                    Optional<MtSupplier> supplier = supplierList.stream()
                            .filter(tt -> tt.getSupplierId().equals(t.getOwnerId())).findFirst();
                    supplier.ifPresent(s -> {
                        t.setOwnerDesc(s.getSupplierName());
                        t.setOwnerCode(s.getSupplierCode());
                    });
                    break;
                case "CI":
                case "IIC":
                    Optional<MtCustomer> customer = customerList.stream()
                            .filter(tt -> tt.getCustomerId().equals(t.getOwnerId())).findFirst();
                    customer.ifPresent(c -> {
                        t.setOwnerDesc(c.getCustomerName());
                        t.setOwnerCode(c.getCustomerCode());
                    });
                    break;
                case "OI":
                case "PI":
                    t.setOwnerCode(t.getOwnerId());
                    t.setOwnerDesc("");
                    break;
                default:
                    break;
            }

            Optional<MtGenType> first =
                    invOwnerTypes.stream().filter(tt -> tt.getTypeCode().equals(t.getOwnerType())).findFirst();
            first.ifPresent(mtGenType -> t.setOwnerTypeDesc(mtGenType.getDescription()));
        }
        for(MtInvOnhandQuantityVO4 mtInvOnhandQuantityVO4 : result){
            if(StringUtils.isBlank(mtInvOnhandQuantityVO4.getOwnerTypeDesc())){
                mtInvOnhandQuantityVO4.setOwnerTypeDesc("自有");
            }
        }
        return result;
    }

    private List<MtModOrganizationVO3> checkedOrganization(MtModOrganizationChildrenVO mtModOrganizationChildrenVO) {
        List<MtModOrganizationVO3> mtModOrganizationVO3s = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(mtModOrganizationChildrenVO.getChildren())){
            for(MtModOrganizationChildrenVO m:mtModOrganizationChildrenVO.getChildren()){
                MtModOrganizationVO3 mtModOrganizationVO3 = new MtModOrganizationVO3();
                mtModOrganizationVO3.setOrgId(m.getId());
                mtModOrganizationVO3.setOrgType(m.getType());
                mtModOrganizationVO3s.add(mtModOrganizationVO3);
                List<MtModOrganizationVO3> mtModOrganizationVO3s1 = checkedOrganization(m);
                if(CollectionUtils.isNotEmpty(mtModOrganizationVO3s1)){
                    mtModOrganizationVO3s.addAll(mtModOrganizationVO3s1);
                }
            }
        }
        return mtModOrganizationVO3s;
    }


}
