package com.ruike.itf.app.assembler;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.infra.util.BeanCopierUtil;
import com.ruike.itf.domain.entity.ItfServiceTransferIface;
import com.ruike.itf.domain.vo.ServiceTransferIfaceInvokeVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.ruike.itf.domain.vo.ServiceTransferIfaceInvokeVO.*;
import static com.ruike.itf.infra.constant.ItfConstant.ConstantValue.ONE;
import static com.ruike.itf.infra.constant.ItfConstant.InstructionStatus.NEW;

/**
 * <p>
 * 售后大仓回调 转换器
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/1 14:46
 */
@Component
public class ServiceTransferIfaceAssembler {
    private final MtCustomDbRepository customDbRepository;

    public ServiceTransferIfaceAssembler(MtCustomDbRepository customDbRepository) {
        this.customDbRepository = customDbRepository;
    }

    public ItfServiceTransferIface invokeVoToEntity(ServiceTransferIfaceInvokeVO vo) {
        ItfServiceTransferIface entity = new ItfServiceTransferIface();
        BeanCopierUtil.copy(vo, entity);
        entity.setProcessDate(new Date());
        entity.setProcessStatus(NEW);
        entity.setInterfaceId(customDbRepository.getNextKey(ItfServiceTransferIface.SEQUENCE));
        entity.setCid(Long.valueOf(customDbRepository.getNextKey(ItfServiceTransferIface.CID_SEQUENCE)));
        return entity;
    }

    public List<ItfServiceTransferIface> invokeVoToEntityBatch(List<ServiceTransferIfaceInvokeVO> list) {
        Iterator<String> ids = customDbRepository.getNextKeys(ItfServiceTransferIface.SEQUENCE, list.size()).iterator();
        Iterator<String> cids = customDbRepository.getNextKeys(ItfServiceTransferIface.CID_SEQUENCE, list.size()).iterator();
        List<ItfServiceTransferIface> result = new ArrayList<>(list.size());
        Date date = DateUtil.date();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        list.forEach(rec -> {
            ItfServiceTransferIface entity = new ItfServiceTransferIface();
            BeanCopierUtil.copy(rec, entity);
            entity.setInterfaceId(ids.next());
            entity.setCid(Long.valueOf(cids.next()));
            entity.setProcessDate(new Date());
            entity.setProcessStatus(NEW);
            entity.setCreatedBy(userId);
            entity.setCreationDate(date);
            entity.setLastUpdateDate(date);
            entity.setLastUpdatedBy(userId);
            entity.setObjectVersionNumber(Long.valueOf(ONE));
            result.add(entity);
        });

        return result;
    }

    public Map<String, Object> entityToFieldMap(ItfServiceTransferIface entity) {
        Map<String, Object> fieldMap = new HashMap<>(32);
        fieldMap.put(SAP_INTERFACE_ID, entity.getInterfaceId());
        fieldMap.put(SAP_ACCOUNTING_DATE, entity.getAccountingDate());
        fieldMap.put(SAP_LEDGER_DATE, entity.getLedgerDate());
        fieldMap.put(SAP_SN_NUM, entity.getSnNum());
        fieldMap.put(SAP_GM_CODE, entity.getGmCode());
        fieldMap.put(SAP_MATERIAL_CODE, entity.getMaterialCode());
        fieldMap.put(SAP_FROM_SITE_CODE, entity.getFromSiteCode());
        fieldMap.put(SAP_FROM_WAREHOUSE_CODE, entity.getFromWarehouseCode());
        fieldMap.put(SAP_MOVE_TYPE, entity.getMoveType());
        fieldMap.put(SAP_QUANTITY, entity.getQuantity());
        fieldMap.put(SAP_UOM_CODE, entity.getUomCode());
        fieldMap.put(SAP_TO_SITE_CODE, entity.getToSiteCode());
        fieldMap.put(SAP_TO_WAREHOUSE_CODE, entity.getToWarehouseCode());
        fieldMap.put(SAP_BARCODE, entity.getBarcode());
        fieldMap.put(SAP_AREA_CODE, entity.getAreaCode());
        fieldMap.put(SAP_RECEIVE_DATE, entity.getReceiveDate());
        fieldMap.put(SAP_REAL_NAME, entity.getRealName());
        fieldMap.put(SAP_BACK_TYPE, entity.getBackType());
        return fieldMap;
    }
}
