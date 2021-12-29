package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfSrmMaterialWasteIfaceSyncDTO;
import com.ruike.itf.app.service.ItfSrmMaterialWasteIfaceService;
import com.ruike.itf.domain.entity.ItfSrmMaterialWasteIface;
import com.ruike.itf.domain.repository.ItfSrmMaterialWasteIfaceRepository;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.utils.GetDeclaredFields;
import com.ruike.itf.utils.SendESBConnect;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 料废调换接口记录表应用服务默认实现
 *
 * @author kejin.liu01@hand-china.com 2020-09-21 11:05:25
 */
@Slf4j
@Service
public class ItfSrmMaterialWasteIfaceServiceImpl implements ItfSrmMaterialWasteIfaceService {

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private SendESBConnect sendESBConnect;

    @Autowired
    private ItfSrmMaterialWasteIfaceRepository itfSrmMaterialWasteIfaceRepository;

    /**
     * SRM系统-料废调换创建Rest接口
     *
     * @param itfSrmInstructionIface
     * @param tenantId
     * @return
     * @author jiangling.zheng@hand-china.com 2020/9/21 11:09
     */
    @Override
    public List<ItfSrmMaterialWasteIfaceSyncDTO> srmMaterialWasteExchangeCreate(List<ItfSrmMaterialWasteIfaceSyncDTO> itfSrmInstructionIface, Long tenantId) {
        List<ItfSrmMaterialWasteIfaceSyncDTO> list = new ArrayList<>();
        // 判断必输
        GetDeclaredFields<ItfSrmMaterialWasteIface> declaredFields = new GetDeclaredFields();
        String[] fields = {"vendorCode", "itemCode", "primayChangeQty", "primaryUomQty", "primaryUom", "shipToOrganization"};
        for (ItfSrmMaterialWasteIfaceSyncDTO syncDTO : itfSrmInstructionIface) {
            ItfSrmMaterialWasteIface iface = new ItfSrmMaterialWasteIface();
            BeanUtils.copyProperties(syncDTO, iface);
            iface.setTenantId(tenantId);
            iface.setIfaceId(customDbRepository.getNextKey("itf_srm_material_waste_iface_s"));
            iface.setCid(Long.valueOf(customDbRepository.getNextKey("itf_srm_material_waste_iface_cid_s")));
            // 判断传参是否为空
            List<String> fieldResult = declaredFields.getDeclaredFields(iface, fields);
            if (CollectionUtils.isNotEmpty(fieldResult)) {
                iface.setZflag("N");
                iface.setZmessage(fieldResult.toString() + "不可为空！");
                itfSrmMaterialWasteIfaceRepository.insertSelective(iface);
                syncDTO.setStatus(iface.getZflag());
                syncDTO.setMessage(iface.getZmessage());
                list.add(syncDTO);
                continue;
            }
            // 查询工厂，供应商，物料，数量单位
            Map<String, Object> record = new HashMap<>();
            Map<String, Object> records = new HashMap<>();
            record.put("record", syncDTO);
            records.put("records", record);
            Map<String, Object> map = sendESBConnect.sendEsb(records, "exchangeInfo",
                    "ItfSrmMaterialWasteIfaceServiceImpl.srmMaterialWasteExchangeCreate", ItfConstant.InterfaceCode.SRM_MATERIAL_WASTE_EX_CHANGE);
            log.info("resultMap:{}", map);
            log.info("status:{}", map.get("status").toString());
            log.info("message:{}", map.get("message").toString());
            String status = map.get("status").toString();
            String message = map.get("message").toString();
            iface.setZflag(status);
            iface.setZmessage(message);
            itfSrmMaterialWasteIfaceRepository.insertSelective(iface);
            syncDTO.setStatus(iface.getZflag());
            syncDTO.setMessage(iface.getZmessage());
            list.add(syncDTO);
        }
        return list;
    }
}
