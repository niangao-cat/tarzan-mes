package com.ruike.itf.app.service.impl;

import com.ruike.hme.domain.entity.HmeAfterSalesRepair;
import com.ruike.hme.domain.entity.HmeLogisticsInfo;
import com.ruike.hme.domain.entity.HmeServiceReceive;
import com.ruike.hme.domain.repository.HmeAfterSalesRepairRepository;
import com.ruike.hme.domain.repository.HmeLogisticsInfoRepository;
import com.ruike.hme.domain.repository.HmeServiceReceiveRepository;
import com.ruike.hme.infra.mapper.HmeAfterSalesRepairMapper;
import com.ruike.itf.api.dto.ItfAfterSalesRepairSyncDTO;
import com.ruike.itf.app.service.ItfAfterSalesRepairIfacesService;
import com.ruike.itf.domain.entity.ItfAfterSalesRepairIfaces;
import com.ruike.itf.domain.repository.ItfAfterSalesRepairIfacesRepository;
import com.ruike.itf.utils.GetDeclaredFields;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.infra.mapper.MtCustomerMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 售后登记平台表应用服务默认实现
 *
 * @author kejin.liu01@hand-china.com 2020-09-02 09:59:41
 */
@Service
@Slf4j
public class ItfAfterSalesRepairIfacesServiceImpl implements ItfAfterSalesRepairIfacesService {

    @Autowired
    private HmeAfterSalesRepairRepository hmeAfterSalesRepairRepository;

    @Autowired
    private ItfAfterSalesRepairIfacesRepository itfAfterSalesRepairIfacesRepository;

    @Autowired
    private HmeLogisticsInfoRepository logisticsInfoRepository;

    @Autowired
    private HmeServiceReceiveRepository hmeServiceReceiveRepository;

    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtCustomerMapper mtCustomerMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtModLocatorRepository locatorRepository;

    @Autowired
    private HmeAfterSalesRepairMapper hmeAfterSalesRepairMapper;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;


