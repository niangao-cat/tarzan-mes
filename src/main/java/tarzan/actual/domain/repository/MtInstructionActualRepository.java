package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.vo.*;

/**
 * 指令实绩表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtInstructionActualRepository
                extends BaseRepository<MtInstructionActual>, AopProxy<MtInstructionActualRepository> {

    /**
     * instructionActualUpdate-指令实绩创建
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/6/19
     */
    MtInstructionActualVO1 instructionActualUpdate(Long tenantId, MtInstructionActualVO dto);

    /**
     * instructionActualPropertyGet-根据指令实绩Id获取指令实绩
     *
     * @param tenantId
     * @param actualId
     * @author guichuan.li
     * @date 2019/6/21
     */
    MtInstructionActual instructionActualPropertyGet(Long tenantId, String actualId);

    /**
     * instructionLimitActualBatchQuery-根据指定指令获取批量明细实绩
     * 
     * @author benjamin
     * @date 2019-07-08 20:14
     * @param tenantId tenantId
     * @param instructionIdList 指令Id集合
     * @return List
     */
    List<MtInstructionActual> instructionLimitActualBatchGet(Long tenantId, List<String> instructionIdList);

    /**
     * propertyLimitInstructionActualQuery-根据属性获取指令实绩ID
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/6/21
     */
    List<String> propertyLimitInstructionActualQuery(Long tenantId, MtInstructionActual dto);

    /**
     * instructionLimitActualPropertyGet-根据指令ID获取指令
     *
     * @param tenantId
     * @param instructionId
     * @author guichuan.li
     * @date 2019/6/21
     */
    List<MtInstructionActual> instructionLimitActualPropertyGet(Long tenantId, String instructionId);

    /**
     * instructionActualAttrPropertyUpdate-指令实绩新增&更新扩展表属性
     *
     * @Author Xie.yiyang
     * @Date 2019/11/19 16:47
     * @param tenantId
     * @param dto
     * @return void
     */
    void instructionActualAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);

    /**
     * instructionLimitActualPropertyBatchGet-根据指令ID批量获取指令实绩属性
     * 
     * @Author peng.yuan
     * @Date 2020/1/7 15:08
     * @param tenantId :
     * @param instructionIdList :
     * @return java.util.List<tarzan.actual.domain.vo.MtInstructionActualVO2>
     */
    List<MtInstructionActualVO2> instructionLimitActualPropertyBatchGet(Long tenantId, List<String> instructionIdList);

    /**
     * instructionActualPropertyBatchGet-根据指令实绩Id批量获取指令实绩
     * 
     * @Author peng.yuan
     * @Date 2020/1/8 15:22
     * @param tenantId :
     * @param actualIdList :
     * @return java.util.List<tarzan.actual.domain.vo.MtInstructionActualVO3>
     */
    List<MtInstructionActualVO3> instructionActualPropertyBatchGet(Long tenantId, List<String> actualIdList);

    /**
     * instructionActualBatchUpdate-指令实绩批量创建与更新
     * 
     * @Author peng.yuan
     * @Date 2020/1/9 16:39
     * @param tenantId :
     * @param mtInstructionActualVO4s :
     * @return java.util.List<tarzan.actual.domain.vo.MtInstructionActualVO5>
     */
    List<MtInstructionActualVO5> instructionActualBatchUpdate(Long tenantId,
                                                              List<MtInstructionActualVO4> mtInstructionActualVO4s);
}
