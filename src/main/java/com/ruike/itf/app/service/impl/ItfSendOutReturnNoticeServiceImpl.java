package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfReceiveMaterialProductionOrderReturnDTO;
import com.ruike.itf.api.dto.ItfReceiveReturnDTO;
import com.ruike.itf.api.dto.ItfSendOutReturnDTO2;
import com.ruike.itf.api.dto.ItfSendOutReturnNoticeDTO;
import com.ruike.itf.app.service.ItfSendOutReturnNoticeService;
import com.ruike.itf.domain.entity.CostcenterDocIface;
import com.ruike.itf.domain.entity.SoDeliveryIface;
import com.ruike.itf.domain.repository.SoDeliveryIfaceRepository;
import com.ruike.itf.infra.mapper.SoDeliveryIfaceMapper;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.MtIdHelper;
import io.tarzan.common.domain.vo.MtExtendVO5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.infra.repository.impl.MtEventRepositoryImpl;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionDocVO4;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO6;
import tarzan.instruction.infra.mapper.MtInstructionMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtCustomerRepository;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO2;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItfSendOutReturnNoticeServiceImpl implements ItfSendOutReturnNoticeService {
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private SoDeliveryIfaceMapper soDeliveryIfaceMapper;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtInstructionMapper mtInstructionMapper;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private SoDeliveryIfaceRepository soDeliveryIfaceRepository;
    @Autowired
    private MtEventRepositoryImpl mtEventRepository;
    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;
    @Autowired
    private MtCustomerRepository mtCustomerRepository;
    private static String[] parsePatterns = {"yyyy-MM-dd"};

    @Override
    public ItfReceiveMaterialProductionOrderReturnDTO create(List<ItfSendOutReturnNoticeDTO> list) {
        //返回值
        ItfReceiveMaterialProductionOrderReturnDTO returnDTO = new ItfReceiveMaterialProductionOrderReturnDTO();

        List<ItfReceiveReturnDTO> returnDetailDTOList = new ArrayList<>();

        //校验是否有数据
        if (CollectionUtils.isEmpty(list)) {
            returnDTO.setProcessDate(new Date());
            returnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            returnDTO.setProcessMessage("传输数据为空");
            return returnDTO;
        }
        //租户默认取0，批次
        Long tenantId = 0L;
        Long batchId = Long.valueOf((new SimpleDateFormat("yyyyMMddhhmmss")).format(new Date()));

        //写入接口表
        List<SoDeliveryIface> soDeliveryIfaceList = insertIface(tenantId, list, batchId);
        //查询失败的数据
        List<SoDeliveryIface> failedDocList = soDeliveryIfaceList.stream().filter(v -> WmsConstant.KEY_IFACE_STATUS_ERROR.equals(v.getStatus())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(failedDocList)) {
            for (SoDeliveryIface failedDoc : failedDocList) {
                ItfReceiveReturnDTO returnDetailDTO = new ItfReceiveReturnDTO();
                returnDetailDTO.setProcessStatus(failedDoc.getStatus());
                returnDetailDTO.setProcessMessage(failedDoc.getMessage());
                returnDetailDTO.setProcessDate(new Date());
                returnDetailDTO.setInstructionNum(failedDoc.getInstructionNum());
                returnDetailDTO.setInstructionDocNum(failedDoc.getInstructionDocNum());
                returnDetailDTOList.add(returnDetailDTO);
            }
            //更新接口表信息
            soDeliveryIfaceRepository.batchUpdateByPrimaryKeySelective(failedDocList);
        }
        // 校验通过的数据
        Map<String, List<SoDeliveryIface>> instructionDocMap =
                soDeliveryIfaceList.stream().filter(v -> "N".equals(v.getStatus())).collect(Collectors.groupingBy(SoDeliveryIface::getInstructionDocNum));

        List<SoDeliveryIface> updateIfaces = new ArrayList<>();

        for (Map.Entry<String, List<SoDeliveryIface>> successDocEntry : instructionDocMap.entrySet()) {
            List<SoDeliveryIface> docGroupItfData = successDocEntry.getValue();

            try {
                updateDataNew(tenantId, docGroupItfData);
                for (SoDeliveryIface success : docGroupItfData) {
                    SoDeliveryIface soDeliveryIface = new SoDeliveryIface();
                    BeanUtils.copyProperties(success, soDeliveryIface);
                    soDeliveryIface.setStatus(WmsConstant.KEY_IFACE_STATUS_SUCCESS);
                    soDeliveryIface.setMessage(WmsConstant.KEY_IFACE_MESSAGE_SUCCESS);
                    updateIfaces.add(soDeliveryIface);
                }
            } catch (Exception e) {
                // 回写接口表状态
                log.error("写入MES业务数据表出错[updateDataNew]:{}", e.getMessage());

                String msg = StringUtils.isNotBlank(e.getMessage()) && e.getMessage().length() > 3000 ?
                        e.getMessage().substring(e.getMessage().length() - 3000) : e.getMessage();
                // 设置返回消息
                ItfReceiveReturnDTO returnDetailDTO = new ItfReceiveReturnDTO();
                returnDetailDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                returnDetailDTO.setProcessMessage(msg);
                returnDetailDTO.setProcessDate(new Date());
                returnDetailDTO.setInstructionDocNum(docGroupItfData.get(0).getInstructionDocNum());
                returnDetailDTOList.add(returnDetailDTO);

                //更新接口表信息
                //更新接口表信息
                for (SoDeliveryIface success : docGroupItfData) {
                    SoDeliveryIface soDeliveryIface = new SoDeliveryIface();
                    BeanUtils.copyProperties(success, soDeliveryIface);
                    soDeliveryIface.setStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    soDeliveryIface.setMessage(msg);
                    updateIfaces.add(soDeliveryIface);
                }
                //更新接口表信息
                soDeliveryIfaceRepository.batchUpdateByPrimaryKeySelective(updateIfaces);
            }
        }
        //更新接口表信息
        soDeliveryIfaceRepository.batchUpdateByPrimaryKeySelective(updateIfaces);

        if (CollectionUtils.isNotEmpty(returnDetailDTOList)) {
            returnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            returnDTO.setProcessMessage(WmsConstant.KEY_IFACE_MESSAGE_ERROR);
            returnDTO.setProcessDate(new Date());
            returnDTO.setDetail(returnDetailDTOList);
        } else {
            returnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_SUCCESS);
            returnDTO.setProcessMessage(WmsConstant.KEY_IFACE_MESSAGE_SUCCESS);
            returnDTO.setProcessDate(new Date());
        }

        return returnDTO;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SoDeliveryIface> insertIface(Long tenantId, List<ItfSendOutReturnNoticeDTO> itfSendOutReturnNoticeDTOS, Long batchId) {
        List<SoDeliveryIface> soDeliveryIfaceList = new ArrayList<>();

        //获取主键和Cid
        List<String> kids = customDbRepository.getNextKeys("itf_so_delivery_iface_s", itfSendOutReturnNoticeDTOS.size());
        List<String> cids = customDbRepository.getNextKeys("itf_so_delivery_iface_cid_s", itfSendOutReturnNoticeDTOS.size());

        int index = 0;

        for (ItfSendOutReturnNoticeDTO dto : itfSendOutReturnNoticeDTOS) {
            //构造接口表数据
            SoDeliveryIface soDeliveryIface = buildCostcenterDocItf(tenantId, dto);
            soDeliveryIface.setIfaceId(kids.get(index++));
            soDeliveryIface.setBatchId(batchId);
            soDeliveryIface.setStatus(WmsConstant.CONSTANT_N);
            soDeliveryIface.setCid(Long.valueOf(cids.get(index - 1)));
            soDeliveryIfaceList.add(soDeliveryIface);
        }
        //数据校验
        validateHeadItfDate(soDeliveryIfaceList);
        List<String> instructionDocNum = soDeliveryIfaceList.stream()
                .filter(t -> WmsConstant.KEY_IFACE_STATUS_ERROR.equals(t.getStatus()))
                .map(SoDeliveryIface::getInstructionDocNum).distinct().collect(Collectors.toList());
        for (SoDeliveryIface temp : soDeliveryIfaceList) {
            if (temp.getStatus().equals("N") && instructionDocNum.contains(temp.getInstructionDocNum())) {
                temp.setStatus("E");
                temp.setMessage(temp.getMessage());
            }
        }
        //3.批量写入接口表
        if (CollectionUtils.isNotEmpty(soDeliveryIfaceList)) {
            customDbRepository.batchInsertTarzan(soDeliveryIfaceList);
        }
        return soDeliveryIfaceList;
    }

    private void validateHeadItfDate(List<SoDeliveryIface> soDeliveryIfaceList) {
        Long tenantId = soDeliveryIfaceList.get(0).getTenantId();
        List<MtSitePlantReleation> mtSitePlantReleationVO1s = new ArrayList<>();
        List<MtCustomer> mtCustomers = new ArrayList<>();
        List<MtMaterial> mtMaterials = new ArrayList<>();
        List<MtUom> mtUoms = new ArrayList<>();
        List<MtModLocator> mtModLocators = new ArrayList<>();


        //工厂编码SITE_CODE/TO_SITE_CODE 找siteId
        List<String> siteCodeList = soDeliveryIfaceList.stream().map(SoDeliveryIface::getPlantCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        siteCodeList.addAll(soDeliveryIfaceList.stream().map(SoDeliveryIface::getFromSiteCode).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        siteCodeList.addAll(soDeliveryIfaceList.stream().map(SoDeliveryIface::getToSiteCode).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(siteCodeList)) {
            MtSitePlantReleationVO3 mtSitePlantReleationVO = new MtSitePlantReleationVO3();
            mtSitePlantReleationVO.setPlantCodes(siteCodeList);
            mtSitePlantReleationVO.setSiteType("MANUFACTURING");
            mtSitePlantReleationVO1s = mtSitePlantReleationRepository.getRelationByPlantAndSiteType(tenantId, mtSitePlantReleationVO);
        }
        //客户校验
        List<String> customerCode = soDeliveryIfaceList.stream().map(SoDeliveryIface::getCustomerCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(customerCode)) {
            mtCustomers = mtCustomerRepository.queryCustomerByCode(tenantId, customerCode);
        }
        //物料编码
        List<String> materialCodes = soDeliveryIfaceList.stream().map(SoDeliveryIface::getMaterialCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(materialCodes)) {
            mtMaterials = mtMaterialRepository.selectByCondition(Condition.builder(MtMaterial.class)
                    .andWhere(Sqls.custom().andEqualTo(MtMaterial.FIELD_TENANT_ID, tenantId)
                            .andIn(MtMaterial.FIELD_MATERIAL_CODE, materialCodes))
                    .build());
        }
        //单位编码
        List<String> uomCodes = soDeliveryIfaceList.stream().map(SoDeliveryIface::getUomCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(uomCodes)) {
            mtUoms = mtUomRepository.selectByCondition(Condition.builder(MtUom.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(MtUom.FIELD_TENANT_ID, tenantId)
                            .andIn(MtUom.FIELD_UOM_CODE, uomCodes))
                    .build());
        }
        //仓库
        List<String> locatorCodes = soDeliveryIfaceList.stream().map(SoDeliveryIface::getFromLocatorCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        locatorCodes.addAll(soDeliveryIfaceList.stream().map(SoDeliveryIface::getToLocatorCode).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(locatorCodes)) {
            mtModLocators = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                            .andIn(MtModLocator.FIELD_LOCATOR_CODE, locatorCodes)
                    ).build());
        }

        //值集类型--发货单
        List<LovValueDTO> soDelivery = lovAdapter.queryLovValue("WX.WMS_SO_DELIVERY", tenantId);
        //退货单
        List<LovValueDTO> salesReturn = lovAdapter.queryLovValue("WX.WMS_SALES_RETURN", tenantId);
        //直接类型---SAP单据类型
        List<LovValueDTO> soDeliveryTypeLimit = lovAdapter.queryLovValue("WX.WMS_SO_DELIVERY_TYPE_LIMIT", tenantId);

        for (SoDeliveryIface soDeliveryTemp : soDeliveryIfaceList) {
            String msg = StringUtils.EMPTY;
            //单据号不可为空
            if (StringUtils.isBlank(soDeliveryTemp.getInstructionDocNum())) {
                msg += "单据号为空!";
            }
            //单据类型
            if (Strings.isNotBlank(soDeliveryTemp.getSapDocType())) {
                if(soDeliveryTemp.getSapDocType().equals("ZLF3")){
                    if("Z910".equals(soDeliveryTemp.getSapDocLineType())){
                        soDeliveryTemp.setInstructionDocType(WmsConstant.SALES_RETURN);
                    }else if("Z920".equals(soDeliveryTemp.getSapDocLineType())){
                        soDeliveryTemp.setInstructionDocType(WmsConstant.InspectionDocType.SO_DELIVERY);
                    }else{
                        msg+="SAP单据类型为ZLF3时单据行类型不为Z910或Z920！";
                    }
                } else {
                    List<LovValueDTO> soDeliveryMean = soDelivery.stream().filter(t -> soDeliveryTemp.getSapDocType().equals(t.getValue())).collect(Collectors.toList());
                    List<LovValueDTO> salesReturnMean = salesReturn.stream().filter(t -> soDeliveryTemp.getSapDocType().equals(t.getValue())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(soDeliveryMean)) {
                        soDeliveryTemp.setInstructionDocType(WmsConstant.InspectionDocType.SO_DELIVERY);
                    } else if (CollectionUtils.isNotEmpty(salesReturnMean)) {
                        soDeliveryTemp.setInstructionDocType(WmsConstant.SALES_RETURN);
                    } else {
                        msg += "发运单/退库单类型未维护，请检查!";
                    }
                }
            }

            //SAP单据类型
            if (StringUtils.isNotBlank(soDeliveryTemp.getSapDocType())) {
                List<LovValueDTO> soDeliveryTypeLimitMean = soDeliveryTypeLimit.stream().filter(t -> soDeliveryTemp.getSapDocType().equals(t.getValue())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(soDeliveryTypeLimitMean)) {
                    msg += "该单据不在WMS管理范围，请检查!";
                }
            } else {
                msg += "SAP单据类型必输!";
            }
            //日期
            if (StringUtils.isNotBlank(soDeliveryTemp.getDemandTime())) {
                if (parseDate(soDeliveryTemp.getDemandTime()) == null) {
                    msg += "字段WADAT非日期格式，请检查!";
                }
            }
/*                if (parseDate(soDeliveryTemp.getDemandTime()) == null) {
                    msg += "字段WADAT非日期格式，请检查!";
                }*/

            //单据状态
/*            if (StringUtils.isBlank(soDeliveryTemp.getInstructionDocStatus())) {
                soDeliveryTemp.setInstructionDocType(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED);
            }*/
            //工厂编码
            if (StringUtils.isNotBlank(soDeliveryTemp.getPlantCode())) {
                List<MtSitePlantReleation> siteInfo = mtSitePlantReleationVO1s.stream().filter(t -> soDeliveryTemp.getPlantCode().equals(t.getPlantCode())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(siteInfo)) {
                    msg += "WMS无对应工厂!";
                }

            } else {
                msg += "工厂必输!";
            }

            //客户编码
            if (StringUtils.isNotBlank(soDeliveryTemp.getCustomerCode())) {
                List<MtCustomer> mtCustomerList = mtCustomers.stream().filter(t -> soDeliveryTemp.getCustomerCode().equals(t.getCustomerCode())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(mtCustomerList)) {
                    msg += "WMS无对应客户!";
                }
            } else {
                msg += "客户编码必输!";
            }
            //来源系统
            if (StringUtils.isBlank(soDeliveryTemp.getSourceSystem())) {
                soDeliveryTemp.setSourceSystem(WmsConstant.SAP);
            }
            //行号
            if (StringUtils.isBlank(soDeliveryTemp.getInstructionNum())) {
                msg += "指令编号必输!";
            }
            //指令类型
            if (StringUtils.equals(soDeliveryTemp.getInstructionDocType(), WmsConstant.InspectionDocType.SO_DELIVERY)) {
                soDeliveryTemp.setInstructionType(WmsConstant.InstructionType.SHIP_TO_CUSTOMER);
            }
            if (StringUtils.equals(soDeliveryTemp.getInstructionDocType(), WmsConstant.SALES_RETURN)) {
                soDeliveryTemp.setInstructionType(WmsConstant.RETURN_FROM_CUSTOMER);
            }
            //指令状态
            //查询数据是否存在
            MtInstructionDocVO4 mtInstructionDocVO4 = new MtInstructionDocVO4();
            mtInstructionDocVO4.setTenantId(tenantId);
            mtInstructionDocVO4.setInstructionDocNum(soDeliveryTemp.getInstructionDocNum());
            List<String> instructionDocIds = mtInstructionDocRepository.propertyLimitInstructionDocQuery(tenantId, mtInstructionDocVO4);
            if (CollectionUtils.isEmpty(instructionDocIds)) {
                soDeliveryTemp.setInstructionDocStatus(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED);
            }else {
                MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(instructionDocIds.get(0));
                if(!mtInstructionDoc.getInstructionDocStatus().equals(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED)){
                    msg += "单据状态不为下达!";
                }
                soDeliveryTemp.setInstructionDocStatus(mtInstructionDoc.getInstructionDocStatus());
            }
          /*  if (StringUtils.isBlank(soDeliveryTemp.getInstructionStatus())) {
                soDeliveryTemp.setInstructionStatus(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED);
            }*/
            //物料编码
            if (Strings.isNotBlank(soDeliveryTemp.getMaterialCode())) {
                List<MtMaterial> materials = mtMaterials.stream().filter(t -> soDeliveryTemp.getMaterialCode().equals(t.getMaterialCode())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(materials)) {
                    msg += "物料编码在WMS不存在!";
                } else {
                    List<MtModSite> siteList = mtModSiteRepository.selectByCondition(Condition.builder(MtModSite.class)
                            .andWhere(Sqls.custom()
                                    .andEqualTo(MtModSite.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(MtModSite.FIELD_SITE_CODE, soDeliveryTemp.getFromSiteCode()))
                            .build());
                    if (StringUtils.isNotBlank(soDeliveryTemp.getFromSiteCode()) && StringUtils.isNotBlank(soDeliveryTemp.getMaterialCode()) && CollectionUtils.isNotEmpty(siteList)) {
                        List<MtMaterialSite> list = mtMaterialSiteRepository.selectByCondition(Condition.builder(MtMaterialSite.class)
                                .andWhere(Sqls.custom()
                                        .andEqualTo(MtMaterialSite.FIELD_MATERIAL_ID, mtMaterials.get(0).getMaterialId())
                                        .andEqualTo(MtMaterialSite.FIELD_ENABLE_FLAG, WmsConstant.CONSTANT_Y)
                                        .andEqualTo(MtMaterialSite.FIELD_SITE_ID, siteList.get(0).getSiteId())
                                )
                                .build());
                        if (CollectionUtils.isEmpty(list)) {
                            msg += "物料未分配至当前工厂!";
                        }
                    }
                }
            } else {
                msg += "物料编码必输!";
            }
            //需求数量
            if (Strings.isBlank(String.valueOf(soDeliveryTemp.getQuantity())) || soDeliveryTemp.getQuantity() == null) {
                msg += "需求数量为空!";
            } else {
                if (!NumberUtils.isNumber(String.valueOf(soDeliveryTemp.getQuantity()))) {
                    msg += "需求数量非数字格式，且必须为正数，请检查!";
                }
            }

            //单位编码
            if (Strings.isBlank(soDeliveryTemp.getUomCode())) {
                msg += "单位为空!";
            } else {
                List<MtUom> mtUomInfo = mtUoms.stream().filter(t -> soDeliveryTemp.getUomCode().equals(t.getUomCode())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(mtUomInfo)) {
                    msg += "单位在WMS不存在!";
                }
            }

            //发货工厂
            if (StringUtils.isNotBlank(soDeliveryTemp.getFromSiteCode())) {
                if (StringUtils.equals(soDeliveryTemp.getInstructionDocType(), WmsConstant.InspectionDocType.SO_DELIVERY)) {
                    List<MtSitePlantReleation> siteInfo = mtSitePlantReleationVO1s.stream().filter(t -> soDeliveryTemp.getPlantCode().equals(t.getPlantCode())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(siteInfo)) {
                        msg += "工厂为空!";
                    }
                }
            } else {
                msg += "发货工厂为空";
            }

            //收货工厂
            if (StringUtils.isNotBlank(soDeliveryTemp.getToSiteCode())) {
                if (StringUtils.equals(soDeliveryTemp.getInstructionDocType(), WmsConstant.InspectionDocType.SO_DELIVERY)) {
                    List<MtSitePlantReleation> siteInfo = mtSitePlantReleationVO1s.stream().filter(t -> soDeliveryTemp.getToSiteCode().equals(t.getPlantCode())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(siteInfo)) {
                        msg += "收货工厂为空!";
                    }
                }
            } else {
                msg += "收货工厂为空!";
            }

            //发货仓库
            if (StringUtils.equals(soDeliveryTemp.getInstructionDocType(), WmsConstant.InspectionDocType.SO_DELIVERY)) {
                if (StringUtils.isBlank(soDeliveryTemp.getFromLocatorCode())) {
                    soDeliveryTemp.setFromLocatorCode("1010");
                    List<LovValueDTO> lovValueDTOs = lovAdapter.queryLovValue("WX.WMS.SO_DOC_TYPE_WAREHOUSE_REL", tenantId);
                    if(CollectionUtils.isNotEmpty(lovValueDTOs)){
                        List<LovValueDTO> lovValueDTOList = lovValueDTOs.stream().filter(item ->item.getValue().equals(soDeliveryTemp.getSapDocLineType())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(lovValueDTOList)){
                            soDeliveryTemp.setFromLocatorCode(lovValueDTOList.get(0).getMeaning());
                        }
                    }
                } else {
                    List<MtModLocator> locators = mtModLocators.stream().filter(t -> soDeliveryTemp.getFromLocatorCode().equals(t.getLocatorCode())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(locators)) {
                        msg += "来源仓库在wms系统不存在!";
                    } else {
                        MtModLocatorOrgRelVO2 rel = new MtModLocatorOrgRelVO2();
                        rel.setLocatorId(locators.get(0).getLocatorId());
                        rel.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
                        List<MtModLocatorOrgRelVO3> existSiteList =
                                mtModLocatorOrgRelRepository.locatorLimitOrganizationQuery(tenantId, rel);
                        if (CollectionUtils.isNotEmpty(existSiteList)) {
                            List<MtSitePlantReleation> siteInfo = mtSitePlantReleationVO1s.stream().filter(t -> existSiteList.get(0).getOrganizationId().equals(t.getSiteId())).collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(siteInfo)) {
                                msg += "仓库不属于对应单据工厂下!";
                            }
                        }
                    }
                }
            }
            //收货仓库
            if (StringUtils.equals(soDeliveryTemp.getInstructionDocType(), WmsConstant.SALES_RETURN)) {
                if (StringUtils.isBlank(soDeliveryTemp.getToLocatorCode())) {
                    soDeliveryTemp.setToLocatorCode("2120");
                    List<LovValueDTO> lovValueDTOs = lovAdapter.queryLovValue("WX.WMS.SO_DOC_TYPE_WAREHOUSE_REL", tenantId);
                    if(CollectionUtils.isNotEmpty(lovValueDTOs)){
                        List<LovValueDTO> lovValueDTOList = lovValueDTOs.stream().filter(item ->item.getValue().equals(soDeliveryTemp.getSapDocLineType())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(lovValueDTOList)){
                            soDeliveryTemp.setToLocatorCode(lovValueDTOList.get(0).getMeaning());
                        }
                    }
                }else{
                    List<MtModLocator> locators = mtModLocators.stream().filter(t -> soDeliveryTemp.getToLocatorCode().equals(t.getLocatorCode())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(locators)) {
                        msg += "目标仓库在wms系统不存在!";
                    } else {
                        MtModLocatorOrgRelVO2 rel = new MtModLocatorOrgRelVO2();
                        rel.setLocatorId(locators.get(0).getLocatorId());
                        rel.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
                        List<MtModLocatorOrgRelVO3> existSiteList =
                                mtModLocatorOrgRelRepository.locatorLimitOrganizationQuery(tenantId, rel);
                        if (CollectionUtils.isNotEmpty(existSiteList)) {
                            List<MtSitePlantReleation> siteInfo = mtSitePlantReleationVO1s.stream().filter(t -> existSiteList.get(0).getOrganizationId().equals(t.getSiteId())).collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(siteInfo)) {
                                msg += "仓库不属于对应单据工厂下!";
                            }
                        }
                    }
                }
            }
            if (Strings.isNotBlank(msg)) {
                soDeliveryTemp.setStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                soDeliveryTemp.setMessage(msg);
            }
        }
    }

    private SoDeliveryIface buildCostcenterDocItf(Long tenantId, ItfSendOutReturnNoticeDTO dto) {
        SoDeliveryIface soDeliveryIface = new SoDeliveryIface();
        soDeliveryIface.setInstructionDocNum(dto.getVbeln().replaceAll("^(0+)", ""));
        soDeliveryIface.setSapDocType(dto.getLfart());
        soDeliveryIface.setSapDocTypeDes(StringUtils.isNotBlank(dto.getVtext()) ? dto.getVtext() : "");
        soDeliveryIface.setPlantCode(dto.getWerks());
        soDeliveryIface.setFromSiteCode(dto.getWerks());
        soDeliveryIface.setToSiteCode(dto.getWerks());
        soDeliveryIface.setCustomerCode(dto.getKunnr().replaceAll("^(0+)", ""));
        soDeliveryIface.setInstructionLineNum(dto.getPosnr());
        soDeliveryIface.setDemandTime(StringUtils.isNotBlank(dto.getWadat()) ? dto.getWadat() : "");
        soDeliveryIface.setSoOrganizationCode(StringUtils.isNotBlank(dto.getVkorg()) ? dto.getVkorg() : "");
        //soDeliveryIface.setInstructionNum(dto.getVbeln_posnr());
        soDeliveryIface.setInstructionNum(dto.getVbeln() + "-" + dto.getPosnr());
        //soDeliveryIface.setInstructionStatus(dto.getKosta());
        soDeliveryIface.setMaterialCode(dto.getMatnr().replaceAll("^(0+)", ""));
        soDeliveryIface.setLotFlag(StringUtils.isNotBlank(dto.getXchpf()) ? dto.getXchpf() : "");
        soDeliveryIface.setMaterialVersion(dto.getAeskd());
        if (StringUtils.isNotBlank(dto.getLgmng())) {
            if (!NumberUtils.isNumber(String.valueOf(dto.getLgmng()))) {
                soDeliveryIface.setMessage("字段[QUANTITY]非数字格式，请检查！");
            } else {
                soDeliveryIface.setQuantity(new BigDecimal(dto.getLgmng()));
            }
        }
        soDeliveryIface.setUomCode(dto.getMeins());
        soDeliveryIface.setFromLocatorCode(StringUtils.isNotBlank(dto.getLgort()) ? dto.getLgort() : "");
        soDeliveryIface.setToLocatorCode(StringUtils.isNotBlank(dto.getLgort()) ? dto.getLgort() : "");
        soDeliveryIface.setRemarkL(StringUtils.isNotBlank(dto.getZtext()) ? dto.getZtext() : "");
        soDeliveryIface.setRemarkSo(StringUtils.isNotBlank(dto.getZtext1()) ? dto.getZtext1() : "");
        soDeliveryIface.setSourceOrderNum(StringUtils.isNotBlank(dto.getKdauf()) ? dto.getKdauf() : "");
        soDeliveryIface.setSourceOrderLineNum(StringUtils.isNotBlank(dto.getKdpos()) ? dto.getKdpos() : "");
        soDeliveryIface.setSpecStockFlag(StringUtils.isNotBlank(dto.getSobkz()) ? dto.getSobkz() : "");
        soDeliveryIface.setSapDocLineType(dto.getPstyv());
        soDeliveryIface.setSapDocLineTypeDes(dto.getVtext1());
        if(StringUtils.isNotBlank(dto.getKdmat())){
            if("2000".equals(dto.getWerks())){
                String sn = soDeliveryIface.getMaterialCode()+" "+dto.getKdmat();
                soDeliveryIface.setSn(sn);
            }else{
                soDeliveryIface.setSn(dto.getKdmat());
            }
        }else{
            String sn = dto.getKdmat();
            soDeliveryIface.setSn(sn);
        }
        soDeliveryIface.setTenantId(tenantId);
        return soDeliveryIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDataNew(Long tenantId, List<SoDeliveryIface> soDeliveryIfaceList) {
        //查询相关数据
        List<MtSitePlantReleation> mtSitePlantReleationVO1s = new ArrayList<>();
        List<MtMaterial> mtMaterials = new ArrayList<>();
        List<MtUom> mtUoms = new ArrayList<>();
        List<MtModLocator> mtModLocators = new ArrayList<>();
        List<MtCustomer> mtCustomers = new ArrayList<>();

        List<String> materialCodes = soDeliveryIfaceList.stream().map(SoDeliveryIface::getMaterialCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(materialCodes)) {
            mtMaterials = mtMaterialRepository.queryMaterialByCode(tenantId, materialCodes);
        }
        List<String> uomCodes = soDeliveryIfaceList.stream().map(SoDeliveryIface::getUomCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(uomCodes)) {
            mtUoms = mtUomRepository.selectByCondition(Condition.builder(MtUom.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(MtUom.FIELD_TENANT_ID, tenantId)
                            .andIn(MtUom.FIELD_UOM_CODE, uomCodes))
                    .build());
        }
        List<String> locatorCodes = soDeliveryIfaceList.stream().map(SoDeliveryIface::getFromLocatorCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        locatorCodes.addAll(soDeliveryIfaceList.stream().map(SoDeliveryIface::getToLocatorCode).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(locatorCodes)) {
            mtModLocators = mtModLocatorRepository.selectModLocatorForCodes(tenantId, locatorCodes);
        }
        List<String> siteCodeList = soDeliveryIfaceList.stream().map(SoDeliveryIface::getPlantCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        siteCodeList.addAll(soDeliveryIfaceList.stream().map(SoDeliveryIface::getFromSiteCode).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        siteCodeList.addAll(soDeliveryIfaceList.stream().map(SoDeliveryIface::getToSiteCode).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(siteCodeList)) {
            MtSitePlantReleationVO3 mtSitePlantReleationVO = new MtSitePlantReleationVO3();
            mtSitePlantReleationVO.setPlantCodes(siteCodeList);
            mtSitePlantReleationVO.setSiteType("MANUFACTURING");
            mtSitePlantReleationVO1s = mtSitePlantReleationRepository.getRelationByPlantAndSiteType(tenantId, mtSitePlantReleationVO);
        }

        List<String> customerCode = soDeliveryIfaceList.stream().map(SoDeliveryIface::getCustomerCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(customerCode)) {
            mtCustomers = mtCustomerRepository.queryCustomerByCode(tenantId, customerCode);
        }

        SoDeliveryIface headerDto = soDeliveryIfaceList.get(0);
        //头表数据
        MtInstructionDocDTO2 mtInstructionDoc = new MtInstructionDocDTO2();

        String eventTypeCode = StringUtils.EMPTY;

        //查询数据是否存在
        MtInstructionDocVO4 mtInstructionDocVO4 = new MtInstructionDocVO4();
        mtInstructionDocVO4.setInstructionDocNum(headerDto.getInstructionDocNum());
        List<String> instructionDocIds = mtInstructionDocRepository.propertyLimitInstructionDocQuery(tenantId, mtInstructionDocVO4);
        if (CollectionUtils.isNotEmpty(instructionDocIds)) {
            mtInstructionDoc.setInstructionDocId(instructionDocIds.get(0));
        }
        mtInstructionDoc.setInstructionDocNum(headerDto.getInstructionDocNum());
        mtInstructionDoc.setInstructionDocType(headerDto.getInstructionDocType());
        List<MtSitePlantReleation> siteInfos = mtSitePlantReleationVO1s.stream().filter(t -> t.getPlantCode().equals(headerDto.getPlantCode())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(siteInfos)) {
            mtInstructionDoc.setSiteId(siteInfos.get(0).getSiteId());
        }
        List<MtCustomer> customers = mtCustomers.stream().filter(t -> t.getCustomerCode().equals(headerDto.getCustomerCode())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(customers)) {
            mtInstructionDoc.setCustomerId(customers.get(0).getCustomerId());
            mtInstructionDoc.setCustomerSiteId("-1");
        }
        if (StringUtils.isNotBlank(headerDto.getDemandTime())) {
            try {
                mtInstructionDoc.setDemandTime(DateUtils.parseDate(headerDto.getDemandTime(), BaseConstants.Pattern.DATE));
            } catch (Exception e) {
                throw new MtException("DemandTimeH日期格式化错误!");
            }
        }
        if (MtIdHelper.isIdNotNull(mtInstructionDoc.getInstructionDocId())) {
            eventTypeCode = WmsConstant.EventType.SO_DELIVERY_UPDATE;
        } else {
            eventTypeCode = WmsConstant.EventType.SO_DELIVERY_CREATE;
            mtInstructionDoc.setInstructionDocStatus(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED);
        }

        // 1.生成事件
        MtEventCreateVO event = new MtEventCreateVO();
        event.setEventTypeCode(eventTypeCode);
        String eventId = mtEventRepository.eventCreate(tenantId, event);

        if (MtIdHelper.isIdNotNull(mtInstructionDoc.getInstructionDocId())) {
            mtInstructionDoc.setEventId(eventId);
        }

        // 更新单据头表
        MtInstructionDocVO3 mtInstructionDocVO3 = mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDoc, WmsConstant.CONSTANT_N);

        //更新单据头扩展表
        //更新头扩展表
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 mtCommonExtendVO5 = new MtExtendVO5();
        mtCommonExtendVO5.setAttrName(WmsConstant.SAP_DOC_TYPE);
        mtCommonExtendVO5.setAttrValue(headerDto.getSapDocType());
        attrs.add(mtCommonExtendVO5);

        MtExtendVO5 mtCommonExtendVO51 = new MtExtendVO5();
        mtCommonExtendVO51.setAttrName(WmsConstant.SAP_DOC_TYPE_DES);
        mtCommonExtendVO51.setAttrValue(headerDto.getSapDocTypeDes());
        attrs.add(mtCommonExtendVO51);

        MtExtendVO5 mtCommonExtendVO52 = new MtExtendVO5();
        mtCommonExtendVO52.setAttrName(WmsConstant.SO_ORGANIZATION_CODE);
        mtCommonExtendVO52.setAttrValue(headerDto.getSoOrganizationCode());
        attrs.add(mtCommonExtendVO52);

        //新增或更新单据头表
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr", mtInstructionDocVO3.getInstructionDocId(), eventId, attrs);
        //写入单据行表
        for (SoDeliveryIface temp : soDeliveryIfaceList) {
            try {
                MtInstructionVO lineBuilder = new MtInstructionVO();

                List<ItfSendOutReturnDTO2> itfCostcenterDocIfaceDTO1s = mtInstructionMapper.selectByDocCodeForJudge(tenantId,
                        temp.getInstructionDocNum(), temp.getInstructionLineNum());

                if (CollectionUtils.isNotEmpty(itfCostcenterDocIfaceDTO1s)) {
                    if (itfCostcenterDocIfaceDTO1s.get(0).getInstructionId() != null) {
                        lineBuilder.setInstructionId(String.valueOf(itfCostcenterDocIfaceDTO1s.get(0).getInstructionId()));
                    }
                }
/*                List<MtInstruction> instructionList = mtInstructionRepository.selectByCondition(Condition.builder(MtInstruction.class)
                        .andWhere(Sqls.custom()
                                .andEqualTo(MtInstruction.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtInstruction.FIELD_INSTRUCTION_NUM, headerDto.getInstructionNum()))
                        .build());

                if (CollectionUtils.isNotEmpty(instructionList)) {
                    if (instructionList.get(0).getInstructionId() != null) {
                        lineBuilder.setInstructionId(instructionList.get(0).getInstructionId());
                        //lineBuilder.setInstructionStatus(instructionList.get(0).getInstructionStatus());
                    }
                }*/
                lineBuilder.setSourceDocId(mtInstructionDocVO3.getInstructionDocId());
                lineBuilder.setInstructionNum(headerDto.getInstructionDocNum() + "-" + temp.getInstructionLineNum());
                lineBuilder.setSiteId(mtInstructionDoc.getSiteId());
                if (StringUtils.isNotBlank(temp.getMaterialCode())) {
                    List<MtMaterial> materials = mtMaterials.stream().filter(t -> temp.getMaterialCode().equals(t.getMaterialCode())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(materials)) {
                        lineBuilder.setMaterialId(materials.get(0).getMaterialId());
                    }
                }
                List<MtUom> mtUomInfo = mtUoms.stream().filter(t -> temp.getUomCode().equals(t.getUomCode())).collect(Collectors.toList());
                lineBuilder.setUomId(mtUomInfo.get(0).getUomId());
                if (StringUtils.equals(headerDto.getInstructionDocType(), WmsConstant.InspectionDocType.SO_DELIVERY)) {
                    if (Strings.isNotBlank(temp.getFromSiteCode())) {
                        List<MtSitePlantReleation> fromSiteInfo = mtSitePlantReleationVO1s.stream().filter(t -> temp.getFromSiteCode().equals(t.getPlantCode())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(fromSiteInfo)) {
                            lineBuilder.setFromSiteId(fromSiteInfo.get(0).getSiteId());
                        }
                    }
                }
                if (StringUtils.equals(headerDto.getInstructionDocType(), WmsConstant.SALES_RETURN)) {
                    if (Strings.isNotBlank(temp.getToSiteCode())) {
                        List<MtSitePlantReleation> toSiteInfo = mtSitePlantReleationVO1s.stream().filter(t -> temp.getToSiteCode().equals(t.getPlantCode())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(toSiteInfo)) {
                            lineBuilder.setToSiteId(toSiteInfo.get(0).getSiteId());
                        }
                    }
                }
                if (MtIdHelper.isIdNull(lineBuilder.getInstructionId())) {
                    if (StringUtils.equals(headerDto.getInstructionDocType(), WmsConstant.InspectionDocType.SO_DELIVERY)) {
                        if (Strings.isNotBlank(temp.getFromLocatorCode())) {
                            List<MtModLocator> locators = mtModLocators.stream().filter(t -> temp.getFromLocatorCode().equals(t.getLocatorCode())).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(locators)) {
                                lineBuilder.setFromLocatorId(locators.get(0).getLocatorId());
                            }
                        }
                    }
                    if (StringUtils.equals(headerDto.getInstructionDocType(), WmsConstant.SALES_RETURN)) {
                        if (Strings.isNotBlank(temp.getToLocatorCode())) {
                            List<MtModLocator> locators = mtModLocators.stream().filter(t -> temp.getToLocatorCode().equals(t.getLocatorCode())).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(locators)) {
                                lineBuilder.setToLocatorId(locators.get(0).getLocatorId());
                            }
                        }
                    }
                    lineBuilder.setQuantity(temp.getQuantity().doubleValue());
                    lineBuilder.setInstructionStatus(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED);
                    lineBuilder.setInstructionType(temp.getInstructionType());
                }
                lineBuilder.setRemark(temp.getRemarkL());
                if (MtIdHelper.isIdNotNull(lineBuilder.getInstructionId())) {
                    lineBuilder.setEventId(eventId);
                }
                if (CollectionUtils.isNotEmpty(customers)) {
                    lineBuilder.setCustomerId(customers.get(0).getCustomerId());
                    lineBuilder.setCustomerSiteId("-1");
                }

                MtInstructionVO6 mtInstructionVO6 = mtInstructionRepository.instructionUpdate(tenantId, lineBuilder, WmsConstant.KEY_IFACE_STATUS_NEW);

                //新增行扩展表
                List<MtExtendVO5> attrList = new ArrayList<>();
                MtExtendVO5 mtCommonExtendVO1 = new MtExtendVO5();
                mtCommonExtendVO1.setAttrName(WmsConstant.LOT_FLAG);
                mtCommonExtendVO1.setAttrValue(temp.getLotFlag());
                attrList.add(mtCommonExtendVO1);

                MtExtendVO5 mtCommonExtendVO2 = new MtExtendVO5();
                mtCommonExtendVO2.setAttrName(WmsConstant.MATERIAL_VERSION);
                mtCommonExtendVO2.setAttrValue(temp.getMaterialVersion());
                attrList.add(mtCommonExtendVO2);

                MtExtendVO5 mtCommonExtendVO3 = new MtExtendVO5();
                mtCommonExtendVO3.setAttrName(WmsConstant.MaterialLotAttr.SO_NUM);
                mtCommonExtendVO3.setAttrValue(temp.getSourceOrderNum());
                attrList.add(mtCommonExtendVO3);

                MtExtendVO5 mtCommonExtendVO4 = new MtExtendVO5();
                mtCommonExtendVO4.setAttrName(WmsConstant.MaterialLotAttr.SO_LINE_NUM);
                mtCommonExtendVO4.setAttrValue(temp.getSourceOrderLineNum());
                attrList.add(mtCommonExtendVO4);

                MtExtendVO5 mtCommonExtendVO6 = new MtExtendVO5();
                mtCommonExtendVO6.setAttrName(WmsConstant.INSTRUCTION_LINE_NUM);
                mtCommonExtendVO6.setAttrValue(temp.getInstructionLineNum());
                attrList.add(mtCommonExtendVO6);

                MtExtendVO5 mtCommonExtendVO7 = new MtExtendVO5();
                mtCommonExtendVO7.setAttrName(WmsConstant.SPEC_STOCK_FLAG);
                mtCommonExtendVO7.setAttrValue(temp.getSpecStockFlag());
                attrList.add(mtCommonExtendVO7);

                MtExtendVO5 mtCommonExtendVO8 = new MtExtendVO5();
                mtCommonExtendVO8.setAttrName(WmsConstant.REMARK_SO);
                mtCommonExtendVO8.setAttrValue(temp.getRemarkSo());
                attrList.add(mtCommonExtendVO8);

                MtExtendVO5 mtCommonExtendVO9 = new MtExtendVO5();
                mtCommonExtendVO9.setAttrName(WmsConstant.SAP_DOC_LINE_TYPE);
                mtCommonExtendVO9.setAttrValue(temp.getSapDocLineType());
                attrList.add(mtCommonExtendVO9);

                MtExtendVO5 mtCommonExtendVO10 = new MtExtendVO5();
                mtCommonExtendVO10.setAttrName(WmsConstant.SAP_DOC_LINE_TYPE_DES);
                mtCommonExtendVO10.setAttrValue(temp.getSapDocLineTypeDes());
                attrList.add(mtCommonExtendVO10);

                MtExtendVO5 mtCommonExtendVO11 = new MtExtendVO5();
                mtCommonExtendVO11.setAttrName(WmsConstant.SN);
                mtCommonExtendVO11.setAttrValue(temp.getSn());
                attrList.add(mtCommonExtendVO11);

                mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", mtInstructionVO6.getInstructionId(), eventId, attrList);

                if (CollectionUtils.isEmpty(itfCostcenterDocIfaceDTO1s)) {
                    headerDto.setInstructionStatus(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED);
                } else {
                    headerDto.setInstructionStatus(itfCostcenterDocIfaceDTO1s.get(0).getInstructionStatus());
                }

            } catch (Exception e) {
                throw new MtException("行号: " + temp.getInstructionNum() + e.getMessage());
            }
        }
/*        if (CollectionUtils.isEmpty(instructionDocIds)) {
            headerDto.setInstructionDocStatus(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED);
        } else {
            MtInstructionDoc mtInstructionDoc1 = new MtInstructionDoc();
            mtInstructionDoc1.setTenantId(tenantId);
            mtInstructionDoc1.setInstructionDocNum(headerDto.getInstructionDocNum());
            MtInstructionDoc mtInstructionDoc2 = mtInstructionDocRepository.selectOne(mtInstructionDoc1);
            headerDto.setInstructionDocStatus(mtInstructionDoc2.getInstructionDocStatus());
        }*/
        headerDto.setStatus(WmsConstant.KEY_IFACE_STATUS_SUCCESS);
        headerDto.setMessage(WmsConstant.KEY_IFACE_MESSAGE_SUCCESS);

    }

    public static Date parseDate(String string) {
        // 默认传入非空字符串
        try {
            return DateUtils.parseDate(string, parsePatterns);
        } catch (Exception e) {
            return null;
        }
    }
}
