package com.ruike.wms.app.service.impl;


import com.ruike.wms.app.service.WmsInvOnhandQuantityService;
import com.ruike.wms.domain.vo.WmsInvJournalExportVO;
import com.ruike.wms.domain.vo.WmsInvOnhandQuantityVO;
import com.ruike.wms.infra.mapper.WmsInvOnhandQuantityMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.infra.feign.MtUserService;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.lang3.StringUtils;
import org.hzero.export.vo.ExportParam;
import org.springframework.stereotype.Service;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventTypeRepository;
import tarzan.inventory.api.dto.MtInvJournalDTO;
import tarzan.inventory.api.dto.MtInvJournalDTO2;
import tarzan.inventory.api.dto.MtInvJournalDTO4;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO10;
import tarzan.inventory.infra.mapper.MtInvJournalMapper;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.app.service.MtCustomerService;
import tarzan.modeling.app.service.MtSupplierService;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存量 管理 服务实现
 *
 * @author kun.zhou 2020/04/29 13:27 现有量查询代码从产品路径迁出
 */
@Service
public class WmsInvOnhandQuantityServiceImpl implements WmsInvOnhandQuantityService {

    private final MtModLocatorRepository mtModLocatorRepository;
    private final WmsInvOnhandQuantityMapper invOnhandQuantityMapper;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtInvJournalMapper mtInvJournalMapper;
    private final MtEventRepository mtEventRepository;
    private final MtEventTypeRepository mtEventTypeRepository;
    private final MtMaterialRepository mtMaterialRepository;
    private final MtCustomerService mtCustomerService;
    private final MtSupplierService mtSupplierService;
    private final MtGenTypeRepository mtGenTypeRepository;
    private final MtUserService mtUserService;
    private final MtModOrganizationRelRepository mtModOrganizationRelRepository;
    private final MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;
    private final MtUserClient userClient;

    public WmsInvOnhandQuantityServiceImpl(MtModLocatorRepository mtModLocatorRepository,
                                           WmsInvOnhandQuantityMapper invOnhandQuantityMapper,
                                           MtErrorMessageRepository mtErrorMessageRepository,
                                           MtInvJournalMapper mtInvJournalMapper,
                                           MtEventRepository mtEventRepository,
                                           MtEventTypeRepository mtEventTypeRepository,
                                           MtMaterialRepository mtMaterialRepository,
                                           MtCustomerService mtCustomerService,
                                           MtSupplierService mtSupplierService,
                                           MtGenTypeRepository mtGenTypeRepository,
                                           MtUserService mtUserService,
                                           MtModOrganizationRelRepository mtModOrganizationRelRepository,
                                           MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository,
                                           MtUserClient userClient) {
        this.mtModLocatorRepository = mtModLocatorRepository;
        this.invOnhandQuantityMapper = invOnhandQuantityMapper;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.mtInvJournalMapper = mtInvJournalMapper;
        this.mtEventRepository = mtEventRepository;
        this.mtEventTypeRepository = mtEventTypeRepository;
        this.mtMaterialRepository = mtMaterialRepository;
        this.mtCustomerService = mtCustomerService;
        this.mtSupplierService = mtSupplierService;
        this.mtGenTypeRepository = mtGenTypeRepository;
        this.mtUserService = mtUserService;
        this.mtModOrganizationRelRepository = mtModOrganizationRelRepository;
        this.mtModLocatorOrgRelRepository = mtModLocatorOrgRelRepository;
        this.userClient = userClient;
    }


    @Override
    public WmsInvOnhandQuantityVO onhandQuantityQuery(Long tenantId, MtInvOnhandQuantityVO10 dto, PageRequest pageRequest) {
        //按条件查询
        Page<MtInvOnhandQuantityVO10> onhandQuantityList = PageHelper.doPage(pageRequest, () -> invOnhandQuantityMapper.onhandQuantityQuery(tenantId, dto));
        //计算库存合计
        BigDecimal onhandQuantitySum = invOnhandQuantityMapper.countOnhandQuantity(tenantId, dto);
        for (MtInvOnhandQuantityVO10 item : onhandQuantityList) {
            //获取仓库编码和仓库名称
            String parentLocatorId = mtModLocatorRepository.locatorBasicPropertyGet(tenantId, item.getLocatorId()).getParentLocatorId();
            if (StringUtils.isNotEmpty(parentLocatorId)) {
                MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId, parentLocatorId);
                item.setWarehouseCode(mtModLocator.getLocatorCode());
                item.setWarehouseName(mtModLocator.getLocatorName());
            }
            //赋值库存合计
            item.setOnhandQuantitySum(Optional.ofNullable(onhandQuantitySum).orElse(BigDecimal.ZERO).stripTrailingZeros().toPlainString());
        }
        WmsInvOnhandQuantityVO quantityVO = new WmsInvOnhandQuantityVO();
        quantityVO.setTotalPages(onhandQuantityList.getTotalPages());
        quantityVO.setTotalElements(onhandQuantityList.getTotalElements());
        quantityVO.setNumber(onhandQuantityList.getNumber());
        quantityVO.setNumberOfElements(onhandQuantityList.getNumberOfElements());
        quantityVO.setSize(onhandQuantityList.getSize());
        quantityVO.setOnhandQuantitySum(onhandQuantitySum);
        quantityVO.setContent(onhandQuantityList.getContent());
        return quantityVO;
    }

    @Override
    public List<MtInvOnhandQuantityVO10> export(Long tenantId, MtInvOnhandQuantityVO10 dto, ExportParam exportParam) {
        return invOnhandQuantityMapper.onhandQuantityExport(tenantId, dto);
    }

    @Override
    public List<WmsInvJournalExportVO> invJournalExcelExport(Long tenantId, MtInvJournalDTO4 dto) {
        List<MtInvJournalDTO2> result = mtInvJournalMapper.queryInvJournalReport(tenantId, dto);
        if (result.size() > 3000) {
            result = result.subList(0, 3000);
        }
        //时间倒序
        result = result.stream().sorted(Comparator.comparing(MtInvJournalDTO2::getEventTime).reversed()).collect(Collectors.toList());
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

        List<WmsInvJournalExportVO> exportVOList = new ArrayList<>();
        BeanCopier copier = BeanCopier.create(MtInvJournalDTO2.class, WmsInvJournalExportVO.class, false);

        //构造返回信息
        for (MtInvJournalDTO2 t : result) {
            WmsInvJournalExportVO exportVO = new WmsInvJournalExportVO();
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
            copier.copy(t, exportVO, null);
            if (exportVO.getEventTime() != null) {
                exportVO.setEventTimeStr(DateUtil.date2String(exportVO.getEventTime(), "yyyy-MM-dd HH:mm:ss"));
            }
            if (StringUtils.isBlank(exportVO.getOwnerTypeDesc())) {
                exportVO.setOwnerTypeDesc("自有");
            }
            exportVOList.add(exportVO);
        }
        return exportVOList;
    }

    /**
     * 根据选择组织类型获取库位
     *
     * <ul>
     * <li>当类型为SITE或AREA时 查询AREA,WORKCELL,PROD_LINE</li>
     * <li>当类型为WORKCELL或PROD_LINE时 查询WORKCELL</li>
     * </ul>
     *
     * @param tenantId 租户Id
     * @param dto      MtInvJournalDTO
     * @return list
     * @author benjamin
     * @date 2019-08-14 20:05
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
     * @param tenantId      租户Id
     * @param dto           MtInvJournalDTO
     * @param targetOrgType 查询目标组织类型
     * @return list
     * @author benjamin
     * @date 2019-08-26 12:53
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
