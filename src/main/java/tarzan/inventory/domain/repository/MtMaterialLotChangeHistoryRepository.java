package tarzan.inventory.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.inventory.domain.entity.MtMaterialLotChangeHistory;
import tarzan.inventory.domain.vo.MtMaterialCategoryHisVO;
import tarzan.inventory.domain.vo.MtMaterialCategoryHisVO1;

/**
 * 物料批变更历史，记录物料批拆分合并的变更情况资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:05:08
 */
public interface MtMaterialLotChangeHistoryRepository
                extends BaseRepository<MtMaterialLotChangeHistory>, AopProxy<MtMaterialLotChangeHistoryRepository> {
    /**
     * sourceMaterialLotQuery-查询物料批单次拆分合并来源
     *
     * @param tenantId
     * @param materialLotId
     * @author guichuan.li
     * @date 2019/4/8
     */
    List<MtMaterialCategoryHisVO> sourceMaterialLotQuery(Long tenantId, String materialLotId);


    /**
     * materialLotLimitChangeHistoryQuery-获取物料批所有拆分
     *
     * @param tenantId
     * @param materialLotId
     * @author guichuan.li
     * @date 2019/4/8
     */
    List<MtMaterialCategoryHisVO1> materialLotLimitChangeHistoryQuery(Long tenantId, String materialLotId);


    /**
     * 新增物料批拆分合并历史记录
     * 
     * @param tenantId
     * @param dto
     * @return String
     */
    String materialLotChangeHistoryCreate(Long tenantId, MtMaterialLotChangeHistory dto);

    /**
     * 追溯来源Id
     *
     * @Author Xie.yiyang
     * @Date  2019/12/27 18:08
     * @param tenantId
     * @param materialId
     * @return java.util.List<tarzan.inventory.domain.vo.MtMaterialCategoryHisVO1>
     */
    List<MtMaterialCategoryHisVO1> materialLotLimitSourceIdQuery(Long tenantId, String materialId);
}
