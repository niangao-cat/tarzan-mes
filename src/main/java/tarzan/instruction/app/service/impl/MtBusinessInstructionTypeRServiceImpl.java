package tarzan.instruction.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.vo.MtGenTypeVO7;
import io.tarzan.common.domain.vo.MtGenTypeVO8;
import tarzan.instruction.app.service.MtBusinessInstructionTypeRService;
import tarzan.instruction.domain.entity.MtBusinessInstructionTypeR;
import tarzan.instruction.domain.repository.MtBusinessInstructionTypeRRepository;
import tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO;
import tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO2;
import tarzan.instruction.infra.mapper.MtBusinessInstructionTypeRMapper;

/**
 * 业务类型与指令移动类型关系表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@Service
public class MtBusinessInstructionTypeRServiceImpl implements MtBusinessInstructionTypeRService {

    @Autowired
    private MtBusinessInstructionTypeRRepository mtBusinessInstructionTypeRRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtBusinessInstructionTypeRMapper mtBusinessInstructionTypeRMapper;

    @Override
    public Page<MtBusinessInstructionTypeRVO2> propertyLimitBusinessInstructionTypeRelQueryForUi(Long tenantId,
                    MtBusinessInstructionTypeRVO dto, PageRequest pageRequest) {

        List<MtBusinessInstructionTypeRVO2> resultList = new ArrayList<>();

        MtBusinessInstructionTypeR typeR = new MtBusinessInstructionTypeR();
        typeR.setTenantId(tenantId);
        typeR.setRelationId(dto.getRelationId());
        typeR.setBussinessType(dto.getBusinessType());
        typeR.setInstructionType(dto.getInstructionType());
        typeR.setEnableFlag(dto.getEnableFlag());

        //转换为page对象
        PageHelper.startPage(pageRequest.getPage(),pageRequest.getSize());
        Page<MtBusinessInstructionTypeR> instructionTypeRList = (Page<MtBusinessInstructionTypeR>) mtBusinessInstructionTypeRMapper.select(typeR);

        Page<MtBusinessInstructionTypeRVO2> page = new Page<>();
        if (CollectionUtils.isEmpty(instructionTypeRList.getContent())) {
            return page;
        }

        for (MtBusinessInstructionTypeR instructionTypeR : instructionTypeRList) {
            MtBusinessInstructionTypeRVO2 resultVO = new MtBusinessInstructionTypeRVO2();

            if (StringUtils.isNotEmpty(instructionTypeR.getBusinessType())) {
                MtGenTypeVO7 typeVO7 = new MtGenTypeVO7();
                typeVO7.setTypeGroup("INSTRUCTION_BUSINESS_TYPE");
                typeVO7.setTypeCode(instructionTypeR.getBusinessType());
                // 上面两个条件是唯一约束
                List<MtGenTypeVO8> mtGenTypeVO8s = mtGenTypeRepository.propertyLimitGenTypePropertyQuery(tenantId, typeVO7);
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
                List<MtGenTypeVO8> mtGenTypeVO8s = mtGenTypeRepository.propertyLimitGenTypePropertyQuery(tenantId, typeVO7);
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

        //构造返回的page对象
        page.setContent(resultList);
        page.setNumber(instructionTypeRList.getNumber());
        page.setSize(instructionTypeRList.getSize());
        page.setTotalElements(instructionTypeRList.getTotalElements());
        page.setTotalPages(instructionTypeRList.getTotalPages());
        page.setNumberOfElements(instructionTypeRList.getNumberOfElements());

        return page;
    }

    @Override
    public String businessInstructionTypeRelUpdateForUi(Long tenantId, MtBusinessInstructionTypeRVO dto) {

        return mtBusinessInstructionTypeRRepository.businessInstructionTypeRelUpdate(tenantId, dto);
    }
}
