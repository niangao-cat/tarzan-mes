package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeRepairSnBindRepository;
import com.ruike.hme.domain.vo.HmeRepairSnBindVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeRepairSnBindMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/8/26 14:17
 */
@ImportValidators({
        @ImportValidator(templateCode = "HME.REPAIR_SN_BIND")
})
public class HmeRepairSnBindImportValidator extends ValidatorHandler {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private HmeRepairSnBindMapper hmeRepairSnBindMapper;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if (data != null && !"".equals(data)) {

            HmeRepairSnBindVO importDTO = null;
            try {
                importDTO = objectMapper.readValue(data, HmeRepairSnBindVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            // 校验EO标识 防止eo被修改
            List<String> eoIds = mtEoRepository.eoIdentify(tenantId, importDTO.getEoIdentification());
            if (CollectionUtils.isEmpty(eoIds)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_228", "HME", importDTO.getEoIdentification()));
                return false;
            }
            // 校验eo是否进站 进站则不允许绑定其他返修条码
            List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class).select(HmeEoJobSn.FIELD_JOB_ID).andWhere(Sqls.custom()
                    .andEqualTo(HmeEoJobSn.FIELD_EO_ID, eoIds.get(0))
                    .andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)).build());
            if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
                // EO已进站,不允许修改返修条码
                throw new MtException("HME_EO_JOB_REWORK_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_REWORK_0005", "HME"));
            }

            if (StringUtils.isBlank(importDTO.getWorkOrderNum())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_COS_CHIP_IMP_0007", "HME"));
                return false;
            }
            List<MtWorkOrder> mtWorkOrderList = mtWorkOrderRepository.woLimitWorkNUmQuery(tenantId, Collections.singletonList(importDTO.getWorkOrderNum()));
            if (CollectionUtils.isEmpty(mtWorkOrderList)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_CHIP_DATA_0001", "HME", importDTO.getWorkOrderNum()));
                return false;
            }
            // 返修SN校验
            if (StringUtils.isNotBlank(importDTO.getRepairSn())) {
                MtMaterialLot mtMaterialLot = new MtMaterialLot();
                mtMaterialLot.setTenantId(tenantId);
                mtMaterialLot.setMaterialLotCode(importDTO.getRepairSn());
                MtMaterialLot reworkMaterialLot = mtMaterialLotRepository.selectOne(mtMaterialLot);
                if (reworkMaterialLot == null) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_COS_RETEST_IMPORT_004", "HME", importDTO.getEoIdentification()));
                    return false;
                }
                if (!HmeConstants.ConstantValue.YES.equals(reworkMaterialLot.getEnableFlag())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_WO_INPUT_0004", "HME", importDTO.getRepairSn()));
                    return false;
                }

                // 物料和工单物料要相同
                if (!StringUtils.equals(reworkMaterialLot.getMaterialId(), mtWorkOrderList.get(0).getMaterialId())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_REPAIR_SN_IMPORT_003", "HME", importDTO.getWorkOrderNum(), importDTO.getRepairSn()));
                    return false;
                }
                // 如果已绑定运行条码则不允许再绑定
                if (StringUtils.isNotBlank(importDTO.getRepairSn())) {
                    List<String> eoIdList = hmeRepairSnBindMapper.queryWorkingEoByOldMaterialLotCode(tenantId, importDTO.getRepairSn());
                    List<String> filterEoIds = eoIdList.stream().filter(vo -> !StringUtils.equals(eoIds.get(0), vo)).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(filterEoIds)) {
                        getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_REWORK_0004", "HME"));
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
