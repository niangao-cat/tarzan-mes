package tarzan.iface.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.iface.domain.entity.MtSubinventory;
import tarzan.iface.domain.vo.MtSubinventoryIfaceVO;

/**
 * ERP库存表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:40:10
 */
public interface MtSubinventoryMapper extends BaseMapper<MtSubinventory> {

    /**
     * batch get sub inventory
     * 
     * @author benjamin
     * @date 2019-07-22 14:34
     * @param subInventoryList List
     * @return List
     */
    List<MtSubinventory> subInventoryBatchGet(
            @Param("tenantId") Long tenantId,
            @Param("subInventoryList") List<MtSubinventoryIfaceVO> subInventoryList);
}
