package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtBomReferencePointHis;

/**
 * 装配清单行参考点关系历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomReferencePointHisRepository
                extends BaseRepository<MtBomReferencePointHis>, AopProxy<MtBomReferencePointHisRepository> {

    /**
     * bomReferencePointHisQuery-获取装配清单组件行参考点历史
     *
     * @param tenantId
     * @param bomReferencePointId
     * @return
     */
    List<MtBomReferencePointHis> bomReferencePointHisQuery(Long tenantId, String bomReferencePointId);

    /**
     * eventLimitBomReferencePointHisBatchQuery 获取一批事件的装配清单组件行参考点属性历史
     * 
     * @param tenantId
     * @param eventIds
     * @return
     */
    List<MtBomReferencePointHis> eventLimitBomReferencePointHisBatchQuery(Long tenantId, List<String> eventIds);

}
