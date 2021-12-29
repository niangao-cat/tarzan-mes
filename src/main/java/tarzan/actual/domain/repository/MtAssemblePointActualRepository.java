package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtAssemblePointActual;
import tarzan.actual.domain.vo.MtAssemblePointActualVO;
import tarzan.actual.domain.vo.MtAssemblePointActualVO1;
import tarzan.actual.domain.vo.MtAssemblePointActualVO2;
import tarzan.actual.domain.vo.MtAssemblePointActualVO3;
import tarzan.actual.domain.vo.MtAssemblePointActualVO4;
import tarzan.actual.domain.vo.MtAssemblePointActualVO5;
import tarzan.actual.domain.vo.MtAssemblePointActualVO6;
import tarzan.actual.domain.vo.MtAssemblePointActualVO7;
import tarzan.actual.domain.vo.MtAssemblePointActualVO8;
import tarzan.actual.domain.vo.MtAssemblePointActualVO9;

/**
 * 装配点实绩，记录装配组下装配点实际装配信息资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtAssemblePointActualRepository
                extends BaseRepository<MtAssemblePointActual>, AopProxy<MtAssemblePointActualRepository> {

    /**
     * ropertyLimitAssemblePointActualQuery-根据装配点实绩属性获取装配点实绩
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> propertyLimitAssemblePointActualQuery(Long tenantId, MtAssemblePointActualVO1 condition);

    /**
     * assemblePointActualPropertyGet-根据装配点实绩获取装配点属性
     *
     * @param tenantId
     * @param assemblePointActualId
     * @return
     */
    MtAssemblePointActualVO1 assemblePointActualPropertyGet(Long tenantId, String assemblePointActualId);

    /**
     * 根据装配点实绩批量获取装配点属性
     *
     * @param tenantId
     * @param assemblePointActualIds
     * @return
     */
    List<MtAssemblePointActualVO1> assemblePointActualPropertyBatchGet(Long tenantId,
                                                                       List<String> assemblePointActualIds);

    /**
     * 获取执行作业指定参考区域下工作单元上物料可上料安装的装配点
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> assembleControlLimitAssemblePointQuery(Long tenantId, MtAssemblePointActualVO condition);

    /**
     * 获取装配组下装配点上料顺序
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    Long assemblePointFeedingNextSequenceGet(Long tenantId, MtAssemblePointActualVO6 condition);

    /**
     * 获取装配点已装配物料的数量
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    Double assemblePointLoadingMaterialQtyGet(Long tenantId, MtAssemblePointActualVO2 condition);

    /**
     * 获取装配点下物料批上料顺序
     * 
     * @param tenantId
     * @param assemblePointId
     * @return
     */
    Long feedingLotNextSequenceGet(Long tenantId, String assemblePointId);

    /**
     * 验证执行作业工作单元上物料装配点安装是否满足装配控制要求
     * 
     * @param tenantId
     * @param condition
     */
    void materialAssemblePointControlVerify(Long tenantId, MtAssemblePointActualVO3 condition);

    /**
     * 验证装配点装配的物料和物料批数量是否满足需求
     *
     * @param tenantId
     * @param condition
     */
    void assemblePointHaveMaterialVerify(Long tenantId, MtAssemblePointActualVO5 condition);

    /**
     * 装配点实绩删除
     * 
     * @param tenantId
     * @param assemblePointActualIds
     */
    void assemblePointActualDeleteBatch(Long tenantId, List<String> assemblePointActualIds);

    /**
     * 装配点实绩删除
     *
     * @param tenantId
     * @param assemblePointActualId
     */
    void assemblePointActualDelete(Long tenantId, String assemblePointActualId);

    /**
     * 装配点实绩新增&更新并记录历史
     *
     * @param tenantId
     * @param dto
     */
    MtAssemblePointActualVO7 assemblePointActualUpdate(Long tenantId, MtAssemblePointActualVO4 dto, String fullUpdate);

    /**
     * 根据属性获取装配点实绩信息
     * @Author peng.yuan
     * @Date 2019/10/10 17:04
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.actual.domain.vo.MtAssemblePointActualVO9>
     */
    List<MtAssemblePointActualVO9> propertyLimitAssemblePointActualPropertyQuery(Long tenantId, MtAssemblePointActualVO8 dto);
}
