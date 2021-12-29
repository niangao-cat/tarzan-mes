package tarzan.general.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.general.domain.entity.MtEventRequest;
import tarzan.general.domain.vo.MtEventRequestVO1;
import tarzan.general.domain.vo.MtEventRequestVO2;
import tarzan.general.domain.vo.MtEventRequestVO3;
import tarzan.general.domain.vo.MtEventRequestVO4;

/**
 * 事件请求记录资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventRequestRepository extends BaseRepository<MtEventRequest>, AopProxy<MtEventRequestRepository> {

    /**
     * eventRequestGet-获取事件请求记录
     *
     * @author chuang.yang
     * @date 2019/3/5
     * @param tenantId
     * @param eventRequestId
     * @return hmes.event_request.dto.MtEventRequest
     */
    MtEventRequestVO1 eventRequestGet(Long tenantId, String eventRequestId);

    /**
     * propertyLimitEventRequestQuery-根据属性获取事件请求记录
     *
     * @author chuang.yang
     * @date 2019/3/5
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     */
    List<String> propertyLimitEventRequestQuery(Long tenantId, MtEventRequestVO2 dto);

    /**
     * eventRequestCreate-创建事件请求
     *
     * @author chuang.yang
     * @date 2019/3/5
     * @param tenantId
     * @param requestTypeCode
     * @return java.lang.String
     */
    String eventRequestCreate(Long tenantId, String requestTypeCode);

    /**
     *propertyLimitEventRequestPropertyQuery-根据属性获取事件请求信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEventRequestVO4> propertyLimitEventRequestPropertyQuery(Long tenantId, MtEventRequestVO3 dto);
}
