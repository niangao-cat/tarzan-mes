package tarzan.instruction.infra.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.infra.mapper.MtInstructionActualDetailMapper;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDetail;
import tarzan.instruction.domain.repository.MtInstructionDetailRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.*;
import tarzan.instruction.infra.mapper.MtInstructionDetailMapper;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;

/**
 * 指令明细行 资源库实现
 *
 * @author yiyang.xie@hand-china.com 2019-10-16 10:19:53
 */
@Component
public class MtInstructionDetailRepositoryImpl extends BaseRepositoryImpl<MtInstructionDetail>
                implements MtInstructionDetailRepository {

    @Autowired
    private MtInstructionDetailMapper mtInstructionDetailMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;

    @Autowired
    private MtInstructionActualDetailMapper mtInstructionActualDetailMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<MtInstructionDetailVO1> propertyLimitInstructionDetailQuery(Long tenantId, MtInstructionDetailVO dto) {
        return mtInstructionDetailMapper.selectCondition(tenantId, dto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> instructionDetailCreate(Long tenantId, MtInstructionDetailVO2 dto) {
        List<String> result = new ArrayList<>();
        // 输入校验
        if (StringUtils.isEmpty(dto.getInstructionId())) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API:instructionDetailCreate】"));
        }
        if (CollectionUtils.isEmpty(dto.getMaterialLotIdList())) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "materialLotId", "【API:instructionDetailCreate】"));
        }
        List<MtInstructionDetail> details = mtInstructionDetailMapper.selectByMaterialLods(tenantId, dto);
        if (CollectionUtils.isNotEmpty(details)) {
            throw new MtException("MT_INSTRUCTION_0038",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0038",
                                            "INSTRUCTION", "mt_instruction_detail", "instructionId,materialLotId",
                                            "【API:instructionDetailCreate】"));
        }
        List<String> mtMaterialLots =
                        mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, dto.getMaterialLotIdList())
                                        .stream().map(MtMaterialLot::getMaterialLotCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(mtMaterialLots)) {
            throw new MtException("MT_INSTRUCTION_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0006", "INSTRUCTION", "materialLotId", "【API:instructionDetailCreate】"));
        }
        dto.getMaterialLotIdList().forEach(id -> {
            MtInstructionDetail detail = new MtInstructionDetail();
            detail.setTenantId(tenantId);
            detail.setInstructionId(dto.getInstructionId());
            detail.setMaterialLotId(id);
            self().insertSelective(detail);
            result.add(detail.getInstructionDetailId());
        });
        return result;
    }

    @Override
    public List<MtInstructionDetailVO4> propertyLimitInstructionDetailBatchQuery(Long tenantId,
                    List<MtInstructionDetailVO3> dtos) {
        List<MtInstructionDetailVO4> resultList = new ArrayList<>();

        // 从数据库中查询数据
        for (MtInstructionDetailVO3 dto : dtos) {
            List<MtInstructionDetailVO4> detailVO4s = mtInstructionDetailMapper.selectByMyCondition(tenantId, dto);
            if (CollectionUtils.isNotEmpty(detailVO4s)) {
                resultList.addAll(detailVO4s);
            }
        }

        return resultList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> sourceInstructionLimitInstructionDetailCreate(Long tenantId, String instructionId) {
        List<String> result = new ArrayList<>();
        // 第一步，判断输入参数是否合规
        // 判断输入参数instructionId不能为空
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId",
                                            "【API:sourceInstructionLimitInstructionDetailCreate】"));
        }

        // 第二步，根据输入参数instructionId获取sourceInstructionId
        // 在表MT_INSTRUCTION中限定INSTRUCTION_ID=instructionId
        MtInstruction mtInstruction = mtInstructionRepository.instructionPropertyGet(tenantId, instructionId);
        if (mtInstruction == null || StringUtils.isEmpty(mtInstruction.getSourceInstructionId())) {
            throw new MtException("MT_INSTRUCTION_0061",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0061",
                                            "INSTRUCTION", "instructionId",
                                            "【API:sourceInstructionLimitInstructionDetailCreate】"));
        }

        // 第三步，根据第二步2-a获取到的sourceInstructionId获取指令实绩ID
        MtInstructionActual mtInstructionActual = new MtInstructionActual();
        mtInstructionActual.setInstructionId(mtInstruction.getSourceInstructionId());
        List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
                        mtInstructionActual);
        if (CollectionUtils.isEmpty(actualIdList)) {
            throw new MtException("MT_INSTRUCTION_0062",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0062",
                                            "INSTRUCTION", "sourceInstructionId",
                                            "【API:sourceInstructionLimitInstructionDetailCreate】"));
        }

        // 4.第四步，根据第三步2-a获取到的instructionActualId列表获取物料批
        List<String> materialLotIds =
                        mtInstructionActualDetailMapper.selectMaterialLotIdByActualId(tenantId, actualIdList);

        if (CollectionUtils.isEmpty(materialLotIds) || materialLotIds.stream().anyMatch(StringUtils::isEmpty)) {
            throw new MtException("MT_INSTRUCTION_0063",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0063",
                                            "INSTRUCTION", "sourceInstructionId",
                                            "【API:sourceInstructionLimitInstructionDetailCreate】"));
        }

        // 5.第五步，生成指令明细
        List<String> sqlList = new ArrayList<>();
        List<String> detailIds = customDbRepository.getNextKeys("mt_instruction_detail_s", materialLotIds.size());
        List<String> detailCIds = customDbRepository.getNextKeys("mt_instruction_detail_cid_s", materialLotIds.size());

        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date(System.currentTimeMillis());
        int num = 0;
        for (String materialLotId : materialLotIds) {
            MtInstructionDetail detail = new MtInstructionDetail();
            detail.setTenantId(tenantId);
            detail.setInstructionDetailId(detailIds.get(num));
            detail.setInstructionId(instructionId);
            detail.setMaterialLotId(materialLotId);
            detail.setCid(Long.valueOf(detailCIds.get(num)));
            detail.setCreationDate(now);
            detail.setCreatedBy(userId);
            detail.setLastUpdateDate(now);
            detail.setLastUpdatedBy(userId);
            sqlList.addAll(customDbRepository.getInsertSql(detail));
            result.add(detail.getInstructionDetailId());
            num++;

        }


        if (CollectionUtils.isNotEmpty(sqlList)) {
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return result;
    }
}
