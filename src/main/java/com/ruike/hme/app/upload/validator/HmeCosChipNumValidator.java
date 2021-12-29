package com.ruike.hme.app.upload.validator;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.domain.entity.HmeContainerCapacity;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.repository.HmeContainerCapacityRepository;
import com.ruike.hme.domain.repository.HmeCosOperationRecordRepository;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeWoJobSnRepository;
import com.ruike.hme.domain.vo.HmeCosChipNumImportVO;
import com.ruike.hme.domain.vo.HmeEoJobSnVO4;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ruike.hme.infra.constant.HmeConstants;

import io.choerodon.core.oauth.DetailsHelper;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

/**
 * COS芯片号-批量导入校验
 *
 * @author jiangling.zheng@hand-china.com 2020-9-16 16:45:59
 */
@ImportValidators({
        @ImportValidator(templateCode = "HME.COS_CHIP_NUM_IMP")
})
public class HmeCosChipNumValidator extends ValidatorHandler {
    private static final Logger logger = LoggerFactory.getLogger(HmeCosChipNumValidator.class);
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private MtOperationRepository mtOperationRepository;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private HmeContainerCapacityRepository hmeContainerCapacityRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        // 获取自定义参数
        Map<String, Object> args = getArgs();
        if (args.isEmpty()) {
            throw new MtException("HME_COS_CHIP_IMP_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_CHIP_IMP_0001", "HME"));
        }
        String workcellId = args.get("workcellId").toString();
        String operationId = args.get("operationId").toString();
        if (StringUtils.isBlank(workcellId)) {
            throw new MtException("HME_COS_CHIP_IMP_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_CHIP_IMP_0001", "HME"));
        }
        if (data != null && !"".equals(data)) {
            HmeCosChipNumImportVO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeCosChipNumImportVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            importVO.setOperationId(operationId);
            importVO.setWorkcellId(workcellId);
            // 非空校验
            Boolean isNullFlag = isNullValidate(tenantId, importVO);
            if (!isNullFlag) {
                return false;
            }
            // 业务校验
            Boolean importDataFlag = importDataValidate(tenantId, importVO);
            if (!importDataFlag) {
                return false;
            }
        }
        return true;
    }

    /**
     * 非空校验
     *
     * @param tenantId
     * @param importVO
     * @author jiangling.zheng@hand-china.com 2020/9/17 10:11
     * @return java.lang.Boolean
     */
    private Boolean isNullValidate(Long tenantId, HmeCosChipNumImportVO importVO) {
        boolean flag = true;
        if (StringUtils.isBlank(importVO.getMaterialLotCode())) {
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                            "HME_COS_CHIP_IMP_0002"));
            flag = false;
        }
        if (StringUtils.isBlank(importVO.getCosType())) {
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                            "HME_COS_CHIP_IMP_0003"));
            flag = false;
        }
        if (StringUtils.isBlank(importVO.getContainerType())) {
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                            "HME_COS_CHIP_IMP_0004"));
            flag = false;
        }
        /*if (StringUtils.isBlank(importVO.getOperationCode())) {
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                            "HME_COS_CHIP_IMP_0005"));
            flag = false;
        }*/
        if (StringUtils.isBlank(importVO.getWafer())) {
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                            "HME_COS_CHIP_IMP_0006"));
            flag = false;
        }
        if (StringUtils.isBlank(importVO.getWorkOrderNum())) {
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                            "HME_COS_CHIP_IMP_0007"));
            flag = false;
        }
        if (StringUtils.isBlank(importVO.getLoadPosition())) {
            getContext().addErrorMsg(mtErrorMessageRepository.messageCodeLimitMessageGet(tenantId, "HME",
                            "HME_COS_CHIP_IMP_0008"));
            flag = false;
        }
        return flag;
    }

    /**
     * 业务逻辑校验
     *
     * @param tenantId
     * @param importVO
     * @author jiangling.zheng@hand-china.com 2020/9/17 10:10
     * @return java.lang.Boolean
     */

    private Boolean importDataValidate(Long tenantId, HmeCosChipNumImportVO importVO) {
        boolean flag = true;
        // 获取当前用户默认工厂
        // String siteId = wmsSiteRepository.userDefaultSite(tenantId);
        // 物料批
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {
            {
                setMaterialLotCode(importVO.getMaterialLotCode());
                setTenantId(tenantId);
                setEnableFlag(HmeConstants.ConstantValue.YES);
                setQualityStatus(HmeConstants.ConstantValue.OK);
            }
        });
        if (Objects.isNull(mtMaterialLot)) {
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_CHIP_IMP_0009", "HME", importVO.getMaterialLotCode()));
            flag = false;
        }
        // cos类型
        String cosTypeMeaning = lovAdapter.queryLovMeaning("HME_COS_TYPE", tenantId, importVO.getCosType());
        if (StringUtils.isBlank(cosTypeMeaning)) {
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_CHIP_IMP_0010", "HME", importVO.getCosType()));
            flag = false;
        }
        // 容器类型校验
        MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(new MtContainerType() {
            {
                setContainerTypeCode(importVO.getContainerType());
                setTenantId(tenantId);
            }
        });
        if (Objects.isNull(mtContainerType)) {
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_CHIP_IMP_0011", "HME", importVO.getContainerType()));
            flag = false;
        } else {
            if (!StringUtils.equals(HmeConstants.ConstantValue.YES, mtContainerType.getEnableFlag())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_CHIP_IMP_0012", "HME", importVO.getContainerType()));
                flag = false;
            }
        }
        // 工艺校验
        /*MtOperation mtOperation = mtOperationRepository.selectOne(new MtOperation() {
            {
                setOperationName(importVO.getOperationCode());
                setTenantId(tenantId);
                setSiteId(siteId);
                setRevision("MAIN");
            }
        });
        if (Objects.isNull(mtOperation)) {
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_CHIP_IMP_0013", "HME", importVO.getOperationCode()));
            flag = false;
        } else {
            if (mtOperation.getDateTo() != null && mtOperation.getDateTo().getTime() < System.currentTimeMillis()) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_CHIP_IMP_0014", "HME", importVO.getOperationCode()));
                flag = false;
            }
        }*/
        // 容器类型扩展属性校验
        if (!flag) {
            HmeContainerCapacity hmeContainerCapacity =
                            hmeContainerCapacityRepository.selectOne(new HmeContainerCapacity() {
                                {
                                    setOperationId(importVO.getOperationId());
                                    setContainerTypeId(mtContainerType.getContainerTypeId());
                                    setCosType(importVO.getCosType());
                                    setTenantId(tenantId);
                                    setEnableFlag(HmeConstants.ConstantValue.YES);
                                }
                            });
            if (Objects.isNull(hmeContainerCapacity)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_CHIP_IMP_0015", "HME", importVO.getContainerType()));
                flag = false;
            }
        }
        // 装载位置校验
        try {
            String loadPosition = importVO.getLoadPosition().trim();
            Integer.parseInt(loadPosition);
            if (loadPosition.length() != HmeConstants.ConstantValue.TWO) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_CHIP_IMP_0016", "HME"));
                flag = false;
            }
        } catch (NumberFormatException e) {
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_CHIP_IMP_0016", "HME"));
            flag = false;
        }
        // 工单校验
        String workOrderId = mtWorkOrderRepository.numberLimitWoGet(tenantId, importVO.getWorkOrderNum());
        if (StringUtils.isBlank(workOrderId)) {
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_CHIP_IMP_0020", "HME"));
            flag = false;
        }
        //来料剩余芯片数校验
        //根据工单+工艺+WAFER+工位查询数据,若不存在，则报错
        if(!Objects.isNull(mtMaterialLot)) {
            HmeCosOperationRecord cosOperationRecord = hmeCosOperationRecordRepository.selectOne(new HmeCosOperationRecord() {{
                setTenantId(tenantId);
                setWorkOrderId(workOrderId);
                setOperationId(importVO.getOperationId());
                setWafer(importVO.getWafer());
                setWorkcellId(importVO.getWorkcellId());
                setAttribute1(mtMaterialLot.getLot());
            }});
            if (Objects.isNull(cosOperationRecord)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_009", "HME"));
                flag = false;
            }
        }
        return flag;
    }
}
