package tarzan.inventory.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.inventory.domain.entity.MtMaterialLotHis;
import tarzan.inventory.domain.vo.MtMaterialLotHisVO1;
import tarzan.inventory.domain.vo.MtMaterialLotHisVO2;
import tarzan.inventory.domain.vo.MtMaterialLotHisVO3;

/**
 * 物料批历史资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:05:08
 */
public interface MtMaterialLotHisRepository
                extends BaseRepository<MtMaterialLotHis>, AopProxy<MtMaterialLotHisRepository> {

    /**
     * requestLimitMaterialLotHisQuery-根据事件请求获取物料批历史
     *
     * @author chuang.yang
     * @date 2019/4/5
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.material_lot_his.dto.MtMaterialLotHis>
     */
    List<MtMaterialLotHis> requestLimitMaterialLotHisQuery(Long tenantId, MtMaterialLotHisVO1 dto);

    /**
     * eventLimitMaterialLotHisQuery-获取指定事件的物料批历史
     *
     * @author chuang.yang
     * @date 2019/4/5
     * @param tenantId
     * @param eventId
     * @return java.util.List<hmes.material_lot_his.dto.MtMaterialLotHis>
     */
    List<MtMaterialLotHis> eventLimitMaterialLotHisQuery(Long tenantId, String eventId);

    /**
     * eventLimitMaterialLotHisBatchQuery-根据指定事件列表批量获取物料批历史
     *
     * @author chuang.yang
     * @date 2019/4/5
     * @param tenantId
     * @param eventIds
     * @return java.util.List<hmes.material_lot_his.dto.MtMaterialLotHis>
     */
    List<MtMaterialLotHis> eventLimitMaterialLotHisBatchQuery(Long tenantId, List<String> eventIds);

    /**
     * materialLotHisQuery-获取物料批历史记录
     *
     * @author chuang.yang
     * @date 2019/4/5
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.material_lot_his.dto.MtMaterialLotHis>
     */
    List<MtMaterialLotHis> materialLotHisQuery(Long tenantId, MtMaterialLotHisVO2 dto);

    /**
     * materialLotLatestHisGet-获取物料批最新历史
     *
     * @param tenantId
     * @param materialLotId
     * @return
     */
    MtMaterialLotHisVO3 materialLotLatestHisGet(Long tenantId, String materialLotId);

    /**
     * 批量获取物料批历史数据
     *
     * @Author Xie.yiyang
     * @Date 2020/1/13 20:57
     * @param tenantId
     * @param hisIds
     * @return java.util.List<tarzan.inventory.domain.entity.MtMaterialLot>
     */
    List<MtMaterialLotHis> materialLotHisBatchGet(Long tenantId, List<String> hisIds);
}
