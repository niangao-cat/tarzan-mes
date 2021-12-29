package tarzan.iface.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.iface.domain.entity.MtSubinventory;
import tarzan.iface.domain.vo.MtSubinventoryIfaceVO;

/**
 * ERP库存表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:40:10
 */
public interface MtSubinventoryRepository extends BaseRepository<MtSubinventory>, AopProxy<MtSubinventoryRepository> {

    /**
     * 批量获取ERP库存数据
     * 
     * @author benjamin
     * @date 2019-07-22 14:36
     * @param mtSubInventoryList List
     * @return List
     */
    List<MtSubinventory> subInventoryBatchGet(Long tenantId, List<MtSubinventoryIfaceVO> mtSubInventoryList);
    
}
