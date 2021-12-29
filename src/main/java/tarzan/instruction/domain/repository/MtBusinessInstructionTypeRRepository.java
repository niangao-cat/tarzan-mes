package tarzan.instruction.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.instruction.domain.entity.MtBusinessInstructionTypeR;
import tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO;
import tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO2;

/**
 * 业务类型与指令移动类型关系表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
public interface MtBusinessInstructionTypeRRepository
                extends BaseRepository<MtBusinessInstructionTypeR>, AopProxy<MtBusinessInstructionTypeRRepository> {


    /**
     * 业务类型和移动类型关系更新&新增
     * 
     * @Author peng.yuan
     * @Date 2020/1/3 10:56
     * @param tenantId :
     * @param mtBusinessInstructionTypeRVO :
     * @return java.lang.String
     */
    String businessInstructionTypeRelUpdate(Long tenantId, MtBusinessInstructionTypeRVO mtBusinessInstructionTypeRVO);

    /**
     * 批量获取业务类型和移动类型关系属性
     * 
     * @Author peng.yuan
     * @Date 2020/1/3 11:40
     * @param tenantId :
     * @param relationIds :
     * @return java.util.List<tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO>
     */
    List<MtBusinessInstructionTypeRVO> businessInstructionTypeRelPropertyBatchGet(Long tenantId,
                                                                                  List<String> relationIds);


    /**
     * 根据属性限制获取业务类型和移动类型关系
     * 
     * @Author peng.yuan
     * @Date 2020/1/3 14:17
     * @param tenantId :
     * @param mtBusinessInstructionTypeRVO :
     * @return tarzan.instruction.domain.vo.MtBusinessInstructionTypeRVO2
     */
    List<MtBusinessInstructionTypeRVO2> propertyLimitBusinessInstructionTypeRelQuery(Long tenantId,
                                                                                     MtBusinessInstructionTypeRVO mtBusinessInstructionTypeRVO);

}
