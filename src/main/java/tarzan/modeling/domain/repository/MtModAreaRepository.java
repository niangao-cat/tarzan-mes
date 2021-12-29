package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.vo.MtModAreaVO1;

/**
 * 区域资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModAreaRepository extends BaseRepository<MtModArea>, AopProxy<MtModAreaRepository> {

    /**
     * areaBasicPropertyGet获取区域基础属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param areaId
     * @return
     */
    MtModArea areaBasicPropertyGet(Long tenantId, String areaId);

    /**
     * areaBasicPropertyBatchGet-批量获取区域基础属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param areaIds
     * @return
     */
    List<MtModArea> areaBasicPropertyBatchGet(Long tenantId, List<String> areaIds);

    /**
     * propertyLimitAreaQuery根据区域属性获取区域
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param condition
     */
    List<String> propertyLimitAreaQuery(Long tenantId, MtModAreaVO1 condition);


    /**
     * areaBasicPropertyUpdate新增更新区域及基础属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return
     */
    String areaBasicPropertyUpdate(Long tenantId, MtModArea dto, String fullUpdate);

}
