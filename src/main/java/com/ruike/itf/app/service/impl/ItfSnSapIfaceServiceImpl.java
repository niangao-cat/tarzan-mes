package com.ruike.itf.app.service.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.api.dto.ItfSnSapIfaceDTO;
import com.ruike.itf.app.service.ItfSnSapIfaceService;
import com.ruike.itf.domain.entity.ItfSnSapIface;
import com.ruike.itf.domain.repository.ItfSnSapIfaceRepository;
import com.ruike.itf.utils.GetDeclaredFields;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 物料组接口表应用服务默认实现
 *
 * @author kejin.liu01@hand-china.com 2020-09-04 11:31:56
 */
@Service
public class ItfSnSapIfaceServiceImpl implements ItfSnSapIfaceService {

    @Autowired
    private ItfSnSapIfaceRepository itfSnSapIfaceRepository;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;


    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    /**
     * 成品SN发货回传接口
     *
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com
     */
    @Override
    public List<ItfSnSapIfaceDTO> updateMaterialStatus(List<ItfSnSapIfaceDTO> dto) {
        Long batchId = Long.valueOf(customDbRepository.getNextKey("itf_sn_sap_iface_cid_s"));
        GetDeclaredFields<ItfSnSapIfaceDTO> fields = new GetDeclaredFields<>();
        String[] strings = {"SERNR", "STTXT", "BWART"};
        List<ItfSnSapIfaceDTO> returnDto = new ArrayList<>();
        for (ItfSnSapIfaceDTO iface : dto) {
            ItfSnSapIface itfSnSapIface = new ItfSnSapIface();
            itfSnSapIface.setIfaceId(customDbRepository.getNextKey("itf_sn_sap_iface_s"));
            itfSnSapIface.setTenantId(tenantId);
            itfSnSapIface.setBatchId(batchId);
            itfSnSapIface.setVbebl(iface.getVBELN());
            itfSnSapIface.setPosnr(iface.getPOSNR());
            itfSnSapIface.setMatnr(iface.getMATNR().replaceAll("^(0+)", ""));
            itfSnSapIface.setSernr(iface.getSERNR());
            itfSnSapIface.setSttxt(iface.getSTTXT());
            itfSnSapIface.setEwart(iface.getBWART());
            // 判断是否为空，不为空则存储数据,跳过后续逻辑
            List<String> declaredFields = fields.getDeclaredFields(iface, strings);
            if (CollectionUtils.isNotEmpty(declaredFields)) {
                itfSnSapIface.setMessage(declaredFields.toString());
                itfSnSapIface.setStatus("E");
                itfSnSapIfaceRepository.insertSelective(itfSnSapIface);
                iface.setZFLAG("E");
                iface.setZMESSAGE(declaredFields.toString() + "不可以为空！");
                returnDto.add(iface);
                continue;
            }
            // 判断类型是否为-ECUS | ESTO
            if (!"ECUS".equals(iface.getSTTXT()) && !"ESTO".equals(iface.getSTTXT())) {
                String error = "[STTXT]类型不正确，正确类型为[ECUS-在客户场地 | ESTO-在仓库中]！";
                itfSnSapIface.setMessage(error);
                itfSnSapIface.setStatus("E");
                itfSnSapIfaceRepository.insertSelective(itfSnSapIface);
                iface.setZFLAG("E");
                iface.setZMESSAGE(error);
                returnDto.add(iface);
                continue;
            }
            // 校验条码是否存在
            MtMaterialLot mtMaterialLot = new MtMaterialLot();
            mtMaterialLot.setTenantId(tenantId);
            mtMaterialLot.setMaterialLotCode(itfSnSapIface.getSernr());
            List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.select(mtMaterialLot);
            if (CollectionUtils.isEmpty(mtMaterialLots)) {
                String error = "该条码不存在MES，请与MES核查！";
                itfSnSapIface.setMessage(error);
                itfSnSapIface.setStatus("E");
                itfSnSapIfaceRepository.insertSelective(itfSnSapIface);
                iface.setZFLAG("E");
                iface.setZMESSAGE(error);
                returnDto.add(iface);
                continue;
            }
            String materialLotId = mtMaterialLots.get(0).getMaterialLotId();
            String siteId = mtMaterialLots.get(0).getSiteId();
            String locatorId = mtMaterialLots.get(0).getLocatorId();
            String materialId = mtMaterialLots.get(0).getMaterialId();
            String materialLotCode = mtMaterialLots.get(0).getMaterialLotCode();
            String lot = mtMaterialLots.get(0).getLot();
            Double primaryUomQty = mtMaterialLots.get(0).getPrimaryUomQty();
            String requestTypeCode = null;
            String enableFlag = null;
            String attrValue = null;
            if ("ECUS".equals(iface.getSTTXT())) {
                if ("N".equals(mtMaterialLots.get(0).getEnableFlag())) {
                    String error = "该条码已经失效，请与MES核查！";
                    itfSnSapIface.setMessage(error);
                    itfSnSapIface.setStatus("E");
                    itfSnSapIfaceRepository.insertSelective(itfSnSapIface);
                    iface.setZFLAG("E");
                    iface.setZMESSAGE(error);
                    returnDto.add(iface);
                    continue;
                }
                requestTypeCode = "SN_SHIPPING";
                enableFlag = "N";
                attrValue = "SHIPPED";
            } else {
                if ("Y".equals(mtMaterialLots.get(0).getEnableFlag())) {
                    String error = "该条码未曾失效，请与MES核查！";
                    itfSnSapIface.setMessage(error);
                    itfSnSapIface.setStatus("E");
                    itfSnSapIfaceRepository.insertSelective(itfSnSapIface);
                    iface.setZFLAG("E");
                    iface.setZMESSAGE(error);
                    returnDto.add(iface);
                    continue;
                }
                requestTypeCode = "SN_RETURN";
                enableFlag = "Y";
                attrValue = "INSTOCK";
            }

            // 请求创建事件
            try {
                String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, requestTypeCode);
                // 创建事件
                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                eventCreateVO.setEventTypeCode(requestTypeCode);
                eventCreateVO.setEventRequestId(eventRequestId);
                String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                // 条码更新状态
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setMaterialLotId(materialLotId);
                mtMaterialLotVO2.setEnableFlag(enableFlag);
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
                // 更新扩展表
                MtExtendVO10 materialLotExtend = new MtExtendVO10();
                materialLotExtend.setKeyId(materialLotId);
                MtExtendVO5 statusAttr = new MtExtendVO5();
                statusAttr.setAttrName(HmeConstants.ExtendAttr.STATUS);
                statusAttr.setAttrValue(attrValue);
                materialLotExtend.setAttrs(Collections.singletonList(statusAttr));
                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotExtend);
                // 更新现有量
                MtInvOnhandQuantityVO9 condition = new MtInvOnhandQuantityVO9();
                condition.setSiteId(siteId);
                condition.setLocatorId(locatorId);
                condition.setMaterialId(materialId);
                condition.setLotCode(lot);
                condition.setChangeQuantity(primaryUomQty);
                condition.setEventId(eventId);
                condition.setOwnerId("");
                condition.setOwnerType("");
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, condition);
            } catch (Exception e) {
                String message = e.getMessage();
                itfSnSapIface.setMessage(message);
                itfSnSapIface.setStatus("E");
                itfSnSapIfaceRepository.insertSelective(itfSnSapIface);
                iface.setZFLAG("E");
                iface.setZMESSAGE(message);
            }
            // 插入记录表
            itfSnSapIface.setMessage("成功.");
            itfSnSapIface.setStatus("Y");
            itfSnSapIfaceRepository.insertSelective(itfSnSapIface);
            iface.setZFLAG("Y");
            iface.setZMESSAGE("成功.");
            returnDto.add(iface);
        }
        return returnDto;
    }


}
