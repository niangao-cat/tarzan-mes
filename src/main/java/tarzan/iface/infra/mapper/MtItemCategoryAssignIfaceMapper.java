package tarzan.iface.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.iface.domain.entity.MtItemCategoryAssignIface;

/**
 * 物料与类别关系数据接口Mapper
 *
 * @author mingjie.chen@hand-china.com 2019-09-09 17:05:24
 */
public interface MtItemCategoryAssignIfaceMapper extends BaseMapper<MtItemCategoryAssignIface> {

    /**
     * get unprocessed List
     *
     * condition: status is 'N' or 'E'
     * 
     * @param tenantId 租户Id
     * @author benjamin
     * @date 2019-07-10 15:33
     * @return List
     */
    List<MtItemCategoryAssignIface> getUnprocessedList(@Param(value = "tenantId") Long tenantId);
}
