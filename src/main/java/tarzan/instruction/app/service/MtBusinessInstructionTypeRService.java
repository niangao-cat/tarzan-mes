package tarzan.instruction.app.service;

import tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO;
import tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO2;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 业务类型与指令移动类型关系表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
public interface MtBusinessInstructionTypeRService {

    /**
     * 业务指令类型查询
     * @Author peng.yuan
     * @Date 2020/1/3 16:50
     * @param tenantId :
     * @param dto :
     * @param pageRequest :
     * @return io.choerodon.core.domain.Page<tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO2>
     */
    Page<MtBusinessInstructionTypeRVO2> propertyLimitBusinessInstructionTypeRelQueryForUi(Long tenantId, MtBusinessInstructionTypeRVO dto, PageRequest pageRequest);

    /**
     * 保存或更新业务与指令类型
     * @Author peng.yuan
     * @Date 2020/1/3 16:50
     * @param tenantId :
     * @param dto :
     * @return java.lang.String
     */
    String businessInstructionTypeRelUpdateForUi(Long tenantId, MtBusinessInstructionTypeRVO dto);
}