    /**
     * 大仓等级平台同步接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ItfAfterSalesRepairIfaces> afterSalesRepairSync(List<ItfAfterSalesRepairSyncDTO> dto) {
        // 校验必输项，存入接口记录表
        GetDeclaredFields<ItfAfterSalesRepairSyncDTO> head = new GetDeclaredFields<>();
        String[] headFields = {"SERNR1", "WERKS", "MATNR1", "BOLNR", "LGORT"};

        List<ItfAfterSalesRepairIfaces> newList = new ArrayList<>();
        for (ItfAfterSalesRepairSyncDTO salesRepairSyncDTO : dto) {
            StringBuilder errorMsg = new StringBuilder();
            // 获取值为空的字段
            List<String> declaredFields = head.getDeclaredFields(salesRepairSyncDTO, headFields);

            if (CollectionUtils.isNotEmpty(declaredFields)) {
                for (String fie : declaredFields) {
                    errorMsg.append(fie).append(":不允许为空！");
                }
                salesRepairSyncDTO.setMESSAGE(errorMsg.toString());
                salesRepairSyncDTO.setSTATUS("N");
            }
            ItfAfterSalesRepairIfaces itfAfterSalesRepairIfaces = new ItfAfterSalesRepairIfaces(salesRepairSyncDTO, tenantId);

            newList.add(itfAfterSalesRepairIfaces);
        }
        // 根据编码获取ID，存入业务表
        List<ItfAfterSalesRepairIfaces> errorList = new ArrayList<>();
        for (ItfAfterSalesRepairIfaces ifaces : newList) {
            if (Strings.isEmpty(ifaces.getMessage())) {
                HmeAfterSalesRepair hmeAfterSalesRepair = new HmeAfterSalesRepair();
                BeanUtils.copyProperties(ifaces, hmeAfterSalesRepair);
                hmeAfterSalesRepair.setWorkOrderId(ifaces.getWorkOrderNum());
                hmeAfterSalesRepair.setTenantId(tenantId);
                StringBuilder errorMsg = new StringBuilder();
                // 查询logistics_info_id
                String logisticsNumber = ifaces.getLogisticsNumber();
                HmeLogisticsInfo hmeLogisticsInfo = new HmeLogisticsInfo();
                hmeLogisticsInfo.setLogisticsNumber(logisticsNumber);
                hmeLogisticsInfo.setTenantId(tenantId);
                List<HmeLogisticsInfo> hmeLogisticsInfos = logisticsInfoRepository.select(hmeLogisticsInfo);
                if (hmeLogisticsInfos.size() == 0) {
                    errorMsg.append("根据[logisticsNumber]查询不到HmeLogisticsInfo.logisticsInfoId，请与ERP核对！");
                } else {
                    // 查询service_receive_id
                    String snNum = ifaces.getSnNum();
                    HmeServiceReceive hmeServiceReceive = new HmeServiceReceive();
                    hmeServiceReceive.setLogisticsInfoId(hmeLogisticsInfos.get(0).getLogisticsInfoId());
                    hmeServiceReceive.setSnNum(snNum);
                    hmeServiceReceive.setTenantId(tenantId);
                    List<HmeServiceReceive> hmeServiceReceives = hmeServiceReceiveRepository.select(hmeServiceReceive);
                    if (hmeServiceReceives.size() == 0) {
                        errorMsg.append("根据[logisticsInfoId,snNum]查询不到HmeServiceReceive.serviceReceiveId，请与ERP核对！");
                    } else {
                        hmeAfterSalesRepair.setLogisticsInfoId(hmeLogisticsInfos.get(0).getLogisticsInfoId());
                        hmeAfterSalesRepair.setServiceReceiveId(hmeServiceReceives.get(0).getServiceReceiveId());
                    }
                }

                // 查询工厂ID
                String plantCode = ifaces.getPlantCode();
                MtSitePlantReleation mtSitePlantReleation = new MtSitePlantReleation();
                mtSitePlantReleation.setTenantId(tenantId);
                mtSitePlantReleation.setPlantCode(plantCode);
                List<MtSitePlantReleation> mtSitePlantReleations = mtSitePlantReleationRepository.select(mtSitePlantReleation);
                if (mtSitePlantReleations.size() == 0) {
                    errorMsg.append("根据[plantCode]查询不到MtSitePlantReleation.siteId，请与MES核对！");
                } else {
                    hmeAfterSalesRepair.setSiteId(mtSitePlantReleations.get(0).getSiteId());
                }

                // 查询物料ID
                String materialCode = ifaces.getMaterialCode();
                MtMaterial mtMaterial = new MtMaterial();
                mtMaterial.setTenantId(tenantId);
                mtMaterial.setMaterialCode(materialCode);
                List<MtMaterial> mtMaterials = mtMaterialRepository.select(mtMaterial);
                if (mtMaterials.size() == 0) {
                    errorMsg.append("根据[materialCode]查询不到MtMaterial.materialId，请与MES核对！");
                } else {
                    hmeAfterSalesRepair.setMaterialId(mtMaterials.get(0).getMaterialId());
                }

                // 查询客户ID
                String customerCode = ifaces.getCustomerCode();
                MtCustomer mtCustomer = new MtCustomer();
                mtCustomer.setTenantId(tenantId);
                mtCustomer.setCustomerCode(customerCode);
                List<MtCustomer> mtCustomers = mtCustomerMapper.select(mtCustomer);
                hmeAfterSalesRepair.setCustomerId(CollectionUtils.isEmpty(mtCustomers) ? null : mtCustomers.get(0).getCustomerId());

                // 查询仓库货位
                MtModLocator locator = new MtModLocator();
                locator.setTenantId(tenantId);
                locator.setLocatorCode(ifaces.getLocatorCode());
                List<MtModLocator> locators = locatorRepository.select(locator);
                if (locators.size() == 0) {
                    errorMsg.append("入库仓库编码不存在");
                } else {
                    hmeAfterSalesRepair.setLocatorId(locators.get(0).getLocatorId());
                }

                ifaces.setMessage(errorMsg.toString());
                // 插入业务表
                if (Strings.isEmpty(ifaces.getMessage())) {
                    // 查询是否存在，存在则更新
                    HmeAfterSalesRepair hmeAfterSalesRepair1 = new HmeAfterSalesRepair();
                    hmeAfterSalesRepair1.setSnNum(hmeAfterSalesRepair.getSnNum());
                    hmeAfterSalesRepair1.setLogisticsNumber(hmeAfterSalesRepair.getLogisticsNumber());
                    List<HmeAfterSalesRepair> hmeAfterSalesRepairs = hmeAfterSalesRepairRepository.select(hmeAfterSalesRepair1);
                    if (hmeAfterSalesRepairs.size() == 0) {
                        hmeAfterSalesRepair.setAfterSalesRepairId(customDbRepository.getNextKey("hme_after_sales_repair_s"));
                        hmeAfterSalesRepairRepository.insertSelective(hmeAfterSalesRepair);
                    } else {
                        hmeAfterSalesRepair.setAfterSalesRepairId(hmeAfterSalesRepairs.get(0).getAfterSalesRepairId());
                        hmeAfterSalesRepairMapper.updateByPrimaryKeySelective(hmeAfterSalesRepair);
                    }
                    ifaces.setIsFlag("Y");
                } else {
                    ifaces.setIsFlag("N");
                }
            }
            // 插入接口记录表
            itfAfterSalesRepairIfacesRepository.insertSelective(ifaces);
            if (Strings.isNotEmpty(ifaces.getMessage())) {
                errorList.add(ifaces);
            }
        }

        return errorList;
    }
}
