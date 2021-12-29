package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfCommonReturnDTO;
import com.ruike.itf.api.dto.ItfSapSnIfaceDto;
import com.ruike.itf.api.dto.ItfSapSnReceiveReturnDto;
import com.ruike.itf.api.dto.ItfSapSnReturnDto;
import com.ruike.itf.app.service.ItfSapSnIfaceService;
import com.ruike.itf.domain.entity.ItfSapSnIface;
import com.ruike.itf.domain.repository.ItfSapSnIfaceRepository;
import com.ruike.itf.infra.mapper.ItfSapSnIfaceMapper;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtCommonExtendVO5;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hpsf.Decimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.infra.mapper.MtMaterialMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO2;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;

/**
 * 成品SN同步接口表应用服务默认实现
 *
 * @author li.zhang13@hand-china.com 2021-07-01 11:05:34
 */
@Service
public class ItfSapSnIfaceServiceImpl implements ItfSapSnIfaceService {

    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private ItfSapSnIfaceRepository itfSapSnIfaceRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtMaterialMapper mtMaterialMapper;
    @Autowired
    private ItfSapSnIfaceMapper itfSapSnIfaceMapper;

    @Override
    public ItfSapSnReturnDto invoke(List<ItfSapSnIfaceDto> itfSapSnIfaceDtoList) {
        //返回值
        ItfSapSnReturnDto itfSapSnReturnDto = new ItfSapSnReturnDto();
        List<ItfSapSnReceiveReturnDto> returnDetailDTOList = new ArrayList<>();
        //校验是否有数据
        if (CollectionUtils.isEmpty(itfSapSnIfaceDtoList)) {
            itfSapSnReturnDto.setStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            itfSapSnReturnDto.setMessage("传输数据为空");
            return itfSapSnReturnDto;
        }
        Long tenantId = 0L;
        Long batchId = Long.valueOf((new SimpleDateFormat("yyyyMMddhhmmss")).format(new Date()));
        List<ItfSapSnIface> itfSapSnIfaceList1 = new ArrayList<>();
        List<ItfSapSnIface> itfSapSnIfaceList = new ArrayList<>();
        //对数据进行校验
        for(ItfSapSnIfaceDto itfSapSnIfaceDto: itfSapSnIfaceDtoList){
            ItfSapSnIface itfSapSnIface = new ItfSapSnIface();
            String status = "N";
            String msg = "";
            //校验工厂
            if(StringUtils.isBlank(itfSapSnIfaceDto.getSiteCode())){
                itfSapSnIfaceDto.setSiteCode("2000");
            }
            MtSitePlantReleation mtSitePlantReleation = new MtSitePlantReleation();
            mtSitePlantReleation.setTenantId(tenantId);
            mtSitePlantReleation.setPlantCode(itfSapSnIfaceDto.getSiteCode());
            mtSitePlantReleation.setSiteType(MtBaseConstants.ORGANIZATION_REL_TYPE.MANUFACTURING);
            List<MtSitePlantReleation> mtSitePlantReleations = mtSitePlantReleationRepository.select(mtSitePlantReleation);
            if(mtSitePlantReleations.size()==0){
                status = "E";
                msg = "WMS无对应工厂！";
            }else{
                MtModSite mtModSite = new MtModSite();
                mtModSite.setTenantId(tenantId);
                mtModSite.setSiteId(mtSitePlantReleations.get(0).getSiteId());
                itfSapSnIface.setSiteCode(mtModSiteRepository.select(mtModSite).get(0).getSiteCode());
            }
            //校验SN号
            if(StringUtils.isBlank(itfSapSnIfaceDto.getSn())){
                status = "E";
                msg = msg + "SERNR字段必传！";
            }else{
                MtMaterialLot mtMaterialLot = new MtMaterialLot();
                mtMaterialLot.setTenantId(tenantId);
                if(StringUtils.isNotBlank(itfSapSnIfaceDto.getMaterialCode())){
                    String materialCode1 = itfSapSnIfaceDto.getMaterialCode().replaceAll("^(0+)", "");
                    String materialLotCode = materialCode1+" "+itfSapSnIfaceDto.getSn();
                    mtMaterialLot.setMaterialLotCode(materialLotCode);
                }else{
                    String materialLotCode = itfSapSnIfaceDto.getSn();
                    mtMaterialLot.setMaterialLotCode(materialLotCode);
                }
                List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.select(mtMaterialLot);
                if(mtMaterialLots.size()!=0 && mtMaterialLots.get(0).getEnableFlag().equals("Y")){
                    status = "E";
                    msg = msg + "SN有效，不可更新！";
                }
            }
            //校验物料
            if(StringUtils.isBlank(itfSapSnIfaceDto.getMaterialCode())){
                status = "E";
                msg = msg + "MATNR字段必传！";
            }else{
                String materialCode = itfSapSnIfaceDto.getMaterialCode().replaceAll("^(0+)", "");
                itfSapSnIfaceDto.setMaterialCode(materialCode);
                MtMaterial mtMaterial = new MtMaterial();
                mtMaterial.setTenantId(tenantId);
                mtMaterial.setMaterialCode(itfSapSnIfaceDto.getMaterialCode());
                List<MtMaterial> mtMaterials = mtMaterialRepository.select(mtMaterial);
                if(mtMaterials.size()==0){
                    status = "E";
                    msg = msg + "物料编码在WMS系统不存在！";
                }else if(mtSitePlantReleations.size()!=0){
                    MtMaterialSite mtMaterialSite = new MtMaterialSite();
                    mtMaterialSite.setTenantId(tenantId);
                    mtMaterialSite.setSiteId(mtSitePlantReleations.get(0).getSiteId());
                    mtMaterialSite.setMaterialId(mtMaterials.get(0).getMaterialId());
                    List<MtMaterialSite> mtMaterialSites = mtMaterialSiteRepository.select(mtMaterialSite);
                    if(mtMaterialSites.size()==0){
                        status = "E";
                        msg = msg + "物料未分配至当前工厂！";
                    }
                }else{
                    status = "E";
                    msg = msg + "物料未分配至当前工厂！";
                }
            }
            //校验单位
            if(StringUtils.isBlank(itfSapSnIfaceDto.getUomCode())){
                status = "E";
                msg = msg + "MEINS字段必传！";
            }else{
                MtUom mtUom = new MtUom();
                mtUom.setTenantId(tenantId);
                mtUom.setUomCode(itfSapSnIfaceDto.getUomCode());
                List<MtUom> mtUoms = mtUomRepository.select(mtUom);
                if(mtUoms.size()==0){
                    status = "E";
                    msg = msg + "单位在WMS系统不存在！";
                }
            }
            //校验库存类型、仓库、货位
            if(StringUtils.isBlank(itfSapSnIfaceDto.getInventoryType()) || itfSapSnIfaceDto.getInventoryType().equals("01")){
                if(StringUtils.isBlank(itfSapSnIfaceDto.getInventoryType())){
                    itfSapSnIface.setMaterialLotStatus("NEW");
                    itfSapSnIface.setQualityStatus("PENDING");
                    itfSapSnIface.setEnableFlag("N");
                    itfSapSnIface.setWarehouseCode("-1");
                    itfSapSnIface.setLocatorCode("-1");
                }else{
                    itfSapSnIface.setMaterialLotStatus("INSTOCK");
                    itfSapSnIface.setQualityStatus("OK");
                    itfSapSnIface.setEnableFlag("Y");
                    MtModLocator mtModLocator = new MtModLocator();
                    mtModLocator.setTenantId(tenantId);
                    mtModLocator.setLocatorCode(itfSapSnIfaceDto.getWarehouseCode());
                    List<MtModLocator> mtModLocators = mtModLocatorRepository.select(mtModLocator);
                    if(mtModLocators.size()==0){
                        status = "E";
                        msg = msg + "仓库在wms系统不存在！";
                    }else{
                        MtModLocatorOrgRelVO2 mtModLocatorOrgRelVO2 = new MtModLocatorOrgRelVO2();
                        mtModLocatorOrgRelVO2.setLocatorId(mtModLocators.get(0).getLocatorId());
                        mtModLocatorOrgRelVO2.setOrganizationType("SITE");
                        List<MtModLocatorOrgRelVO3> siteIdList =
                                mtModLocatorOrgRelRepository.locatorLimitOrganizationQuery(tenantId, mtModLocatorOrgRelVO2);
                        if(siteIdList.size()==0){
                            status = "E";
                            msg = msg + "仓库不属于对应工厂下！";
                        }
                        MtModLocator mtModLocator1 = new MtModLocator();
                        mtModLocator1.setTenantId(tenantId);
                        mtModLocator1.setParentLocatorId(mtModLocators.get(0).getLocatorId());
                        mtModLocator1.setLocatorType("PROD_COMP");
                        List<MtModLocator> mtModLocators1 = mtModLocatorRepository.select(mtModLocator1);
                        if(mtModLocators1.size()!= 1){
                            status = "E";
                            msg = msg + "完工货位维护异常！";
                        }else{
                            itfSapSnIface.setLocatorCode(mtModLocators1.get(0).getLocatorCode());
                        }
                    }
                    itfSapSnIface.setWarehouseCode(itfSapSnIfaceDto.getWarehouseCode());
                }
            }else{
                status = "E";
                msg = msg + "库存类型错误！";
            }
            itfSapSnIface.setSiteCode(itfSapSnIfaceDto.getSiteCode());
            itfSapSnIface.setTenantId(tenantId);
            String sn = itfSapSnIfaceDto.getMaterialCode() + " "+ itfSapSnIfaceDto.getSn();
            itfSapSnIface.setSn(sn);
            itfSapSnIface.setMaterialCode(itfSapSnIfaceDto.getMaterialCode());
            itfSapSnIface.setQuantity(BigDecimal.valueOf(Double.valueOf(1)));
            itfSapSnIface.setUomCode(itfSapSnIfaceDto.getUomCode());
            itfSapSnIface.setInventoryType(itfSapSnIfaceDto.getInventoryType());
            itfSapSnIface.setSoNum(itfSapSnIfaceDto.getSoNum());
            if(StringUtils.isNotBlank(itfSapSnIfaceDto.getSoLineNum())){
                String soLineNum = itfSapSnIfaceDto.getSoLineNum().replaceAll("^(0+)", "");
                itfSapSnIface.setSoLineNum(soLineNum);
            }
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            itfSapSnIface.setProductDate(time.format(date));
            itfSapSnIface.setStatus(status);
            itfSapSnIface.setMessage(msg);
            itfSapSnIface.setIfaceId(customDbRepository.getNextKey("itf_sap_sn_iface_s"));
            itfSapSnIface.setCid(Long.parseLong(customDbRepository.getNextKey("itf_sap_sn_iface_cid_s")));
            itfSapSnIface.setBatchId(batchId);
            if(status.equals("E")){
                // 设置返回消息
                ItfSapSnReceiveReturnDto itfReceiveReturnDTO = new ItfSapSnReceiveReturnDto();
                itfReceiveReturnDTO.setProcessDate(currentTimeGet());
                itfReceiveReturnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                itfReceiveReturnDTO.setProcessMessage(msg);
                itfReceiveReturnDTO.setSn(itfSapSnIface.getSn());
                returnDetailDTOList.add(itfReceiveReturnDTO);
            }else{
                itfSapSnIfaceList.add(itfSapSnIface);
            }
            itfSapSnIfaceList1.add(itfSapSnIface);
        }
        //将数据写入接口表
        itfSapSnIfaceRepository.batchInsert(itfSapSnIfaceList1);

        //将数据从接口表写入业务表
        if(itfSapSnIfaceList.size()!=0){
            List<ItfSapSnIface> itfSapSnIfaceListSuccess = itfSapSnIfaceList.stream().filter(item
                    ->item.getStatus().equals("N")).collect(Collectors.toList());
            if(itfSapSnIfaceListSuccess.size()!=0){
                //获取LBBSA为空时的数据集合
                List<ItfSapSnIface> itfSapSnIfaceListSuccess1 = itfSapSnIfaceListSuccess.stream().filter(item
                        ->StringUtils.isBlank(item.getInventoryType())).collect(Collectors.toList());
                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                if(itfSapSnIfaceListSuccess1.size()!=0){
                    //调用API {eventCreate} 获取LBBSA为空时的eventId
                    eventCreateVO.setEventTypeCode("MATERIAL_LOT_CREATE");
                    String eventId1 = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                    for(ItfSapSnIface itfSapSnIface1:itfSapSnIfaceListSuccess1){
                        try {
                            //调用条码以及条码扩展属性更新方法
                            insertSapAndSapAttr(tenantId,eventId1,itfSapSnIface1);
                            //成功，改状态为S
                            itfSapSnIfaceMapper.update(tenantId,"S","",itfSapSnIface1.getIfaceId());
                        }catch (Exception e){
                            itfSapSnIfaceMapper.update(tenantId,"E",itfSapSnIface1.getMessage()+e.getMessage(),itfSapSnIface1.getIfaceId());
                            // 设置返回消息
                            ItfSapSnReceiveReturnDto itfReceiveReturnDTO1 = new ItfSapSnReceiveReturnDto();
                            itfReceiveReturnDTO1.setProcessDate(currentTimeGet());
                            itfReceiveReturnDTO1.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                            itfReceiveReturnDTO1.setProcessMessage(e.getMessage());
                            itfReceiveReturnDTO1.setSn(itfSapSnIface1.getSn());
                            returnDetailDTOList.add(itfReceiveReturnDTO1);
                        }

                    }
                }
                //获取LBBSA为01时的数据集合
                List<ItfSapSnIface> itfSapSnIfaceListSuccess2 = itfSapSnIfaceListSuccess.stream().filter(item
                        ->item.getInventoryType().equals("01")).collect(Collectors.toList());
                if(itfSapSnIfaceListSuccess2.size()!=0){
                    //调用API {eventCreate} 获取LBBSA为01时的eventId
                    eventCreateVO.setEventTypeCode("PRODUCTION_COMPLETED");
                    String eventId2 = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                    for(ItfSapSnIface itfSapSnIface2:itfSapSnIfaceListSuccess2){
                        try{
                            //调用条码更新、条码扩展属性更新、库存现有量增加的方法
                            insertSapAndSapAttrAndQuantity(tenantId,eventId2,itfSapSnIface2);
                            //成功，改状态为S
                            itfSapSnIfaceMapper.update(tenantId,"S","",itfSapSnIface2.getIfaceId());
                        }catch (Exception e){
                            itfSapSnIfaceMapper.update(tenantId,"E",itfSapSnIface2.getMessage()+ e.getMessage(),itfSapSnIface2.getIfaceId());
                            // 设置返回消息
                            ItfSapSnReceiveReturnDto itfReceiveReturnDTO2 = new ItfSapSnReceiveReturnDto();
                            itfReceiveReturnDTO2.setProcessDate(currentTimeGet());
                            itfReceiveReturnDTO2.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                            itfReceiveReturnDTO2.setProcessMessage(e.getMessage());
                            itfReceiveReturnDTO2.setSn(itfSapSnIface2.getSn());
                            returnDetailDTOList.add(itfReceiveReturnDTO2);
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(returnDetailDTOList)) {
            itfSapSnReturnDto.setStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            itfSapSnReturnDto.setMessage(WmsConstant.KEY_IFACE_MESSAGE_ERROR);
            itfSapSnReturnDto.setDetail(returnDetailDTOList);
        } else {
            itfSapSnReturnDto.setStatus(WmsConstant.KEY_IFACE_STATUS_SUCCESS);
            itfSapSnReturnDto.setMessage(WmsConstant.KEY_IFACE_MESSAGE_SUCCESS);
        }
        return itfSapSnReturnDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertSapAndSapAttr(Long tenantId, String eventId, ItfSapSnIface itfSapSnIface){
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        MtMaterialLot mtMaterialLot = new MtMaterialLot();
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLot.setTenantId(tenantId);
        mtMaterialLot.setMaterialLotCode(itfSapSnIface.getSn());
        List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.select(mtMaterialLot);
        if(mtMaterialLots.size()==0){
            //SN不存在执行创建
            mtMaterialLotVO2.setMaterialLotCode(itfSapSnIface.getSn());
            mtMaterialLotVO2.setCreateReason("INITIALIZE");
        }else{
            //SN不存在执行更新
            mtMaterialLotVO2.setMaterialLotId(mtMaterialLots.get(0).getMaterialLotId());
        }
        MtSitePlantReleation mtSitePlantReleation = new MtSitePlantReleation();
        mtSitePlantReleation.setTenantId(tenantId);
        mtSitePlantReleation.setPlantCode(itfSapSnIface.getSiteCode());
        mtSitePlantReleation.setSiteType(MtBaseConstants.ORGANIZATION_REL_TYPE.MANUFACTURING);
        List<MtSitePlantReleation> mtSitePlantReleations = mtSitePlantReleationRepository.select(mtSitePlantReleation);
        mtMaterialLotVO2.setSiteId(mtSitePlantReleations.get(0).getSiteId());
        mtMaterialLotVO2.setEnableFlag(itfSapSnIface.getEnableFlag());
        mtMaterialLotVO2.setQualityStatus(itfSapSnIface.getQualityStatus());
        MtMaterial mtMaterial = new MtMaterial();
        mtMaterial.setTenantId(tenantId);
        mtMaterial.setMaterialCode(itfSapSnIface.getMaterialCode());
        mtMaterialLotVO2.setMaterialId(mtMaterialMapper.select(mtMaterial).get(0).getMaterialId());
        MtUom mtUom = new MtUom();
        mtUom.setTenantId(tenantId);
        mtUom.setUomCode(itfSapSnIface.getUomCode());
        mtMaterialLotVO2.setPrimaryUomId(mtUomRepository.select(mtUom).get(0).getUomId());
        mtMaterialLotVO2.setPrimaryUomQty(itfSapSnIface.getQuantity().doubleValue());
        mtMaterialLotVO2.setLocatorId("-1");
        mtMaterialLotVO2.setLot("");
        //调用API {materialLotUpdate} 条码更新
        MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, WmsConstant.CONSTANT_N);
        MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
        mtExtendVO10.setEventId(eventId);
        mtExtendVO10.setKeyId(mtMaterialLotVO13.getMaterialLotId());
        List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
        MtExtendVO5 mtExtendVO5;
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("STATUS");
        mtExtendVO5.setAttrValue(itfSapSnIface.getMaterialLotStatus());
        mtExtendVO5List.add(mtExtendVO5);
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("SO_NUM");
        mtExtendVO5.setAttrValue(itfSapSnIface.getSoNum());
        mtExtendVO5List.add(mtExtendVO5);
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("SO_LINE_NUM");
        mtExtendVO5.setAttrValue(itfSapSnIface.getSoLineNum());
        mtExtendVO5List.add(mtExtendVO5);
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("PRODUCT_DATE");
        mtExtendVO5.setAttrValue(itfSapSnIface.getProductDate());
        mtExtendVO5List.add(mtExtendVO5);
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("MATERIAL_VERSION");
        mtExtendVO5.setAttrValue(itfSapSnIface.getMaterialVersion());
        mtExtendVO5List.add(mtExtendVO5);
        mtExtendVO10.setAttrs(mtExtendVO5List);
        //调用API {attrPropertyBatchUpdate} 更新条码扩展表
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertSapAndSapAttrAndQuantity(Long tenantId, String eventId, ItfSapSnIface itfSapSnIface){
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        MtMaterialLot mtMaterialLot = new MtMaterialLot();
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLot.setTenantId(tenantId);
        mtMaterialLot.setMaterialLotCode(itfSapSnIface.getSn());
        List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.select(mtMaterialLot);
        if(mtMaterialLots.size()==0){
            //SN不存在执行创建
            mtMaterialLotVO2.setMaterialLotCode(itfSapSnIface.getSn());
            mtMaterialLotVO2.setCreateReason("INITIALIZE");
        }else{
            //SN不存在执行更新
            mtMaterialLotVO2.setMaterialLotId(mtMaterialLots.get(0).getMaterialLotId());
        }
        MtSitePlantReleation mtSitePlantReleation = new MtSitePlantReleation();
        mtSitePlantReleation.setTenantId(tenantId);
        mtSitePlantReleation.setPlantCode(itfSapSnIface.getSiteCode());
        mtSitePlantReleation.setSiteType(MtBaseConstants.ORGANIZATION_REL_TYPE.MANUFACTURING);
        List<MtSitePlantReleation> mtSitePlantReleations = mtSitePlantReleationRepository.select(mtSitePlantReleation);
        mtMaterialLotVO2.setSiteId(mtSitePlantReleations.get(0).getSiteId());
        mtMaterialLotVO2.setEnableFlag(itfSapSnIface.getEnableFlag());
        mtMaterialLotVO2.setQualityStatus(itfSapSnIface.getQualityStatus());
        MtMaterial mtMaterial = new MtMaterial();
        mtMaterial.setTenantId(tenantId);
        mtMaterial.setMaterialCode(itfSapSnIface.getMaterialCode());
        mtMaterialLotVO2.setMaterialId(mtMaterialMapper.select(mtMaterial).get(0).getMaterialId());
        MtUom mtUom = new MtUom();
        mtUom.setTenantId(tenantId);
        mtUom.setUomCode(itfSapSnIface.getUomCode());
        mtMaterialLotVO2.setPrimaryUomId(mtUomRepository.select(mtUom).get(0).getUomId());
        mtMaterialLotVO2.setPrimaryUomQty(itfSapSnIface.getQuantity().doubleValue());
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setLocatorCode(itfSapSnIface.getLocatorCode());
        if(StringUtils.isBlank(mtModLocatorRepository.select(mtModLocator).get(0).getLocatorId())){
            mtMaterialLotVO2.setLocatorId("");
        }else{
            mtMaterialLotVO2.setLocatorId(mtModLocatorRepository.select(mtModLocator).get(0).getLocatorId());
        }
        mtMaterialLotVO2.setLot("");
        //调用API {materialLotUpdate} 条码更新
        MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, WmsConstant.CONSTANT_N);
        MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
        mtExtendVO10.setEventId(eventId);
        mtExtendVO10.setKeyId(mtMaterialLotVO13.getMaterialLotId());
        List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
        MtExtendVO5 mtExtendVO5;
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("STATUS");
        mtExtendVO5.setAttrValue(itfSapSnIface.getMaterialLotStatus());
        mtExtendVO5List.add(mtExtendVO5);
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("SO_NUM");
        mtExtendVO5.setAttrValue(itfSapSnIface.getSoNum());
        mtExtendVO5List.add(mtExtendVO5);
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("SO_LINE_NUM");
        mtExtendVO5.setAttrValue(itfSapSnIface.getSoLineNum());
        mtExtendVO5List.add(mtExtendVO5);
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("PRODUCT_DATE");
        mtExtendVO5.setAttrValue(itfSapSnIface.getProductDate());
        mtExtendVO5List.add(mtExtendVO5);
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("MATERIAL_VERSION");
        mtExtendVO5.setAttrValue(itfSapSnIface.getMaterialVersion());
        mtExtendVO5List.add(mtExtendVO5);
        mtExtendVO10.setAttrs(mtExtendVO5List);
        //调用API {attrPropertyBatchUpdate} 更新条码扩展表
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
        //调用API{onhandQtyUpdateProcess} 进行物料条码现有量增加
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(mtSitePlantReleations.get(0).getSiteId());
        MtMaterial mtMaterial1 = new MtMaterial();
        mtMaterial1.setTenantId(tenantId);
        mtMaterial1.setMaterialCode(itfSapSnIface.getMaterialCode());
        mtInvOnhandQuantityVO9.setMaterialId(mtMaterialMapper.select(mtMaterial1).get(0).getMaterialId());
        MtModLocator mtModLocator2 = new MtModLocator();
        mtModLocator2.setTenantId(tenantId);
        mtModLocator2.setLocatorCode(itfSapSnIface.getLocatorCode());
        mtInvOnhandQuantityVO9.setLocatorId(mtModLocatorRepository.select(mtModLocator2).get(0).getLocatorId());
        mtInvOnhandQuantityVO9.setChangeQuantity(itfSapSnIface.getQuantity().doubleValue());
        mtInvOnhandQuantityVO9.setEventId(eventId);
        mtInvOnhandQuantityVO9.setLotCode("");
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
    }
    /***
     * @Description: 获取当前时间
     */
    public static Date currentTimeGet() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(currentTime.format(formatter), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }
}
