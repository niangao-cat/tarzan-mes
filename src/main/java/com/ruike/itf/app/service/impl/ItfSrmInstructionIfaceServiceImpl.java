package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfSrmInstructionIfaceSyncDTO;
import com.ruike.itf.app.service.ItfSrmInstructionIfaceService;
import com.ruike.itf.domain.entity.ItfSrmInstructionIface;
import com.ruike.itf.domain.repository.ItfSrmInstructionIfaceRepository;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.utils.SendESBConnect;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.infra.mapper.MtInstructionDocMapper;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtSupplierRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 送货单状态接口记录表应用服务默认实现
 *
 * @author kejin.liu01@hand-china.com 2020-09-09 10:05:04
 */
@Slf4j
@Service
public class ItfSrmInstructionIfaceServiceImpl implements ItfSrmInstructionIfaceService {

    private final MtSupplierRepository mtSupplierRepository;

    private final MtSitePlantReleationRepository mtSitePlantReleationRepository;

    private final ItfSrmInstructionIfaceRepository itfSrmInstructionIfaceRepository;

    private final MtCustomDbRepository customDbRepository;

    private final SendESBConnect sendESBConnect;

    private final MtInstructionDocMapper mtInstructionDocMapper;

    public ItfSrmInstructionIfaceServiceImpl(MtSupplierRepository mtSupplierRepository, MtSitePlantReleationRepository mtSitePlantReleationRepository, ItfSrmInstructionIfaceRepository itfSrmInstructionIfaceRepository, MtCustomDbRepository customDbRepository, SendESBConnect sendESBConnect, MtInstructionDocMapper mtInstructionDocMapper) {
        this.mtSupplierRepository = mtSupplierRepository;
        this.mtSitePlantReleationRepository = mtSitePlantReleationRepository;
        this.itfSrmInstructionIfaceRepository = itfSrmInstructionIfaceRepository;
        this.customDbRepository = customDbRepository;
        this.sendESBConnect = sendESBConnect;
        this.mtInstructionDocMapper = mtInstructionDocMapper;
    }

    @Override
    public List<ItfSrmInstructionIface> sendInstructionDocStatusSrm(List<ItfSrmInstructionIface> itfSrmInstructionIface, Long tenantId) {
        List<ItfSrmInstructionIface> list = new ArrayList<>();
        for (ItfSrmInstructionIface iface : itfSrmInstructionIface) {
            iface.setTenantId(tenantId);
            iface.setIfaceId(customDbRepository.getNextKey("itf_srm_instruction_iface_s"));
            iface.setCid(Long.valueOf(customDbRepository.getNextKey("itf_srm_instruction_iface_cid_s")));
            StringBuffer errorMsg = new StringBuffer();
            // 校验必输字段
            if (Strings.isEmpty(iface.getInstructionDocNum())) {
                errorMsg.append("送货单号必输!");
            }
            if (Strings.isEmpty(iface.getInstructionDocType())) {
                errorMsg.append("送货单类型必输!");
            }
            if (Strings.isEmpty(iface.getInstructionDocStatus())) {
                errorMsg.append("送货单状态必输!");
            }
            if (Strings.isEmpty(iface.getSupplierId())) {
                errorMsg.append("供应商ID必输!");
            } else {// 查询供应商
                String supplierId = iface.getSupplierId();
                MtSupplier mtSupplier = new MtSupplier();
                mtSupplier.setSupplierId(supplierId);
                mtSupplier.setTenantId(tenantId);
                List<MtSupplier> mtSuppliers = mtSupplierRepository.select(mtSupplier);
                if (CollectionUtils.isEmpty(mtSuppliers)) {
                    errorMsg.append("供应商不存在!");
                } else {
                    iface.setSupplierCode(mtSuppliers.get(0).getSupplierCode());
                }
            }
            if (Strings.isEmpty(iface.getSiteId())) {
                errorMsg.append("站点ID必输!");
            } else {// 查询供应商和工厂
                String siteId = iface.getSiteId();
                MtSitePlantReleation mtSitePlantReleation = new MtSitePlantReleation();
                mtSitePlantReleation.setSiteId(siteId);
                mtSitePlantReleation.setTenantId(tenantId);
                List<MtSitePlantReleation> mtSitePlantReleations = mtSitePlantReleationRepository.select(mtSitePlantReleation);
                if (CollectionUtils.isEmpty(mtSitePlantReleations)) {
                    errorMsg.append("工厂不存在!");
                } else {
                    iface.setPlantCode(mtSitePlantReleations.get(0).getPlantCode());
                }
            }
            if (Strings.isNotEmpty(errorMsg)) {
                iface.setZflag("E");
                iface.setZmessage(errorMsg.toString());
            } else {
                ItfSrmInstructionIfaceSyncDTO sendData = new ItfSrmInstructionIfaceSyncDTO(iface);
                Map<String, Object> record = new HashMap<>();
                Map<String, Object> records = new HashMap<>();
                record.put("record", sendData);
                records.put("records", record);
                Map<String, Object> map = sendESBConnect.sendEsb(records, "asnReturnInfo",
                        "ItfSrmInstructionIfaceServiceImpl.sendInstructionDocStatusSrm", ItfConstant.InterfaceCode.SRM_DELIVERY_NOTE_STATUS);
                String status = map.get("status").toString();
                String message = map.get("message").toString();
                iface.setZflag(status);
                iface.setZmessage(message);
            }
            itfSrmInstructionIfaceRepository.insertSelective(iface);
            MtInstructionDoc doc = new MtInstructionDoc();
            doc.setInstructionDocId(iface.getInstructionDocId());
            doc.setInterfaceFlag(iface.getZflag());
            doc.setInterfaceMsg(iface.getZmessage());
            mtInstructionDocMapper.updateByPrimaryKeySelective(doc);
            list.add(iface);
        }

        return list;
    }

    /**
     * 查询送货单状态
     *
     * @param instructionDocId
     * @param tenantId
     * @return
     */
    @Override
    public List<ItfSrmInstructionIface> selectMtDocStatus(String instructionDocId, Long tenantId) {
        return itfSrmInstructionIfaceRepository.selectMtDocStatus(instructionDocId, tenantId);
    }


}
