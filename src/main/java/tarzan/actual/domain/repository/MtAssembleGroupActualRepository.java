package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtAssembleGroupActual;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO1;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO2;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO3;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO4;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO5;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO6;

/**
 * 装配组实绩，记录装配组安装的位置资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtAssembleGroupActualRepository
                extends BaseRepository<MtAssembleGroupActual>, AopProxy<MtAssembleGroupActualRepository> {

    /**
     * 获取执行作业指定工作单元指定参考区域可安装的装配组
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> assembleControlLimitAssembleGroupQuery(Long tenantId, MtAssembleGroupActualVO condition);

    /**
     * 根据装配组实绩获取装配组和工作单元
     *
     * @param tenantId
     * @param assembleGroupActualId
     * @return
     */
    MtAssembleGroupActual assembleGroupActualPropertyGet(Long tenantId, String assembleGroupActualId);

    /**
     * 根据装配组实绩批量获取装配组和工作单元
     * 
     * @param tenantId
     * @param assembleGroupActualIds
     * @return
     */
    List<MtAssembleGroupActual> assembleGroupActualPropertyBatchGet(Long tenantId, List<String> assembleGroupActualIds);

    /**
     * 根据工作单元或装配组获取装配组实绩
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> propertyLimitAssembleGroupActualQuery(Long tenantId, MtAssembleGroupActualVO1 condition);

    /**
     * 获取工作单元当前安装的装配组
     * 
     * @param tenantId
     * @param workcellId
     * @return
     */
    List<MtAssembleGroupActualVO2> wkcLimitCurrentAssembleGroupQuery(Long tenantId, String workcellId);

    /**
     * 获取顺序控制装配组第一个未上料的装配点
     * 
     * @param tenantId
     * @param assembleGroupId
     * @return
     */
    String firstEmptyAssemblePointGet(Long tenantId, String assembleGroupId);

    /**
     * 获取指定物料或物料批第一个可上料的装配点
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    String firstAvailableFeedingAssemblePointGet(Long tenantId, MtAssembleGroupActualVO3 condition);

    /**
     * 验证工作单元装配组安装是否满足装配控制要求
     * 
     * @param tenantId
     * @param condition
     */
    void wkcAssembleGroupControlVerify(Long tenantId, MtAssembleGroupActualVO4 condition);

    /**
     * assembleGroupActualUpdate-装配组实绩新增&更新并记录历史
     * 
     * update remarks
     * <ul>
     * <li>2019-09-18 benjamin 添加返回参数</li>
     * </ul>
     * 
     * @param tenantId 租户Id
     * @param dto MtAssembleGroupActualVO5
     * @return MtAssembleGroupActualVO6
     */
    MtAssembleGroupActualVO6 assembleGroupActualUpdate(Long tenantId, MtAssembleGroupActualVO5 dto);

    /**
     * 装配组实绩删除
     * 
     * @param tenantId
     * @param assembleGroupActualId
     */
    void assembleGroupActualDelete(Long tenantId, String assembleGroupActualId);

}
