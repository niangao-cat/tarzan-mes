package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO2;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO3;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO4;

/**
 * 指令实绩明细表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtInstructionActualDetailRepository
                extends BaseRepository<MtInstructionActualDetail>, AopProxy<MtInstructionActualDetailRepository> {

    /**
     * instructionActualDetailCreate-指令实绩明细创建
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/6/19
     */
    String instructionActualDetailCreate(Long tenantId, MtInstructionActualDetail dto);

    /**
     * propertyLimitInstructionActualDetailQuery-根据实绩明细属性获取实绩明细
     * 
     * @author benjamin
     * @date 2019-07-04 10:18
     * @param tenantId
     * @param detail MtInstructionActualDetail
     * @return List
     */
    List<MtInstructionActualDetailVO> propertyLimitInstructionActualDetailQuery(Long tenantId,
                                                                                MtInstructionActualDetail detail);

    /**
     * instructionLimitActualDetailQuery-根据指令获取实绩明细
     * 
     * @author benjamin
     * @date 2019-07-04 10:22
     * @param tenantId
     * @param instructionId 指令Id
     * @return List
     */
    List<MtInstructionActualDetail> instructionLimitActualDetailQuery(Long tenantId, String instructionId);

    /**
     * 批量删除指令实绩明细
     * 
     * @author benjamin
     * @date 2019-07-04 11:12
     * @param tenantId
     * @param instructionActualDetailIdList 指令实绩明细Id集合
     */
    void instructionActualDetailBatchDelete(Long tenantId, List<String> instructionActualDetailIdList);

    /**
     * 批量删除指令实绩明细
     * 
     * @author zijin.liang
     * @date 2019-10-22 09:58
     * @param tenantId
     * @param actualIds 指令实绩明细Id集合
     */
    List<MtInstructionActualDetail> instructionActualLimitDetailBatchQuery(Long tenantId, List<String> actualIds);

    /**
     * instructActDetailAttrPropertyUpdate-指令实绩明细行新增&更新扩展表属性
     *
     * @Author Xie.yiyang
     * @Date 2019/11/19 17:15
     * @param tenantId
     * @param dto
     * @return void
     */
    void instructActDetailAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);

    /**
     * 根据指令批量获取实绩明细
     * 
     * @Author peng.yuan
     * @Date 2020/1/8 15:46
     * @param tenantId :
     * @param instructionIdList :
     * @return java.util.List<tarzan.actual.domain.vo.MtInstructionActualDetailVO2>
     */
    List<MtInstructionActualDetailVO2> instructionLimitActualDetailBatchQuery(Long tenantId,
                                                                              List<String> instructionIdList);

    /**
     * instructionActualDetailBatchCreate-指令实绩明细批量创建
     *
     * @author chuang.yang
     * @date 2020/1/14
     * @param tenantId
     * @param actualDetailMessageList
     * @return java.util.List<tarzan.actual.domain.vo.MtInstructionActualDetailVO3>
     */
    List<MtInstructionActualDetailVO3> instructionActualDetailBatchCreate(Long tenantId,
                                                                          List<MtInstructionActualDetailVO4> actualDetailMessageList);
}
