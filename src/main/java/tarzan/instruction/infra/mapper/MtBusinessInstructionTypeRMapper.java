package tarzan.instruction.infra.mapper;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

import org.apache.ibatis.annotations.Param;
import tarzan.instruction.domain.entity.MtBusinessInstructionTypeR;
import tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO;

/**
 * 业务类型与指令移动类型关系表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
public interface MtBusinessInstructionTypeRMapper extends BaseMapper<MtBusinessInstructionTypeR> {

    /**
     * 根据id批量查询
     * 
     * @Author peng.yuan
     * @Date 2020/1/3 14:00
     * @param tenantId :
     * @param relationIds :
     * @return java.util.List<tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO>
     */
    List<MtBusinessInstructionTypeRVO> selectByIdList(@Param(value = "tenantId") Long tenantId, @Param(value = "relationIds") List<String> relationIds);

}
