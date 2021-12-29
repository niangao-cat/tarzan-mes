package com.ruike.hme.app.service.validator;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruike.hme.infra.constant.HmeConstants;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO21;
import tarzan.method.domain.util.Constant;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.WhereField;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * HmeWorkOrderSnValidator
 *
 * @author liyuan.lv@hand-china.com 2020/06/09 15:53
 */
@ImportValidators({
        @ImportValidator(templateCode = "HME_WO_SN")
})
public class HmeWorkOrderSnValidator extends ValidatorHandler {
    private static final Logger logger = LoggerFactory.getLogger(HmeWorkOrderSnValidator.class);

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }

        if (data != null && !"".equals(data)) {
            JSONObject object = (JSONObject) JSON.parse(data);
            String workOrderNum = object.getString("workOrderNum");

            String workOrderId = mtWorkOrderRepository.numberLimitWoGet(tenantId, workOrderNum);

            if (StringUtils.isBlank(workOrderId)) {
                // 工单号不存在
                getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME", "HME_WORK_ORDER_SN_005"));
                return false;
            }

            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, workOrderId);
            if (!HmeConstants.WorkOrderStatus.EORELEASED.equals(mtWorkOrder.getStatus()) && !HmeConstants.WorkOrderStatus.RELEASED.equals(mtWorkOrder.getStatus())
                    && !"HOLD".equals(mtWorkOrder.getStatus())) {
                // 工单未下达
                getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME", "HME_WORK_ORDER_SN_006"));
                return false;
            }

            String materialLotCode = object.getString("snNumber");

            List<MtMaterialLot> existsMaterialLotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, materialLotCode)
                            .andEqualTo(MtMaterialLot.FIELD_ENABLE_FLAG, HmeConstants.ConstantValue.YES)
                            .andIsNotNull(MtMaterialLot.FIELD_EO_ID))
                    .build());
            if (CollectionUtils.isNotEmpty(existsMaterialLotList)) {
                // 该产品序列号系统中已存在
                getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME", "HME_WORK_ORDER_SN_007"));
                return false;
            }
        }
        return true;
    }
}
