package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtAssembleGroup;
import tarzan.method.domain.vo.MtAssembleGroupVO1;
import tarzan.method.domain.vo.MtAssembleGroupVO2;
import tarzan.method.domain.vo.MtAssembleGroupVO3;
import tarzan.method.domain.vo.MtAssembleGroupVO4;
import tarzan.method.domain.vo.MtAssembleGroupVO5;

/**
 * 装配组，标识一个装载设备或一类装配关系资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
public interface MtAssembleGroupRepository
                extends BaseRepository<MtAssembleGroup>, AopProxy<MtAssembleGroupRepository> {

    /**
     * 根据属性获取装配组性
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitAssembleGroupQuery(Long tenantId, MtAssembleGroup dto);

    /**
     * 获取装配组属性
     * 
     * @param tenantId
     * @param assembleGroupId
     * @return
     */
    MtAssembleGroup assembleGroupPropertyGet(Long tenantId, String assembleGroupId);

    /**
     * assembleGroupPropertyBatchGet-批量获取装配组属性
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param assembleGroupIds
     * @return java.util.List<hmes.assemble_group.dto.MtAssembleGroup>
     */
    List<MtAssembleGroup> assembleGroupPropertyBatchGet(Long tenantId, List<String> assembleGroupIds);

    /**
     * 装配组可用性校验
     * 
     * @param tenantId
     * @param assembleGroupId
     */
    void assembleGroupAvailableValidate(Long tenantId, String assembleGroupId);

    /**
     * assembleGroupUpdate-新增更新装配组
     *
     * update remarks
     * <ul>
     * <li>2019-09-18 benjamin 新增时校验唯一性</li>
     * </ul>
     * 
     * @param tenantId 租户Id
     * @param dto MtAssembleGroup
     * @return String
     */
    String assembleGroupUpdate(Long tenantId, MtAssembleGroup dto);

    /**
     * 将装配组从指定工作单元中取消安装/sen.luo 2018-03-21
     * 
     * @param tenantId
     * @param dto
     */
    void wkcAssembleGroupSetupCancel(Long tenantId, MtAssembleGroupVO1 dto);

    /**
     * 将装配组安装到工作单元/sen.luo 2018-03-22
     * 
     * @param tenantId
     * @param dto
     */
    void wkcAssembleGroupSetup(Long tenantId, MtAssembleGroupVO1 dto);

    /**
     * 装配组物料消耗/sen.luo 2018-03-22
     * 
     * @param tenantId
     * @param dto
     */
    void assembleGroupMaterialConsume(Long tenantId, MtAssembleGroupVO2 dto);

    /**
     * assembleGroupStatusUpdateVerify-装配组状态变更校验
     *
     * @Author lxs
     * @Date 2019/4/15
     * @Params [tenantId, dto]
     * @Return void
     */
    void assembleGroupStatusUpdateVerify(Long tenantId, MtAssembleGroupVO3 dto);

    /**
     * assembleGroupStatusUpdate-装配组状态变更
     *
     * @Author lxs
     * @Date 2019/4/15
     * @Params [tenantId, dto]
     * @Return void
     */
    void assembleGroupStatusUpdate(Long tenantId, MtAssembleGroupVO3 dto);

    /**
     * 根据属性获取装配组信息
     * @Author peng.yuan
     * @Date 2019/10/9 15:23
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.method.domain.vo.MtAssembleGroupVO5>
     */
    List<MtAssembleGroupVO5> propertyLimitAssembleGroupPropertyQuery(Long tenantId, MtAssembleGroupVO4 dto);

}
