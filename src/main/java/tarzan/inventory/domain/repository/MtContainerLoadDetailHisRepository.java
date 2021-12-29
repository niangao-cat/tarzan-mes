package tarzan.inventory.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.inventory.domain.entity.MtContainerLoadDetailHis;
import tarzan.inventory.domain.vo.MtContLoadDtlHisVO;
import tarzan.inventory.domain.vo.MtContLoadDtlHisVO2;
import tarzan.inventory.domain.vo.MtContLoadDtlHisVO3;

/**
 * 容器装载明细历史资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
public interface MtContainerLoadDetailHisRepository
                extends BaseRepository<MtContainerLoadDetailHis>, AopProxy<MtContainerLoadDetailHisRepository> {

    /**
     * eventLimitContainerLoadDetailBatchQuery -根据指定事件列表批量获取容器装载明细历史
     * 
     * @Author lxs
     * @Date 2019/4/3
     * @Return List<MtContainerLoadDetailHis>
     */
    List<MtContainerLoadDetailHis> eventLimitContainerLoadDetailBatchQuery(Long tenantId, List<String> eventIds);

    /**
     * requestLimitContainerLoadDetailHisQuery -根据事件请求获取容器装载明细历史
     * 
     * @Author lxs
     * @Date 2019/4/3
     * @Return java.util.List<hmes.container_load_detail_his.dto.MtContainerLoadDetailHis>
     */
    List<MtContainerLoadDetailHis> requestLimitContainerLoadDetailHisQuery(Long tenantId, MtContLoadDtlHisVO dto);


    /**
     * eventLimitContainerLoadDetailQuery -获取指定事件的容器装载明细历史
     * 
     * @Author lxs
     * @Date 2019/4/3
     * @Return java.util.List<hmes.container_load_detail_his.dto.MtContainerLoadDetailHis>
     */
    List<MtContainerLoadDetailHis> eventLimitContainerLoadDetailQuery(Long tenantId, String eventId);

    /**
     * containerLoadDetailHisQuery -获取容器装载明细历史记录
     * 
     * @Author lxs
     * @Date 2019/4/3
     * @Return java.util.List<hmes.container_load_detail_his.view.MtContainerLoadDetailHisVo3>
     */
    List<MtContLoadDtlHisVO3> containerLoadDetailHisQuery(Long tenantId, MtContLoadDtlHisVO2 dto);

}
