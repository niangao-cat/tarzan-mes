package com.ruike.itf.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruike.hme.domain.entity.HmeRepairWorkOrderCreate;
import com.ruike.hme.domain.repository.HmeRepairWorkOrderCreateRepository;
import com.ruike.hme.domain.service.HmeOrganizationService;
import com.ruike.itf.app.service.ItfRepairWorkOrderCreateService;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.utils.GetDeclaredFields;
import com.ruike.itf.utils.SendESBConnect;
import com.ruike.itf.utils.Utils;
import io.choerodon.core.exception.CommonException;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tarzan.modeling.domain.entity.MtModArea;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItfRepairWorkOrderCreateServiceImpl implements ItfRepairWorkOrderCreateService {

    private final LovAdapter lovAdapter;
    private final HmeRepairWorkOrderCreateRepository hmeRepairWorkOrderCreateRepository;
    private final SendESBConnect sendESBConnect;
    private final MtCustomDbRepository customDbRepository;
    private final HmeOrganizationService organizationService;
    private final MtErrorMessageRepository mtErrorMessageRepository;

    public ItfRepairWorkOrderCreateServiceImpl(LovAdapter lovAdapter,
                                               HmeRepairWorkOrderCreateRepository hmeRepairWorkOrderCreateRepository,
                                               SendESBConnect sendESBConnect, MtCustomDbRepository customDbRepository, HmeOrganizationService organizationService, MtErrorMessageRepository mtErrorMessageRepository) {
        this.lovAdapter = lovAdapter;
        this.hmeRepairWorkOrderCreateRepository = hmeRepairWorkOrderCreateRepository;
        this.sendESBConnect = sendESBConnect;
        this.customDbRepository = customDbRepository;
        this.organizationService = organizationService;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
    }

    /**
     * <strong>Title : hmeRepairWorkOrderCreateService</strong><br/>
     * <strong>Description : ??????ERP?????????????????? </strong><br/>
     * <strong>Create on : 2020/12/10 ??????5:32</strong><br/>
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.domain.entity.HmeRepairWorkOrderCreate
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>????????????:</strong><br/>
     * ????????? | ???????????? | ????????????<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    @Override
    public HmeRepairWorkOrderCreate hmeRepairWorkOrderCreateService(Long tenantId, HmeRepairWorkOrderCreate dto) {
        // ??????????????????
        if (Objects.isNull(dto)) {
            throw new CommonException("??????????????????");
        }
        // ???????????????
        String[] fieldIsNotNull = {"materialLotCode", "materialCode", "qty", "primaryUomCode", "siteCode"
                , "locatorCode", "planStartTime", "planEndTime", "productionVersion"};
        GetDeclaredFields<HmeRepairWorkOrderCreate> fields = new GetDeclaredFields<>();
        List<String> declaredFields = fields.getDeclaredFields(dto, fieldIsNotNull);
        if (!CollectionUtils.isEmpty(declaredFields)) {
            throw new CommonException(declaredFields.toString() + "??????????????????");
        }
        // ????????????
        if (dto.getQty() < 0D || dto.getQty() == 0D) {
            throw new CommonException("??????????????????????????????");
        }
        dto.setStartTime(DateUtil.date2String(dto.getPlanStartTime(), "yyyyMMdd"));
        dto.setEndTime(DateUtil.date2String(dto.getPlanEndTime(), "yyyyMMdd"));
        String productionAdministrator = getProductionAdministrator(tenantId);
        dto.setProductionAdministrator(productionAdministrator);

        //??????????????????????????????
        dto.setLocatorCode("9994");
        // ??????ERP??????
        Hashtable<String, String> dataExchange = new Hashtable<>();
        dataExchange.put("materialCode", "MATNR");
        dataExchange.put("qty", "BDMNG");
        dataExchange.put("primaryUomCode", "MEINS");
        dataExchange.put("siteCode", "WERKS");
        dataExchange.put("locatorCode", "LGORT");
        dataExchange.put("startTime", "GSTRP");
        dataExchange.put("endTime", "GLTRP");
        dataExchange.put("productionVersion", "VERID");
        dataExchange.put("productionAdministrator", "FEVOR");
        Map<String, Object> maps = Utils.dataExchange(dataExchange, dto, "Z");

        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue(ItfConstant.LovCode.TIMELY_REPAIR_WO_FLAG, tenantId);
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(lovValueDTOS)) {
            throw new CommonException(ItfConstant.LovCode.TIMELY_REPAIR_WO_FLAG + "??????????????????\n????????????Y???N???Y??????????????????N?????????????????????");
        }
        String meaning = lovValueDTOS.get(0).getMeaning();
        if ("Y".equals(meaning)) {
            Map<String, Object> returnData = sendESBConnect.sendEsb(maps, "HEADER",
                    "ItfRepairWorkOrderCreateServiceImpl.ErpRepairWorkOrderCreateRestProxy",
                    ItfConstant.InterfaceCode.ESB_REPAIR_WORK_ORDER_SYNC);
            String jsonString = JSON.toJSONString(returnData.get("RETURN"));
            Map<String, Object> object = JSONObject.parseObject(jsonString);
            String status = returnData.get("STATUS").toString();
            if (!"S".equals(status)) {
                throw new CommonException(object.get("MESSAGE").toString());
            }
            if (Objects.isNull(returnData.get("AUFNR"))) {
                throw new CommonException("ERP??????????????????????????????????????????");
            }
            dto.setWorkOrderNum(returnData.get("AUFNR").toString().replaceAll("^(0+)", ""));
            dto.setStatus(status);
            dto.setMessage(object.get("MESSAGE").toString());
        }
        dto.setCid(Long.valueOf(this.customDbRepository.getNextKey("hme_repair_work_order_create_cid_s")));
        dto.setWorkOrderCreateId(this.customDbRepository.getNextKey("hme_repair_work_order_create_s"));
        hmeRepairWorkOrderCreateRepository.insertSelective(dto);
        return dto;
    }

    private String getProductionAdministrator(Long tenantId) {
        MtModArea area = organizationService.getUserDefaultArea(tenantId);
        List<LovValueDTO> values = lovAdapter.queryLovValue("WMS.WORKSHOP_MAPPING_REL", tenantId);
        values = values.stream().filter(rec -> rec.getMeaning().equals(area.getAreaCode())).sorted(Comparator.comparing(LovValueDTO::getOrderSeq)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(values) || values.size() > 1) {
            throw new MtException("HME_SPLIT_RECORD_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_SPLIT_RECORD_0026", "HME"));
        }
        return values.get(0).getValue();
    }


}
