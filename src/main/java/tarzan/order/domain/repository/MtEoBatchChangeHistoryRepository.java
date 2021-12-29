package tarzan.order.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.order.domain.entity.MtEoBatchChangeHistory;
import tarzan.order.domain.vo.MtEoBatchChangeHistoryVO;
import tarzan.order.domain.vo.MtEoBatchChangeHistoryVO2;
import tarzan.order.domain.vo.MtEoBatchChangeHistoryVO3;
import tarzan.order.domain.vo.MtEoBatchChangeHistoryVO4;
import tarzan.order.domain.vo.MtEoBatchChangeHistoryVO5;
import tarzan.order.domain.vo.MtEoBatchChangeHistoryVO6;

/**
 * 执行作业变更记录资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
public interface MtEoBatchChangeHistoryRepository
        extends BaseRepository<MtEoBatchChangeHistory>, AopProxy<MtEoBatchChangeHistoryRepository> {
    /**
     * eoBatchChangeTargetQuery-获取指定执行作业单次拆分合并结果
     *
     * @param tenantId
     * @param eoId
     * @return
     */
    List<MtEoBatchChangeHistoryVO> relTargetEoQuery(Long tenantId, String eoId);

    /**
     * eoBatchChangeSourceQuery-获取指定执行作业单次拆分合并来源
     *
     * @param tenantId
     * @param eoId
     * @return
     */
    List<MtEoBatchChangeHistoryVO2> relSourceEoQuery(Long tenantId, String eoId);

    /**
     * eoLimitBatchChangeHistoryQuery-获取指定执行作业所有的拆分合并记录
     *
     * @param tenantId
     * @param eoId
     * @return
     */
    List<MtEoBatchChangeHistoryVO3> eoRelQuery(Long tenantId, String eoId);

    /**
     * eoRelUpdate-更新&新增执行作业关系
     *
     * @author chuang.yang
     * @date 2019/12/2
     * @param tenantId
     * @param dto
     * @param fullUpdate
     * @return java.lang.String
     */
    String eoRelUpdate(Long tenantId, MtEoBatchChangeHistoryVO4 dto, String fullUpdate);

    /**
     * relSourceTreeEoQuery-获取指定执行作业所有层级拆分合并来源
     *
     * @param tenantId
     * @param eoId
     * @return
     * @return List<MtEoBatchChangeHistoryVO6>
     */
    List<MtEoBatchChangeHistoryVO6> relSourceEoTreeQuery(Long tenantId, String eoId);


    /**
     * relTargetTreeEoQuery-获取指定执行作业所有层级拆分合并结果
     *
     * @param tenantId
     * @param eoId
     * @return
     * @return List<MtEoBatchChangeHistoryVO5>
     */
    List<MtEoBatchChangeHistoryVO5> relTargetEoTreeQuery(Long tenantId, String eoId);
}
