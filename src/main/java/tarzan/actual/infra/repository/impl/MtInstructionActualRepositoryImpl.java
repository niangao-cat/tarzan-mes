package tarzan.actual.infra.repository.impl;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualHis;
import tarzan.actual.domain.repository.MtInstructionActualHisRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtInstructionActualMapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 指令实绩表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtInstructionActualRepositoryImpl extends BaseRepositoryImpl<MtInstructionActual>
        implements MtInstructionActualRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtInstructionActualHisRepository mtInstructionActualHisRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtInstructionActualMapper mtInstructionActualMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtInstructionActualVO1 instructionActualUpdate(Long tenantId, MtInstructionActualVO dto) {
        MtInstructionActual mtInstructionActual = null;
        Double currentQty = null;
        String materialId = null;

        // 1. 查找指令实绩
        if (StringUtils.isNotEmpty(dto.getActualId())) {
            mtInstructionActual = instructionActualPropertyGet(tenantId, dto.getActualId());
            if (null != mtInstructionActual) {
                currentQty = mtInstructionActual.getActualQty();
                materialId = mtInstructionActual.getMaterialId();
            }
        } else {
            if (StringUtils.isNotEmpty(dto.getInstructionId())) {
                MtInstructionActual searchMtInstructionActual = new MtInstructionActual();
                searchMtInstructionActual.setInstructionId(dto.getInstructionId());
                searchMtInstructionActual
                                .setFromLocatorId(null == dto.getFromLocatorId() ? "" : dto.getFromLocatorId());
                searchMtInstructionActual.setToLocatorId(null == dto.getToLocatorId() ? "" : dto.getToLocatorId());
                List<MtInstructionActual> list = this.mtInstructionActualMapper.select(searchMtInstructionActual);
                if (CollectionUtils.isNotEmpty(list)) {
                    if (1 < list.size()) {
                        throw new MtException("MT_INSTRUCTION_0053",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0053", "INSTRUCTION",
                                                        "【API:instructionActualUpdate】"));
                    }

                    mtInstructionActual = list.get(0);
                    if (null != mtInstructionActual && null != mtInstructionActual.getActualId()) {
                        currentQty = mtInstructionActual.getActualQty();
                        materialId = mtInstructionActual.getMaterialId();
                    }
                }
            }
        }

        // 返回实绩Id和历史Id
        MtInstructionActualVO1 result = new MtInstructionActualVO1();
        if (mtInstructionActual == null) {
            // 1.检验通用参数的合规性
            if (StringUtils.isEmpty(dto.getInstructionType())) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "instructionType", "【API:instructionActualUpdate】"));
            }
            if (dto.getActualQty() == null) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "actualQty", "【API:instructionActualUpdate】"));

            }
            if (StringUtils.isEmpty(dto.getEventId())) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "eventId", "【API:instructionActualUpdate】"));

            }

            // 1.a materialId、eoId不能同时为空
            if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getEoId())) {
                throw new MtException("MT_INSTRUCTION_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                                "INSTRUCTION", "materialId", "eoId", "【API:instructionActualUpdate】"));

            }

            // 1.c materialId、uomId只能同时为空或者同时不为空
            if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getUomId())
                            || StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getUomId())) {
                throw new MtException("MT_INSTRUCTION_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0007",
                                                "INSTRUCTION", "materialId", "uomId", "【API:instructionActualUpdate】"));

            }
            // 1.d sourceOrderType、sourceOrderId只能同时为空或者同时不为空
            if (StringUtils.isEmpty(dto.getSourceOrderType()) && StringUtils.isNotEmpty(dto.getSourceOrderId())
                            || StringUtils.isNotEmpty(dto.getSourceOrderType())
                                            && StringUtils.isEmpty(dto.getSourceOrderId())) {
                throw new MtException("MT_INSTRUCTION_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0007",
                                                "INSTRUCTION", "sourceOrderType", "sourceOrderId",
                                                "【API:instructionActualUpdate】"));

            }

            // 1.e验toSiteId与fromSiteId不能同时为空
            if (StringUtils.isEmpty(dto.getToSiteId()) && StringUtils.isEmpty(dto.getFromSiteId())) {
                throw new MtException("MT_INSTRUCTION_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                                "INSTRUCTION", "toSiteId", "fromSiteId",
                                                "【API:instructionActualUpdate】"));

            }

            // 1.f校验fromLocatorId与toLocatorId不能同时为空
            if (StringUtils.isEmpty(dto.getToLocatorId()) && StringUtils.isEmpty(dto.getFromLocatorId())) {
                throw new MtException("MT_INSTRUCTION_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                                "INSTRUCTION", "toLocatorId", "fromLocatorId",
                                                "【API:instructionActualUpdate】"));

            }

            // 1.g supplierId、supplierSiteId只能同时为空或者同时不为空
            if (StringUtils.isEmpty(dto.getSupplierId()) && StringUtils.isNotEmpty(dto.getSupplierSiteId())) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "supplierId", "【API:instructionActualUpdate】"));

            }

            // 1.h customerSiteId、customerId只能同时为空或者同时不为空
            if (StringUtils.isEmpty(dto.getCustomerSiteId()) && StringUtils.isNotEmpty(dto.getCustomerId())
                            || StringUtils.isNotEmpty(dto.getCustomerSiteId())
                                            && StringUtils.isEmpty(dto.getCustomerId())) {
                throw new MtException("MT_INSTRUCTION_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0007",
                                                "INSTRUCTION", "customerSiteId", "customerId",
                                                "【API:instructionActualUpdate】"));

            }

            // 1.i调用API{groupLimitTypeQuery}
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("INSTRUCTION");
            mtGenTypeVO2.setTypeGroup("INSTRUCTION_MOVE_TYPE");
            List<MtGenType> instructionTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
            if (CollectionUtils.isEmpty(instructionTypes)) {
                return null;
            }
            List<String> list = instructionTypes.stream().map(t -> t.getTypeCode()).collect(Collectors.toList());
            if (!list.contains(dto.getInstructionType())) {
                throw new MtException("MT_INSTRUCTION_0004",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0004",
                                                "INSTRUCTION", dto.getInstructionType(), list.toString(),
                                                "【API:instructionActualUpdate】"));

            }

            // 4.校验不同指令类型下参数合规性
            switch (dto.getInstructionType()) {
                case "RECEIVE_FROM_SUPPLIER":
                    if (StringUtils.isEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "supplierId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toSiteId",
                                                        "【API:instructionActualUpdate】"));

                    }

                    if (StringUtils.isNotEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "fromSiteId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "fromLocatorId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "customerId", "【API:instructionActualUpdate】"));

                    }
                    break;
                case "RETURN_TO_SUPPLIER":
                    if (StringUtils.isEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "supplierId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "toSiteId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "ToLocatorId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "customerId", "【API:instructionActualUpdate】"));

                    }
                    break;
                case "SHIP_TO_CUSTOMER":
                    if (StringUtils.isEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "customerId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "toSiteId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "toLocatorId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "supplierId", "【API:instructionActualUpdate】"));

                    }
                    break;
                case "RETURN_FROM_CUSTOMER":
                    if (StringUtils.isEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toSiteId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "customerId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "FromSiteId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "fromLocatorId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "supplierId", "【API:instructionActualUpdate】"));

                    }
                    break;
                case "TRANSFER_OVER_LOCATOR":
                    if (StringUtils.isEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualUpdate】"));

                    }
                    break;
                case "TRANSFER_OVER_SITE":
                    if (StringUtils.isEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "supplierId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "customerId", "【API:instructionActualUpdate】"));

                    }
                    break;
                case "SENT_FROM_SITE":
                    if (StringUtils.isEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "toSiteId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "toLocatorId", "【API:instructionActualUpdate】"));

                    }
                    break;
                case "RECEIVE_TO_SITE":
                    if (StringUtils.isEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toSiteId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "fromSiteId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "fromLocatorId", "【API:instructionActualUpdate】"));

                    }
                    break;
                case "SHIP_TO_MISCELLANEOUS":
                    if (StringUtils.isEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "costCenterId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "toSiteId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "toLocatorId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "supplierId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "customerId", "【API:instructionActualUpdate】"));

                    }
                    break;
                case "RECEIVE_FROM_COSTCENTER":
                    if (StringUtils.isEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toSiteId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "costCenterId",
                                                        "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "FromSiteId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "fromLocatorId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "supplierId", "【API:instructionActualUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "customerId", "【API:instructionActualUpdate】"));

                    }
                    break;
                default:
                    break;
            }

            // 5.在表MT_INSTRUCTION_ACTUAL创建数据
            mtInstructionActual = new MtInstructionActual();
            BeanUtils.copyProperties(dto, mtInstructionActual);
            mtInstructionActual.setTenantId(tenantId);
            self().insertSelective(mtInstructionActual);

            // 记录历史，在MT_INSTRUCTION_ACTUAL_HIS中新增数据
            MtInstructionActualHis his = new MtInstructionActualHis();
            BeanUtils.copyProperties(mtInstructionActual, his);
            his.setEventId(dto.getEventId());
            his.setTenantId(tenantId);
            mtInstructionActualHisRepository.insertSelective(his);

            // 主表记录最新历史
            mtInstructionActual.setLatestHisId(his.getActualHisId());
            self().updateByPrimaryKeySelective(mtInstructionActual);

            // 返回实绩Id和历史Id
            result.setActualId(mtInstructionActual.getActualId());
            result.setActualHisId(his.getActualHisId());
        } else {
            if (null != dto.getMaterialId() && null == materialId) {
                throw new MtException("MT_INSTRUCTION_0040", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_INSTRUCTION_0040", "INSTRUCTION", "【API:instructionActualUpdate】"));
            }
            if (null != dto.getMaterialId() && null != materialId && !dto.getMaterialId().equals(materialId)) {
                throw new MtException("MT_INSTRUCTION_0040", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_INSTRUCTION_0040", "INSTRUCTION", "【API:instructionActualUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getEventId())) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "eventId", "【API:instructionActualUpdate】"));
            }
            mtInstructionActual.setActualQty(BigDecimal.valueOf(null == currentQty ? Double.valueOf(0.0D) : currentQty)
                            .add(BigDecimal.valueOf(dto.getActualQty())).doubleValue());
            mtInstructionActual.setTenantId(tenantId);

            MtInstructionActualHis his = new MtInstructionActualHis();
            BeanUtils.copyProperties(mtInstructionActual, his);
            his.setEventId(dto.getEventId());
            his.setTenantId(tenantId);
            mtInstructionActualHisRepository.insertSelective(his);

            // 主表记录最新历史
            mtInstructionActual.setLatestHisId(his.getActualHisId());
            self().updateByPrimaryKeySelective(mtInstructionActual);

            result.setActualId(mtInstructionActual.getActualId());
            result.setActualHisId(his.getActualHisId());
        }

        return result;
    }

    @Override
    public MtInstructionActual instructionActualPropertyGet(Long tenantId, String actualId) {
        // 1.参数合规性校验
        if (StringUtils.isEmpty(actualId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "actualId", "【API:instructionActualPropertyGet】"));

        }
        MtInstructionActual mtInstructionActual = new MtInstructionActual();
        mtInstructionActual.setActualId(actualId);
        mtInstructionActual.setTenantId(tenantId);
        return mtInstructionActualMapper.selectOne(mtInstructionActual);
    }

    @Override
    public List<MtInstructionActual> instructionLimitActualBatchGet(Long tenantId, List<String> instructionIdList) {
        if (CollectionUtils.isEmpty(instructionIdList)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionIdList",
                                            "【API:instructionLimitActualBatchGet】"));

        }
        return mtInstructionActualMapper.selectByInstructionIdList(tenantId, instructionIdList);
    }

    @Override
    public List<String> propertyLimitInstructionActualQuery(Long tenantId, MtInstructionActual dto) {
        dto.setTenantId(tenantId);
        return mtInstructionActualMapper.select(dto).stream().map(t -> t.getActualId()).collect(Collectors.toList());
    }

    @Override
    public List<MtInstructionActual> instructionLimitActualPropertyGet(Long tenantId, String instructionId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId", "【API:instructionLimitActualPropertyGet】"));
        }

        MtInstructionActual mtInstructionActual = new MtInstructionActual();
        mtInstructionActual.setInstructionId(instructionId);
        mtInstructionActual.setTenantId(tenantId);
        return mtInstructionActualMapper.select(mtInstructionActual);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void instructionActualAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "keyId", "【API:instructionActualAttrPropertyUpdate】"));
        }
        // 获取主表数据
        MtInstructionActual mtInstructionActual = new MtInstructionActual();
        mtInstructionActual.setTenantId(tenantId);
        mtInstructionActual.setActualId(dto.getKeyId());
        mtInstructionActual = mtInstructionActualMapper.selectOne(mtInstructionActual);
        if (mtInstructionActual == null || StringUtils.isEmpty(mtInstructionActual.getActualId())) {
            throw new MtException("MT_INSTRUCTION_0055",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0055",
                                            "INSTRUCTION", "keyId:" + dto.getKeyId(), "mt_instruction_actual",
                                            "【API:instructionActualAttrPropertyUpdate】"));
        }

        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_actual_attr", dto.getKeyId(),
                        dto.getEventId(), dto.getAttrs());
    }

    @Override
    public List<MtInstructionActualVO2> instructionLimitActualPropertyBatchGet(Long tenantId,
                    List<String> instructionIdList) {
        // 1.检验参数
        if (CollectionUtils.isEmpty(instructionIdList)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId ",
                                            "【API:instructionLimitActualPropertyBatchGet】"));
        }
        return mtInstructionActualMapper.selectByIdList(tenantId, instructionIdList);
    }

    @Override
    public List<MtInstructionActualVO3> instructionActualPropertyBatchGet(Long tenantId, List<String> actualIdList) {
        // 1.检验参数
        if (CollectionUtils.isEmpty(actualIdList)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "actualId", "【API:instructionActualPropertyBatchGet】"));
        }
        return mtInstructionActualMapper.selectByActualIdList(tenantId, actualIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtInstructionActualVO5> instructionActualBatchUpdate(Long tenantId, List<MtInstructionActualVO4> dtos) {
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(dtos)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "", "【API:instructionActualBatchUpdate】"));
        }

        // 验证eventId是否为空
        List<String> eventIds = dtos.stream().map(MtInstructionActualVO4::getEventId)
                        .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(eventIds) || eventIds.size() != dtos.size()) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "eventId", "【API:instructionActualBatchUpdate】"));
        }


        // 初始化全局参数
        List<MtInstructionActualVO5> resultList = new ArrayList<>();

        // 根据instructionId、actualId输入情况，分为三种
        // A. 有actualId的
        List<MtInstructionActualVO4> hasActualIdList =
                        dtos.stream().filter(t -> StringUtils.isNotEmpty(t.getActualId())).collect(Collectors.toList());

        // B. 没有actualId的，并且有instructionId的
        List<MtInstructionActualVO4> hasInstructionIdList = dtos.stream().filter(
                        t -> StringUtils.isEmpty(t.getActualId()) && StringUtils.isNotEmpty(t.getInstructionId()))
                        .collect(Collectors.toList());

        // C. 都没有
        List<MtInstructionActualVO4> bothEmptyList = dtos.stream()
                        .filter(t -> StringUtils.isEmpty(t.getActualId()) && StringUtils.isEmpty(t.getInstructionId()))
                        .collect(Collectors.toList());

        List<String> sqlList = new ArrayList<>();
        Date now = new Date();
        Long userId = DetailsHelper.getUserDetails().getUserId();

        // 处理 A. 有actualId的
        if (CollectionUtils.isNotEmpty(hasActualIdList)) {
            // 根据输入的actualId集合，批量执行更新
            List<String> actualIds = dtos.stream().map(MtInstructionActualVO4::getActualId)
                            .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());

            List<MtInstructionActual> mtInstructionActuals = mtInstructionActualMapper.selectByCondition(Condition
                            .builder(MtInstructionActual.class)
                            .andWhere(Sqls.custom().andEqualTo(MtInstructionActual.FIELD_TENANT_ID, tenantId)
                                            .andIn(MtInstructionActual.FIELD_ACTUAL_ID, actualIds))
                            .build());
            if (CollectionUtils.isEmpty(mtInstructionActuals) || mtInstructionActuals.size() != actualIds.size()) {
                List<String> actualIdList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(mtInstructionActuals)) {
                    actualIdList = mtInstructionActuals.stream().map(MtInstructionActual::getActualId)
                                    .collect(Collectors.toList());
                }

                actualIds.removeAll(actualIdList);
                throw new MtException("MT_INSTRUCTION_0062",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0062",
                                                "INSTRUCTION", actualIds.toString(),
                                                "【API:instructionActualBatchUpdate】"));
            }

            // 批量获取ID
            List<String> actualCids = customDbRepository.getNextKeys("mt_instruction_actual_cid_s", hasActualIdList.size());
            List<String> actualHisIds =
                            customDbRepository.getNextKeys("mt_instruction_actual_his_s", hasActualIdList.size());
            List<String> actualHisCids =
                            customDbRepository.getNextKeys("mt_instruction_actual_his_cid_s", hasActualIdList.size());

            if (CollectionUtils.isNotEmpty(mtInstructionActuals)) {
                Map<String, MtInstructionActual> actualMap =
                                mtInstructionActuals.stream().collect(Collectors.toMap(t -> t.getActualId(), t -> t));

                for (MtInstructionActualVO4 actualVO4 : hasActualIdList) {
                    // 前面有校验，一定会查到一条数据
                    MtInstructionActual mtInstructionActual = actualMap.get(actualVO4.getActualId());

                    String actualHisId = actualHisIds.remove(0);

                    // 计算更新后实绩数量
                    double actualQty = BigDecimal.valueOf(mtInstructionActual.getActualQty())
                                    .add(BigDecimal.valueOf(actualVO4.getActualQty())).doubleValue();
                    mtInstructionActual.setActualQty(actualQty);
                    // 主表记录最新历史
                    mtInstructionActual.setLatestHisId(actualHisId);
                    mtInstructionActual.setCid(Long.valueOf(actualCids.remove(0)));
                    mtInstructionActual.setLastUpdateDate(now);
                    mtInstructionActual.setLastUpdatedBy(userId);
                    sqlList.addAll(customDbRepository.getUpdateSql(mtInstructionActual));

                    // 记录历史，在MT_INSTRUCTION_ACTUAL_HIS中新增数据
                    MtInstructionActualHis mtInstructionActualHis = new MtInstructionActualHis();
                    mtInstructionActualHis.setActualId(mtInstructionActual.getActualId());
                    mtInstructionActualHis.setInstructionId(mtInstructionActual.getInstructionId());
                    mtInstructionActualHis.setInstructionType(mtInstructionActual.getInstructionType());
                    mtInstructionActualHis.setBusinessType(mtInstructionActual.getBusinessType());
                    mtInstructionActualHis.setMaterialId(mtInstructionActual.getMaterialId());
                    mtInstructionActualHis.setUomId(mtInstructionActual.getUomId());
                    mtInstructionActualHis.setEoId(mtInstructionActual.getEoId());
                    mtInstructionActualHis.setSourceOrderType(mtInstructionActual.getSourceOrderType());
                    mtInstructionActualHis.setSourceOrderId(mtInstructionActual.getSourceOrderId());
                    mtInstructionActualHis.setFromSiteId(mtInstructionActual.getFromSiteId());
                    mtInstructionActualHis.setToSiteId(mtInstructionActual.getToSiteId());
                    mtInstructionActualHis.setFromLocatorId(mtInstructionActual.getFromLocatorId());
                    mtInstructionActualHis.setToLocatorId(mtInstructionActual.getToLocatorId());
                    mtInstructionActualHis.setFromOwnerType(mtInstructionActual.getFromOwnerType());
                    mtInstructionActualHis.setToOwnerType(mtInstructionActual.getToOwnerType());
                    mtInstructionActualHis.setCostCenterId(mtInstructionActual.getCostCenterId());
                    mtInstructionActualHis.setSupplierId(mtInstructionActual.getSupplierId());
                    mtInstructionActualHis.setSupplierSiteId(mtInstructionActual.getSupplierSiteId());
                    mtInstructionActualHis.setCustomerId(mtInstructionActual.getCustomerId());
                    mtInstructionActualHis.setCustomerSiteId(mtInstructionActual.getCustomerSiteId());
                    mtInstructionActualHis.setActualQty(mtInstructionActual.getActualQty());

                    mtInstructionActualHis.setEventId(actualVO4.getEventId());
                    mtInstructionActualHis.setTenantId(tenantId);
                    mtInstructionActualHis.setActualHisId(actualHisId);
                    mtInstructionActualHis.setCid(Long.valueOf(actualHisCids.remove(0)));
                    sqlList.addAll(customDbRepository.getInsertSql(mtInstructionActualHis));

                    // 返回实绩Id和历史Id
                    MtInstructionActualVO5 result = new MtInstructionActualVO5();
                    result.setActualId(mtInstructionActualHis.getActualId());
                    result.setActualHisId(mtInstructionActualHis.getActualHisId());
                    result.setInstructionId(mtInstructionActualHis.getInstructionId());
                    resultList.add(result);
                }
            }
        }

        // 处理 B. 没有actualId的，并且有instructionId的
        if (CollectionUtils.isNotEmpty(hasInstructionIdList)) {
            // 新加校验逻辑，输入参数，instructionId、formLocatorId、toLocatorId维度不能重复输入
            Map<String, List<MtInstructionActualVO4>> uniqueMap = hasInstructionIdList.stream().collect(Collectors
                            .groupingBy(t -> t.getInstructionId() + t.getFromLocatorId() + t.getToLocatorId()));
            if (uniqueMap.size() < hasInstructionIdList.size()) {
                throw new MtException("MT_INSTRUCTION_0053", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_INSTRUCTION_0053", "INSTRUCTION", "【API:instructionActualBatchUpdate】"));
            }

            // 根据指令ID，批量获取实绩信息
            List<String> instructionIds = hasInstructionIdList.stream().map(MtInstructionActualVO4::getInstructionId)
                            .collect(Collectors.toList());
            List<MtInstructionActual> mtInstructionActuals = mtInstructionActualMapper.selectByCondition(Condition
                            .builder(MtInstructionActual.class)
                            .andWhere(Sqls.custom().andEqualTo(MtInstructionActual.FIELD_TENANT_ID, tenantId)
                                            .andIn(MtInstructionActual.FIELD_INSTRUCTION_ID, instructionIds))
                            .build());

            // 记录需要更新的数据
            List<MtInstructionActualVO4> needUpdateActualList = new ArrayList<>(hasInstructionIdList.size());

            // 记录更新数据对应的现有实绩数据
            Map<String, MtInstructionActual> updateActualMap = new HashMap<>();

            if (CollectionUtils.isNotEmpty(mtInstructionActuals)) {
                // instructionId - Map
                Map<String, List<MtInstructionActual>> instructionActualMap = mtInstructionActuals.stream()
                                .collect(Collectors.groupingBy(MtInstructionActual::getInstructionId));

                for (MtInstructionActualVO4 actualVO4 : hasInstructionIdList) {
                    List<MtInstructionActual> instructionActualList =
                                    instructionActualMap.get(actualVO4.getInstructionId());
                    if (CollectionUtils.isEmpty(instructionActualList)) {
                        // 根据指令ID查不到实绩，则新增，按 bothEmpty 处理
                        bothEmptyList.add(actualVO4);
                    } else {
                        if (actualVO4.getFromLocatorId() == null) {
                            actualVO4.setFromLocatorId("");
                        }
                        if (actualVO4.getToLocatorId() == null) {
                            actualVO4.setToLocatorId("");
                        }

                        // 匹配输入的 fromLocatorId 和 toLocatorId
                        Optional<MtInstructionActual> any = instructionActualList.stream()
                                        .filter(t -> actualVO4.getFromLocatorId().equals(t.getFromLocatorId())
                                                        && actualVO4.getToLocatorId().equals(t.getToLocatorId()))
                                        .findAny();
                        if (any.isPresent()) {
                            // 匹配的上，则更新
                            needUpdateActualList.add(actualVO4);
                            updateActualMap.put(actualVO4.getInstructionId(), any.get());
                        } else {
                            // 匹配不上，则新增
                            bothEmptyList.add(actualVO4);
                        }
                    }
                }

            } else {
                // 需要走新增逻辑，按 bothEmpty 处理
                bothEmptyList.addAll(hasInstructionIdList);
            }

            // 处理需要更新的数据
            if (CollectionUtils.isNotEmpty(needUpdateActualList)) {
                // 批量获取ID
                List<String> actualCids =
                                customDbRepository.getNextKeys("mt_instruction_actual_cid_s", needUpdateActualList.size());
                List<String> actualHisIds =
                                customDbRepository.getNextKeys("mt_instruction_actual_his_s", needUpdateActualList.size());
                List<String> actualHisCids = customDbRepository.getNextKeys("mt_instruction_actual_his_cid_s",
                                needUpdateActualList.size());

                for (MtInstructionActualVO4 actualVO4 : needUpdateActualList) {
                    MtInstructionActual mtInstructionActual = updateActualMap.get(actualVO4.getInstructionId());

                    String actualHisId = actualHisIds.remove(0);

                    // 计算更新后的实绩数量
                    double actualQty = BigDecimal.valueOf(mtInstructionActual.getActualQty())
                                    .add(BigDecimal.valueOf(actualVO4.getActualQty())).doubleValue();
                    mtInstructionActual.setActualQty(actualQty);

                    // 主表记录最新历史
                    mtInstructionActual.setLatestHisId(actualHisId);
                    mtInstructionActual.setCid(Long.valueOf(actualCids.remove(0)));
                    mtInstructionActual.setLastUpdateDate(now);
                    mtInstructionActual.setLastUpdatedBy(userId);
                    sqlList.addAll(customDbRepository.getUpdateSql(mtInstructionActual));

                    // 记录历史，在MT_INSTRUCTION_ACTUAL_HIS中新增数据
                    MtInstructionActualHis mtInstructionActualHis = new MtInstructionActualHis();
                    mtInstructionActualHis.setActualId(mtInstructionActual.getActualId());
                    mtInstructionActualHis.setInstructionId(mtInstructionActual.getInstructionId());
                    mtInstructionActualHis.setInstructionType(mtInstructionActual.getInstructionType());
                    mtInstructionActualHis.setBusinessType(mtInstructionActual.getBusinessType());
                    mtInstructionActualHis.setMaterialId(mtInstructionActual.getMaterialId());
                    mtInstructionActualHis.setUomId(mtInstructionActual.getUomId());
                    mtInstructionActualHis.setEoId(mtInstructionActual.getEoId());
                    mtInstructionActualHis.setSourceOrderType(mtInstructionActual.getSourceOrderType());
                    mtInstructionActualHis.setSourceOrderId(mtInstructionActual.getSourceOrderId());
                    mtInstructionActualHis.setFromSiteId(mtInstructionActual.getFromSiteId());
                    mtInstructionActualHis.setToSiteId(mtInstructionActual.getToSiteId());
                    mtInstructionActualHis.setFromLocatorId(mtInstructionActual.getFromLocatorId());
                    mtInstructionActualHis.setToLocatorId(mtInstructionActual.getToLocatorId());
                    mtInstructionActualHis.setFromOwnerType(mtInstructionActual.getFromOwnerType());
                    mtInstructionActualHis.setToOwnerType(mtInstructionActual.getToOwnerType());
                    mtInstructionActualHis.setCostCenterId(mtInstructionActual.getCostCenterId());
                    mtInstructionActualHis.setSupplierId(mtInstructionActual.getSupplierId());
                    mtInstructionActualHis.setSupplierSiteId(mtInstructionActual.getSupplierSiteId());
                    mtInstructionActualHis.setCustomerId(mtInstructionActual.getCustomerId());
                    mtInstructionActualHis.setCustomerSiteId(mtInstructionActual.getCustomerSiteId());
                    mtInstructionActualHis.setActualQty(mtInstructionActual.getActualQty());

                    mtInstructionActualHis.setEventId(actualVO4.getEventId());
                    mtInstructionActualHis.setTenantId(tenantId);
                    mtInstructionActualHis.setActualHisId(actualHisId);
                    mtInstructionActualHis.setCid(Long.valueOf(actualHisCids.remove(0)));
                    sqlList.addAll(customDbRepository.getInsertSql(mtInstructionActualHis));

                    // 返回实绩Id和历史Id
                    MtInstructionActualVO5 result = new MtInstructionActualVO5();
                    result.setActualId(mtInstructionActualHis.getActualId());
                    result.setActualHisId(mtInstructionActualHis.getActualHisId());
                    result.setInstructionId(mtInstructionActualHis.getInstructionId());
                    resultList.add(result);
                }
            }
        }

        // 处理 C. 都没有
        if (CollectionUtils.isNotEmpty(bothEmptyList)) {
            // 第三步，校验通用参数合规性
            this.checkInsertDateVerify(tenantId, bothEmptyList);

            List<String> instructionActualIds = customDbRepository.getNextKeys("mt_instruction_actual_s", dtos.size());
            List<String> instructionActualCids = customDbRepository.getNextKeys("mt_instruction_actual_cid_s", dtos.size());
            List<String> instructionActualHisIds =
                            customDbRepository.getNextKeys("mt_instruction_actual_his_s", dtos.size());
            List<String> instructionActualHisCids =
                            customDbRepository.getNextKeys("mt_instruction_actual_his_cid_s", dtos.size());

            for (MtInstructionActualVO4 dto : bothEmptyList) {

                // 第五步，进行更新，在表MT_INSTRUCTION_ACTUAL创建数据：(如无特殊说明，当输入参数为空时则对应字段也为空)
                String actualId = instructionActualIds.remove(0);
                String actualCid = instructionActualCids.remove(0);
                String actualHisId = instructionActualHisIds.remove(0);
                String actualHisCid = instructionActualHisCids.remove(0);

                MtInstructionActual mtInstructionActual = new MtInstructionActual();
                BeanUtils.copyProperties(dto, mtInstructionActual);
                mtInstructionActual.setTenantId(tenantId);
                mtInstructionActual.setActualId(actualId);
                mtInstructionActual.setCid(Long.valueOf(actualCid));
                mtInstructionActual.setCreatedBy(userId);
                mtInstructionActual.setCreationDate(now);
                mtInstructionActual.setLastUpdatedBy(userId);
                mtInstructionActual.setLastUpdateDate(now);
                // 主表记录最新历史
                mtInstructionActual.setLatestHisId(actualHisId);
                sqlList.addAll(customDbRepository.getInsertSql(mtInstructionActual));

                // 记录历史，在MT_INSTRUCTION_ACTUAL_HIS中新增数据
                MtInstructionActualHis mtInstructionActualHis = new MtInstructionActualHis();
                BeanUtils.copyProperties(mtInstructionActual, mtInstructionActualHis);
                mtInstructionActualHis.setEventId(dto.getEventId());
                mtInstructionActualHis.setTenantId(tenantId);
                mtInstructionActualHis.setActualHisId(actualHisId);
                mtInstructionActualHis.setCid(Long.valueOf(actualHisCid));
                mtInstructionActualHis.setEventId(dto.getEventId());
                sqlList.addAll(customDbRepository.getInsertSql(mtInstructionActualHis));

                // 返回实绩Id和历史Id
                MtInstructionActualVO5 result = new MtInstructionActualVO5();
                result.setActualId(actualId);
                result.setActualHisId(actualHisId);
                result.setInstructionId(dto.getInstructionId());
                resultList.add(result);
            }
        }

        // 批量执行
        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }

        return resultList;
    }

    /**
     * 服务于API：instructionActualBatchUpdate-指令实绩批量创建与更新 新增逻辑数据校验
     *
     * @author chuang.yang
     * @date 2020/1/17
     * @param tenantId
     * @param dtos
     * @return void
     */
    private void checkInsertDateVerify(Long tenantId, List<MtInstructionActualVO4> dtos) {
        // 第三步，校验通用参数合规性
        for (MtInstructionActualVO4 dto : dtos) {
            // 1.检验通用参数的合规性
            if (StringUtils.isEmpty(dto.getInstructionType())) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "instructionType",
                                                "【API:instructionActualBatchUpdate】"));
            }
            if (dto.getActualQty() == null) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "actualQty", "【API:instructionActualBatchUpdate】"));

            }
            if (StringUtils.isEmpty(dto.getEventId())) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "eventId", "【API:instructionActualBatchUpdate】"));

            }

            // 1.a materialId、eoId不能同时为空
            if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getEoId())) {
                throw new MtException("MT_INSTRUCTION_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                                "INSTRUCTION", "materialId", "eoId",
                                                "【API:instructionActualBatchUpdate】"));

            }

            // 1.c materialId、uomId只能同时为空或者同时不为空
            if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getUomId())
                            || StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getUomId())) {
                throw new MtException("MT_INSTRUCTION_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0007",
                                                "INSTRUCTION", "materialId", "uomId",
                                                "【API:instructionActualBatchUpdate】"));

            }
            // 1.d sourceOrderType、sourceOrderId只能同时为空或者同时不为空
            if (StringUtils.isEmpty(dto.getSourceOrderType()) && StringUtils.isNotEmpty(dto.getSourceOrderId())
                            || StringUtils.isNotEmpty(dto.getSourceOrderType())
                                            && StringUtils.isEmpty(dto.getSourceOrderId())) {
                throw new MtException("MT_INSTRUCTION_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0007",
                                                "INSTRUCTION", "sourceOrderType", "sourceOrderId",
                                                "【API:instructionActualBatchUpdate】"));

            }

            // 1.e验toSiteId与fromSiteId不能同时为空
            if (StringUtils.isEmpty(dto.getToSiteId()) && StringUtils.isEmpty(dto.getFromSiteId())) {
                throw new MtException("MT_INSTRUCTION_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                                "INSTRUCTION", "toSiteId", "fromSiteId",
                                                "【API:instructionActualBatchUpdate】"));

            }

            // 1.f校验fromLocatorId与toLocatorId不能同时为空
            if (StringUtils.isEmpty(dto.getToLocatorId()) && StringUtils.isEmpty(dto.getFromLocatorId())) {
                throw new MtException("MT_INSTRUCTION_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                                "INSTRUCTION", "toLocatorId", "fromLocatorId",
                                                "【API:instructionActualBatchUpdate】"));

            }

            // 1.g supplierId、supplierSiteId只能同时为空或者同时不为空
            if (StringUtils.isEmpty(dto.getSupplierId()) && StringUtils.isNotEmpty(dto.getSupplierSiteId())) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "supplierId", "【API:instructionActualBatchUpdate】"));

            }

            // 1.h customerSiteId、customerId只能同时为空或者同时不为空
            if (StringUtils.isEmpty(dto.getCustomerSiteId()) && StringUtils.isNotEmpty(dto.getCustomerId())
                            || StringUtils.isNotEmpty(dto.getCustomerSiteId())
                                            && StringUtils.isEmpty(dto.getCustomerId())) {
                throw new MtException("MT_INSTRUCTION_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0007",
                                                "INSTRUCTION", "customerSiteId", "customerId",
                                                "【API:instructionActualBatchUpdate】"));

            }

            // 1.i调用API{groupLimitTypeQuery}
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("INSTRUCTION");
            mtGenTypeVO2.setTypeGroup("INSTRUCTION_MOVE_TYPE");
            List<MtGenType> instructionTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
            if (CollectionUtils.isEmpty(instructionTypes)) {
                return;
            }

            List<String> list = instructionTypes.stream().map(t -> t.getTypeCode()).collect(Collectors.toList());
            if (!list.contains(dto.getInstructionType())) {
                throw new MtException("MT_INSTRUCTION_0004",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0004",
                                                "INSTRUCTION", dto.getInstructionType(), list.toString(),
                                                "【API:instructionActualBatchUpdate】"));

            }

            switch (dto.getInstructionType()) {
                case "RECEIVE_FROM_SUPPLIER":
                    if (StringUtils.isEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "supplierId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toSiteId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }

                    if (StringUtils.isNotEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "fromSiteId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "fromLocatorId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "customerId", "【API:instructionActualBatchUpdate】"));

                    }
                    break;
                case "RETURN_TO_SUPPLIER":
                    if (StringUtils.isEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "supplierId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "toSiteId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "ToLocatorId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "customerId", "【API:instructionActualBatchUpdate】"));

                    }
                    break;
                case "SHIP_TO_CUSTOMER":
                    if (StringUtils.isEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "customerId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "toSiteId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "toLocatorId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "supplierId", "【API:instructionActualBatchUpdate】"));

                    }
                    break;
                case "RETURN_FROM_CUSTOMER":
                    if (StringUtils.isEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toSiteId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "customerId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "FromSiteId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "fromLocatorId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "supplierId", "【API:instructionActualBatchUpdate】"));

                    }
                    break;
                case "TRANSFER_OVER_LOCATOR":
                    if (StringUtils.isEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualBatchUpdate】"));

                    }
                    break;
                case "TRANSFER_OVER_SITE":
                    if (StringUtils.isEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "supplierId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "customerId", "【API:instructionActualBatchUpdate】"));

                    }
                    break;
                case "SENT_FROM_SITE":
                    if (StringUtils.isEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "toSiteId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "toLocatorId", "【API:instructionActualBatchUpdate】"));

                    }
                    break;
                case "RECEIVE_TO_SITE":
                    if (StringUtils.isEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toSiteId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "costCenterId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "fromSiteId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "fromLocatorId", "【API:instructionActualBatchUpdate】"));

                    }
                    break;
                case "SHIP_TO_MISCELLANEOUS":
                    if (StringUtils.isEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "costCenterId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "toSiteId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "toLocatorId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "supplierId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "customerId", "【API:instructionActualBatchUpdate】"));

                    }
                    break;
                case "RECEIVE_FROM_COSTCENTER":
                    if (StringUtils.isEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toSiteId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "costCenterId",
                                                        "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "FromSiteId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "fromLocatorId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "supplierId", "【API:instructionActualBatchUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", dto.getInstructionType(),
                                                        "customerId", "【API:instructionActualBatchUpdate】"));

                    }
                    break;
                default:
                    break;
            }
        }
    }
}
