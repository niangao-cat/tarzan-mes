package tarzan.inventory.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.inventory.domain.entity.MtContainerHis;
import tarzan.inventory.domain.vo.MtContainerHisVO;
import tarzan.inventory.domain.vo.MtContainerHisVO1;
import tarzan.inventory.domain.vo.MtContainerHisVO2;
import tarzan.inventory.domain.vo.MtContainerHisVO3;

/**
 * 容器历史资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
public interface MtContainerHisRepository extends BaseRepository<MtContainerHis>, AopProxy<MtContainerHisRepository> {

    /**
     * eventLimitContainerHisBatchQuery-根据指定事件列表批量获取容器历史
     *
     * @param tenantId
     * @param eventIds
     * @return java.util.List<hmes.container_his.dto.MtContainerHis>
     * @author chuang.yang
     * @date 2019/4/8
     */
    List<MtContainerHis> eventLimitContainerHisBatchQuery(Long tenantId, List<String> eventIds);

    /**
     * requestLimitContainerHisQuery-根据事件请求获取容器历史
     *
     * @Author lxs
     * @Date 2019/4/8
     * @Return java.util.List<hmes.container_his.dto.MtContainerHis>
     */
    List<MtContainerHis> requestLimitContainerHisQuery(Long tenantId, MtContainerHisVO dto);


    /**
     * eventLimitContainerHisQuery-获取指定事件的容器历史
     *
     * @Author lxs
     * @Date 2019/4/8
     * @Return java.util.List<hmes.container_his.dto.MtContainerHis>
     */
    List<MtContainerHis> eventLimitContainerHisQuery(Long tenantId, String eventId);

    /**
     * containerHisQuery-获取容器历史记录
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/8
     */
    List<MtContainerHisVO2> containerHisQuery(Long tenantId, MtContainerHisVO1 dto);

    /**
     * containerLatestHisGet-获取容器最新历史
     *
     * @param tenantId
     * @param containerId
     * @return
     */
    MtContainerHisVO3 containerLatestHisGet(Long tenantId, String containerId);
}
