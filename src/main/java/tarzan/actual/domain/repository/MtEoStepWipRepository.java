package tarzan.actual.domain.repository;

import java.util.List;
import java.util.Map;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtEoStepWip;
import tarzan.actual.domain.vo.*;

/**
 * 执行作业在制品资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:30
 */
public interface MtEoStepWipRepository extends BaseRepository<MtEoStepWip>, AopProxy<MtEoStepWipRepository> {

    /**
     * 执行作业在制品临时库存记录
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String eoStepWipUpdate(Long tenantId, MtEoStepWipVO3 dto);

    /**
     * eoWkcAndStepWipQuery-获取工作单元或步骤的在制品数量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoStepWip> eoWkcAndStepWipQuery(Long tenantId, MtEoStepWipVO1 dto);

    /**
     * eoWkcAndStepWipBatchGet-批量获取工作单元在步骤的在制品数量
     *
     * @param tenantId
     * @param eoStepActualWkcIds eoStepActualId(key)、workcellId(value)组成的列表
     * @return
     */
    List<MtEoStepWip> eoWkcAndStepWipBatchGet(Long tenantId, Map<String, String> eoStepActualWkcIds);

    /**
     * eoWkcQueue-执行作业工作单元排队
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/11
     */
    void eoWkcQueue(Long tenantId, MtEoStepWipVO4 dto);

    /**
     * eoWkcBatchQueue-执行作业工作单元排队
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2020/10/26
     */
    void eoWkcBatchQueue(Long tenantId, MtEoStepWipVO17 dto);

    /**
     * eoWkcQueueCancel-执行作业工作单元排队取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/11
     */
    void eoWkcQueueCancel(Long tenantId, MtEoStepWipVO4 dto);

    /**
     * eoScrappedConfirm-执行作业报废确认
     * <p>
     * update remarks - 2019/7/16 benjamin 添加EoStepActualId字段并添加相应判断逻辑
     *
     * @param tenantId IRequest
     * @param dto MtEoStepWipVO5
     * @author chuang.yang
     * @date 2019/3/20
     */
    void eoScrappedConfirm(Long tenantId, MtEoStepWipVO5 dto);

    /**
     * wkcLimitEoQuery-根据WKC实绩获取可操作执行作业
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.eo_step_wip.view.MtEoStepWipVO6>
     * @author chuang.yang
     * @date 2019/3/22
     */
    List<MtEoStepWipVO6> wkcLimitEoQuery(Long tenantId, MtEoStepWipVO7 dto);

    /**
     * wkcWipLimitEoQuery-根据WKC在制品获取可操作执行作业
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.eo_step_wip.view.MtEoStepWipVO6>
     * @author chuang.yang
     * @date 2019/6/20
     */
    List<MtEoStepWipVO6> wkcWipLimitEoQuery(Long tenantId, MtEoStepWipVO7 dto);

    /**
     * eoWkcAndStepWipBatchQuery-批量获取工作单元或步骤的在制品数量
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019/12/19
     */
    List<MtEoStepWip> eoWkcAndStepWipBatchQuery(Long tenantId, List<MtEoStepWipVO1> dto);

    /**
     * eoStepWipUpdateQtyCalculate-执行作业在制品临时库存更新数量计算
     *
     * @param tenantId
     * @param vo
     * @return
     */
    Double eoStepWipUpdateQtyCalculate(Long tenantId, MtEoStepWipVO8 vo);

    /**
     * eoStepWipBatchUpdate-批量执行作业在制品临时库存记录
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoStepWipVO12> eoStepWipBatchUpdate(Long tenantId, MtEoStepWipVO13 dto);

    /**
     * eoStepWipUpdateQtyBatchCalculate-执行作业在制品临时库存更新数量批量计算
     *
     * @author chuang.yang
     * @date 2020/10/26
     * @param tenantId
     * @param dto
     */
    List<MtEoStepWipVO20> eoStepWipUpdateQtyBatchCalculate(Long tenantId, List<MtEoStepWipVO18> dto);
}
