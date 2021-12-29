package tarzan.inventory.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.domain.vo.MtUserVO3;
import io.tarzan.common.infra.feign.MtUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tarzan.general.domain.entity.MtEventType;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventTypeRepository;
import tarzan.general.domain.vo.MtEventVO1;
import tarzan.inventory.api.dto.MtInvJournalDTO;
import tarzan.inventory.api.dto.MtInvJournalDTO2;
import tarzan.inventory.api.dto.MtInvJournalDTO3;
import tarzan.inventory.api.dto.MtInvJournalDTO4;
import tarzan.inventory.app.service.MtInvJournalService;
import tarzan.inventory.infra.mapper.MtInvJournalMapper;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.modeling.app.service.MtCustomerService;
import tarzan.modeling.app.service.MtSupplierService;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO;
import tarzan.modeling.domain.vo.MtModLocatorVO9;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存日记账应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
@Service
public class MtInvJournalServiceImpl implements MtInvJournalService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtEventTypeRepository mtEventTypeRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;

    @Autowired
    private MtUserService mtUserService;
    @Autowired
    private MtCustomerService mtCustomerService;
    @Autowired
    private MtSupplierService mtSupplierService;

    @Autowired
    private MtInvJournalMapper mtInvJournalMapper;

    @Autowired
    private MtUserClient userClient;

    @Override
    public List<MtInvJournalDTO2> queryInvJournalForUi(Long tenantId, MtInvJournalDTO dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getSiteId()) || dto.getStartTime() == null || dto.getEndTime() == null) {
            return Collections.emptyList();
        }
        if (!(StringUtils.isEmpty(dto.getOrgType()) && StringUtils.isEmpty(dto.getOrgId())
                || StringUtils.isNotEmpty(dto.getOrgType()) && StringUtils.isNotEmpty(dto.getOrgId()))) {
            throw new MtException("MT_INVENTORY_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0021", "INVENTORY"));
        }
        if (StringUtils.isEmpty(dto.getOrgType())) {
            dto.setOrgType("SITE");
        }

        List<String> locatorIdList;
        if ("LOCATOR".equals(dto.getOrgType())) {
            MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
            mtModLocatorVO9.setLocatorId(dto.getOrgId());
            mtModLocatorVO9.setQueryType("ALL");
            locatorIdList = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
            if (CollectionUtils.isEmpty(locatorIdList)) {
                locatorIdList = Collections.singletonList(dto.getOrgId());
            } else {
                locatorIdList.add(dto.getOrgId());
            }
        } else {
            // get locators of org and sub org list
            locatorIdList = getLocatorIdList(tenantId, dto);
        }
        if (CollectionUtils.isEmpty(locatorIdList)) {
            return Collections.emptyList();
        }

        List<String> finalLocatorIdList = locatorIdList.stream().distinct().collect(Collectors.toList());
        List<MtInvJournalDTO2> result = PageHelper.doPageAndSort(pageRequest,
                () -> mtInvJournalMapper.queryInvJournalForUi(tenantId, dto, finalLocatorIdList));

        // event & locator & material & owner
        List<MtEventVO1> eventList = Collections.emptyList();
        Map<String, MtEventType> eventTypeMap = new HashMap<>();
        List<MtModLocator> locatorList = Collections.emptyList();
        List<MtModLocator> warehouseList = Collections.emptyList();
        List<MtMaterialVO> materialList = Collections.emptyList();
        List<MtCustomer> customerList = Collections.emptyList();
        List<MtSupplier> supplierList = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(result)) {
            List<String> originEventIdList =
                    result.stream().map(MtInvJournalDTO2::getEventId).collect(Collectors.toList());
            eventList = mtEventRepository.eventBatchGet(tenantId, originEventIdList);
            if (CollectionUtils.isNotEmpty(eventList)) {
                List<String> eventTypeIdList = eventList.stream().map(MtEventVO1::getEventTypeId).distinct()
                        .collect(Collectors.toList());
                for (String typeId : eventTypeIdList) {
                    eventTypeMap.put(typeId, mtEventTypeRepository.eventTypeGet(tenantId, typeId));
                }
            }

            List<String> originLocatorIdList =
                    result.stream().map(MtInvJournalDTO2::getLocatorId).distinct().collect(Collectors.toList());
            locatorList = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, originLocatorIdList);

            //查询货位对应的仓库
            List<String> warehouseIdList = locatorList.stream().map(MtModLocator::getParentLocatorId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(warehouseIdList)){
                warehouseList = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, warehouseIdList);
            }

            List<String> originMaterialIdList =
                    result.stream().map(MtInvJournalDTO2::getMaterialId).collect(Collectors.toList());
            materialList = mtMaterialRepository.materialPropertyBatchGet(tenantId, originMaterialIdList);

            Map<String, List<MtInvJournalDTO3>> ownerMapPerOwnerType = result.stream().filter(
                    t -> StringUtils.isNotEmpty(t.getOwnerType()) && StringUtils.isNotEmpty(t.getOwnerId()))
                    .map(t -> new MtInvJournalDTO3(t.getOwnerType(), t.getOwnerId()))
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

        // user info
        Map<Long, List<MtUserVO3>> userMap = null;
        ResponseEntity<Page<MtUserVO3>> res =
                mtUserService.userByOrganization(tenantId, tenantId, MtBaseConstants.MAX_USER_SIZE);
        List<MtUserVO3> userVO3List = res != null && res.getBody() != null ? res.getBody() : new ArrayList<MtUserVO3>();
        if (null != userVO3List) {
            userMap = userVO3List.stream().collect(Collectors.groupingBy(MtUserVO3::getId));
        }

        // set journal info
        for (MtInvJournalDTO2 t : result) {
            Optional<MtEventVO1> event =
                    eventList.stream().filter(tt -> tt.getEventId().equals(t.getEventId())).findFirst();
            event.ifPresent(e -> {
                if (eventTypeMap.get(e.getEventTypeId()) != null) {
                    t.setEventType(eventTypeMap.get(e.getEventTypeId()).getEventTypeCode());
                    t.setEventTypeDesc(eventTypeMap.get(e.getEventTypeId()).getDescription());
                }
            });
            Optional<MtModLocator> locator =
                    locatorList.stream().filter(tt -> tt.getLocatorId().equals(t.getLocatorId())).findFirst();
            locator.ifPresent(l -> {
                t.setLocatorCode(l.getLocatorCode());
                t.setLocatorDesc(l.getLocatorName());
                t.setWarehouseId(l.getParentLocatorId());
            });

            Optional<MtModLocator> warehouse =
                    warehouseList.stream().filter(tt -> tt.getLocatorId().equals(t.getWarehouseId())).findFirst();
            warehouse.ifPresent(w -> {
                t.setWarehouseCode(w.getLocatorCode());
                t.setWarehouseDesc(w.getLocatorName());
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

            if (userMap != null && CollectionUtils.isNotEmpty(userMap.get(t.getEventBy()))) {
                t.setEventByUserName(userMap.get(t.getEventBy()).get(0).getLoginName());
            }
        }
        return result;
    }

    /**
     *
     * @Description 库存日记报表
     *
     * @author yuchao.wang
     * @date 2020/10/19 12:34
     * @param tenantId 租户Id
     * @param dto MtInvJournalDTO4
     * @param pageRequest PageRequest
     * @return java.util.List<tarzan.inventory.api.dto.MtInvJournalDTO2>
     *
     */
    @Override
    public List<MtInvJournalDTO2> queryInvJournalReport(Long tenantId, MtInvJournalDTO4 dto, PageRequest pageRequest) {
        List<MtInvJournalDTO2> result = PageHelper.doPageAndSort(pageRequest,
                () -> mtInvJournalMapper.queryInvJournalReport(tenantId, dto));

        //查询用户信息
        List<Long> userList = result.stream().map(MtInvJournalDTO2::getEventBy).filter(Objects::nonNull)
                .distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId, userList);

        //获取客户或者供应商信息
        List<String> ownerIdList = result.stream().map(MtInvJournalDTO2::getOwnerId).filter(StringUtils::isNotBlank)
                .distinct().collect(Collectors.toList());
        List<MtCustomer> customerList = mtCustomerService.batchSelectCustomerByIdList(tenantId, ownerIdList);
        List<MtSupplier> supplierList = mtSupplierService.batchSelectSupplierByIdList(tenantId, ownerIdList);

        //查询GenType
        MtGenTypeVO2 mtGenTypeVO = new MtGenTypeVO2();
        mtGenTypeVO.setModule("INVENTORY");
        mtGenTypeVO.setTypeGroup("INVENTORY_OWNER_TYPE");
        List<MtGenType> invOwnerTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO);

        //构造返回信息
        for (MtInvJournalDTO2 t : result) {
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

            if (userInfoMap != null && userInfoMap.containsKey(t.getEventBy())) {
                t.setEventByUserName(userInfoMap.get(t.getEventBy()).getLoginName());
            }
        }

        return result;
    }

    /**
     * 根据选择组织类型获取库位
     *
     * <ul>
     * <li>当类型为SITE或AREA时 查询AREA,WORKCELL,PROD_LINE</li>
     * <li>当类型为WORKCELL或PROD_LINE时 查询WORKCELL</li>
     * </ul>
     * 
     * @author benjamin
     * @date 2019-08-14 20:05
     * @param tenantId 租户Id
     * @param dto MtInvJournalDTO
     * @return list
     */
    private List<String> getLocatorIdList(Long tenantId, MtInvJournalDTO dto) {
        List<String> locatorIdList = new ArrayList<>();
        if ("SITE".equals(dto.getOrgType()) || "AREA".equals(dto.getOrgType())) {
            locatorIdList.addAll(queryLocatorListByOrgType(tenantId, dto, "AREA"));
            locatorIdList.addAll(queryLocatorListByOrgType(tenantId, dto, "WORKCELL"));
            locatorIdList.addAll(queryLocatorListByOrgType(tenantId, dto, "PROD_LINE"));
        } else if ("WORKCELL".equals(dto.getOrgType()) || "PROD_LINE".equals(dto.getOrgType())) {
            locatorIdList.addAll(queryLocatorListByOrgType(tenantId, dto, "WORKCELL"));
        }

        return locatorIdList;
    }

    /**
     * 限制组织查询组织下对应库位集合
     * 
     * @author benjamin
     * @date 2019-08-26 12:53
     * @param tenantId 租户Id
     * @param dto MtInvJournalDTO
     * @param targetOrgType 查询目标组织类型
     * @return list
     */
    private List<String> queryLocatorListByOrgType(Long tenantId, MtInvJournalDTO dto, String targetOrgType) {
        MtModOrganizationVO2 orgVO = new MtModOrganizationVO2();
        orgVO.setTopSiteId(dto.getSiteId());
        orgVO.setParentOrganizationType(dto.getOrgType());
        orgVO.setParentOrganizationId(dto.getSiteId());
        orgVO.setOrganizationType(targetOrgType);
        orgVO.setQueryType("ALL");

        // query locators of the sub organizations
        List<MtModOrganizationItemVO> orgItemList =
                        mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, orgVO);
        List<MtModLocatorOrgRelVO> locatorOrgRelList = new ArrayList<>();
        for (MtModOrganizationItemVO org : orgItemList) {
            locatorOrgRelList.addAll(mtModLocatorOrgRelRepository.organizationLimitLocatorQuery(tenantId, targetOrgType,
                            org.getOrganizationId(), "ALL"));
        }

        // query locators of the current organization
        if (StringUtils.isEmpty(dto.getOrgId())) {
            // not choose organization, then need to query locators of the current site
            locatorOrgRelList.addAll(mtModLocatorOrgRelRepository.organizationLimitLocatorQuery(tenantId, "SITE",
                            dto.getSiteId(), "ALL"));
        } else {
            // query locators of the current organization
            locatorOrgRelList.addAll(mtModLocatorOrgRelRepository.organizationLimitLocatorQuery(tenantId,
                            dto.getOrgType(), dto.getOrgId(), "ALL"));
        }

        return locatorOrgRelList.stream().map(MtModLocatorOrgRelVO::getLocatorId).collect(Collectors.toList());
    }
}
