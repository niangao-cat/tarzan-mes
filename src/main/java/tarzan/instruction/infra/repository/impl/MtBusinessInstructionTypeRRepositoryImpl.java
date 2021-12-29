package tarzan.instruction.infra.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenTypeVO7;
import io.tarzan.common.domain.vo.MtGenTypeVO8;
import tarzan.instruction.domain.entity.MtBusinessInstructionTypeR;
import tarzan.instruction.domain.repository.MtBusinessInstructionTypeRRepository;
import tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO;
import tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO2;
import tarzan.instruction.infra.mapper.MtBusinessInstructionTypeRMapper;

/**
 * 业务类型与指令移动类型关系表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@Component
public class MtBusinessInstructionTypeRRepositoryImpl extends BaseRepositoryImpl<MtBusinessInstructionTypeR>
                implements MtBusinessInstructionTypeRRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtBusinessInstructionTypeRMapper mtBusinessInstructionTypeRMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String businessInstructionTypeRelUpdate(Long tenantId, MtBusinessInstructionTypeRVO dto) {
        String businessType = dto.getBusinessType();
        String instructionType = dto.getInstructionType();
        String enableFlag = dto.getEnableFlag();
        String relationId = dto.getRelationId();

        // 第一步判断输入参数是否合规
        if (StringUtils.isNotEmpty(businessType)) {
            MtGenTypeVO7 typeVO7 = new MtGenTypeVO7();
            typeVO7.setTypeGroup("INSTRUCTION_BUSINESS_TYPE");
            typeVO7.setTypeCode(businessType);
            List<MtGenTypeVO8> mtGenTypeVO8s = mtGenTypeRepository.propertyLimitGenTypePropertyQuery(tenantId, typeVO7);
            if (CollectionUtils.isEmpty(mtGenTypeVO8s)) {
                throw new MtException("MT_INSTRUCTION_0006",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                                "INSTRUCTION", "businessType",
                                                "【API:businessInstructionTypeRelUpdate】"));
            }
        }

        if (StringUtils.isNotEmpty(instructionType)) {
            MtGenTypeVO7 typeVO7 = new MtGenTypeVO7();
            typeVO7.setTypeGroup("INSTRUCTION_MOVE_TYPE");
            typeVO7.setTypeCode(instructionType);
            List<MtGenTypeVO8> mtGenTypeVO8s = mtGenTypeRepository.propertyLimitGenTypePropertyQuery(tenantId, typeVO7);
            if (CollectionUtils.isEmpty(mtGenTypeVO8s)) {
                throw new MtException("MT_INSTRUCTION_0006",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                                "INSTRUCTION", "instructionType",
                                                "【API:businessInstructionTypeRelUpdate】"));
            }
        }

        if (StringUtils.isNotEmpty(enableFlag) && !Arrays.asList("Y", "N").contains(enableFlag)) {
            throw new MtException("MT_INSTRUCTION_0059",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0059",
                                            "INSTRUCTION", "enableFlag", "【API:businessInstructionTypeRelUpdate】"));
        }

        // 第二步根据输入参数判断为需新增数据或是更新数据
        MtBusinessInstructionTypeR instructionTypeR;
        if (StringUtils.isNotEmpty(relationId)) {
            MtBusinessInstructionTypeR typeR = new MtBusinessInstructionTypeR();
            typeR.setTenantId(tenantId);
            typeR.setRelationId(relationId);
            instructionTypeR = mtBusinessInstructionTypeRMapper.selectOne(typeR);
            if (null == instructionTypeR) {
                throw new MtException("MT_INSTRUCTION_0006",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                                "INSTRUCTION", "relationId",
                                                "【API:businessInstructionTypeRelUpdate】"));
            }
        } else {
            // 若businessType输入为空或未输入，判定为新增模式,输入则限定查询
            if (StringUtils.isNotEmpty(businessType)) {
                MtBusinessInstructionTypeR typeR = new MtBusinessInstructionTypeR();
                typeR.setTenantId(tenantId);
                typeR.setBussinessType(businessType);
                instructionTypeR = mtBusinessInstructionTypeRMapper.selectOne(typeR);
            } else {
                throw new MtException("MT_INSTRUCTION_0006",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                                "INSTRUCTION", "businessType",
                                                "【API:businessInstructionTypeRelUpdate】"));
            }
        }

        // 若获取到数据，判定为更新模式，继续第三步
        if (null != instructionTypeR) {
            if (StringUtils.isNotEmpty(businessType)) {
                instructionTypeR.setBussinessType(businessType);
            }
            if (StringUtils.isNotEmpty(instructionType)) {
                instructionTypeR.setInstructionType(instructionType);
            }
            if (StringUtils.isNotEmpty(enableFlag)) {
                instructionTypeR.setEnableFlag(enableFlag);
            }
            self().updateByPrimaryKey(instructionTypeR);

            relationId = instructionTypeR.getRelationId();

        } else {
            // 第四步，新增
            if (StringUtils.isEmpty(instructionType) || StringUtils.isEmpty(enableFlag)) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "businessType、instructionType、enableFlag",
                                                "【API:businessInstructionTypeRelUpdate】"));
            }

            // 判断businessType是否存在
            MtBusinessInstructionTypeR typeR = new MtBusinessInstructionTypeR();
            typeR.setTenantId(tenantId);
            typeR.setBussinessType(businessType);
            instructionTypeR = mtBusinessInstructionTypeRMapper.selectOne(typeR);
            if (null != instructionTypeR) {
                throw new MtException("MT_INSTRUCTION_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0002",
                                                "INSTRUCTION", "businessType",
                                                "【API:businessInstructionTypeRelUpdate】"));
            }

            typeR.setTenantId(tenantId);
            typeR.setBussinessType(businessType);
            typeR.setInstructionType(instructionType);
            typeR.setEnableFlag(enableFlag);
            self().insertSelective(typeR);
            relationId = typeR.getRelationId();

        }

        return relationId;
    }

    @Override
    public List<MtBusinessInstructionTypeRVO> businessInstructionTypeRelPropertyBatchGet(Long tenantId,
                    List<String> relationIds) {

        if (CollectionUtils.isEmpty(relationIds)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "relationId",
                                            "【API:businessInstructionTypeRelPropertyBatchGet】"));
        }

        return mtBusinessInstructionTypeRMapper.selectByIdList(tenantId, relationIds);
    }

    @Override
    public List<MtBusinessInstructionTypeRVO2> propertyLimitBusinessInstructionTypeRelQuery(Long tenantId,
                    MtBusinessInstructionTypeRVO dto) {
        List<MtBusinessInstructionTypeRVO2> resultList = new ArrayList<>();

        MtBusinessInstructionTypeR typeR = new MtBusinessInstructionTypeR();
        typeR.setTenantId(tenantId);
        typeR.setRelationId(dto.getRelationId());
        typeR.setBussinessType(dto.getBusinessType());
        typeR.setInstructionType(dto.getInstructionType());
        typeR.setEnableFlag(dto.getEnableFlag());
        List<MtBusinessInstructionTypeR> instructionTypeRList = mtBusinessInstructionTypeRMapper.select(typeR);

        if (CollectionUtils.isEmpty(instructionTypeRList)) {
            return Collections.emptyList();
        }

        for (MtBusinessInstructionTypeR instructionTypeR : instructionTypeRList) {
            MtBusinessInstructionTypeRVO2 resultVO = new MtBusinessInstructionTypeRVO2();

            if (StringUtils.isNotEmpty(instructionTypeR.getBusinessType())) {
                MtGenTypeVO7 typeVO7 = new MtGenTypeVO7();
                typeVO7.setTypeGroup("INSTRUCTION_BUSINESS_TYPE");
                typeVO7.setTypeCode(instructionTypeR.getBusinessType());
                // 上面两个条件是唯一约束
                List<MtGenTypeVO8> mtGenTypeVO8s =
                                mtGenTypeRepository.propertyLimitGenTypePropertyQuery(tenantId, typeVO7);
                if (CollectionUtils.isNotEmpty(mtGenTypeVO8s)) {
                    MtGenTypeVO8 mtGenTypeVO8 = mtGenTypeVO8s.get(0);
                    resultVO.setBusinessTypeDesc(mtGenTypeVO8.getDescription());
                }
            }

            if (StringUtils.isNotEmpty(instructionTypeR.getInstructionType())) {
                MtGenTypeVO7 typeVO7 = new MtGenTypeVO7();
                typeVO7.setTypeGroup("INSTRUCTION_MOVE_TYPE");
                typeVO7.setTypeCode(instructionTypeR.getInstructionType());
                // 上面两个条件是唯一约束
                List<MtGenTypeVO8> mtGenTypeVO8s =
                                mtGenTypeRepository.propertyLimitGenTypePropertyQuery(tenantId, typeVO7);
                if (CollectionUtils.isNotEmpty(mtGenTypeVO8s)) {
                    MtGenTypeVO8 mtGenTypeVO8 = mtGenTypeVO8s.get(0);
                    resultVO.setInstructionTypeDesc(mtGenTypeVO8.getDescription());
                }
            }

            resultVO.setRelationId(instructionTypeR.getRelationId());
            resultVO.setBusinessType(instructionTypeR.getBusinessType());
            resultVO.setInstructionType(instructionTypeR.getInstructionType());
            resultVO.setEnableFlag(instructionTypeR.getEnableFlag());

            resultList.add(resultVO);
        }

        return resultList;
    }
}
