package tarzan.pull.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.pull.domain.entity.MtPullOnhandSnapshot;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO2;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO3;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO4;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO5;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO6;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO7;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO8;

/**
 * 拉动线边库存快照资源库
 *
 * @author yiyang.xie 2020-02-04 15:53:01
 */
public interface MtPullOnhandSnapshotRepository
                extends BaseRepository<MtPullOnhandSnapshot>, AopProxy<MtPullOnhandSnapshotRepository> {

    /**
     * 创建库存快照
     * 
     * @Author peng.yuan
     * @Date 2020/2/5 11:20
     * @param tenantId :
     * @param vo :
     * @return tarzan.pull.domain.vo.MtPullOnhandSnapshotVO2
     */
    List<MtPullOnhandSnapshotVO2> onhandSnapShotCreate(Long tenantId, List<MtPullOnhandSnapshotVO> vo);

    /**
     * inventoryWaveReplenishmentQtyCalculate –定时补货模式拉动计算
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    MtPullOnhandSnapshotVO4 inventoryWaveReplenishmentQtyCalculate(Long tenantId, MtPullOnhandSnapshotVO3 dto);

    /**
     * pullOnhandSnapshotQuery-拉动现有量快照查询
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtPullOnhandSnapshotVO6> pullOnhandSnapshotQuery(Long tenantId, MtPullOnhandSnapshotVO5 dto);

    /**
     * inventoryWaveReplenishmentPullProcess-定时补货拉动
     * 
     * @param tenantId
     * @param dto
     */
    void inventoryWaveReplenishmentPullProcess(Long tenantId, MtPullOnhandSnapshotVO7 dto);

    /**
     * distributionActualQtyCalculate–计算实际配送数量
     * 
     * @param tenantId
     * @param dto
     */
    Double distributionActualQtyCalculate(Long tenantId, MtPullOnhandSnapshotVO8 dto);
}
