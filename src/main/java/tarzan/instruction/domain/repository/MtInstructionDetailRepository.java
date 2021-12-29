package tarzan.instruction.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.instruction.domain.entity.MtInstructionDetail;
import tarzan.instruction.domain.vo.*;

/**
 * 指令明细行资源库
 *
 * @author yiyang.xie@hand-china.com 2019-10-16 10:19:53
 */
public interface MtInstructionDetailRepository
                extends BaseRepository<MtInstructionDetail>, AopProxy<MtInstructionDetailRepository> {

    /**
     * 根据属性获取指令明细行
     * 
     * @Author peng.yuan
     * @Date 2019/10/18 14:46
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.instruction.domain.vo.MtInstructionDetailVO1>
     */
    List<MtInstructionDetailVO1> propertyLimitInstructionDetailQuery(Long tenantId, MtInstructionDetailVO dto);

    /**
     * instructionDetailCreate-指令明细行创建
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> instructionDetailCreate(Long tenantId, MtInstructionDetailVO2 dto);

    /**
     * 根据属性批量获取指令明细行
     *
     * @Author peng.yuan
     * @Date 2020/1/8 10:49
     * @param tenantId :
     * @param mtInstructionDetailVO3s :
     * @return java.util.List<tarzan.instruction.domain.vo.MtInstructionDetailVO4>
     */
    List<MtInstructionDetailVO4> propertyLimitInstructionDetailBatchQuery(Long tenantId,
                                                                          List<MtInstructionDetailVO3> mtInstructionDetailVO3s);

    /**
     * sourceInstructionLimitInstructionDetailCreate-根据来源指令实绩明细创建指令明细
     * 
     * @param tenantId
     * @param instructionId
     * @return
     */
    List<String> sourceInstructionLimitInstructionDetailCreate(Long tenantId, String instructionId);
}
