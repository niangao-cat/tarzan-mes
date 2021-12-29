package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtBomHis;
import tarzan.method.domain.vo.MtBomHisVO1;
import tarzan.method.domain.vo.MtBomHisVO2;
import tarzan.method.domain.vo.MtBomHisVO3;
import tarzan.method.domain.vo.MtBomHisVO4;
import tarzan.method.domain.vo.MtBomHisVO5;

/**
 * 装配清单头历史资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomHisRepository extends BaseRepository<MtBomHis>, AopProxy<MtBomHisRepository> {

    /**
     * bomAllHisCreate-创建装配清单历史 修改名称 bomHisCreate->bomAllHisCreate 19.3.21
     *
     * @param tenantId
     * @param dto
     */
    MtBomHisVO4 bomAllHisCreate(Long tenantId, MtBomHisVO1 dto);

    /**
     * bomHisQuery-获取装配清单历史
     *
     * @param tenantId
     * @param bomId
     * @return
     */
    List<MtBomHis> bomHisQuery(Long tenantId, String bomId);

    /**
     * bomAllHisQuery-获取装配清单整体历史
     *
     * @param tenantId
     * @param bomId
     * @return
     */
    List<MtBomHisVO2> bomAllHisQuery(Long tenantId, String bomId);

    /**
     * eventLimitBomAllHisQuery 根据事件获取装配清单整体历史
     * 
     * @param tenantId
     * @param eventId
     * @return
     */
    List<MtBomHisVO3> eventLimitBomAllHisQuery(Long tenantId, String eventId);

    /**
     * eventLimitBomHisBatchQuery 获取一批事件的装配清单历史
     * 
     * @param tenantId
     * @param eventIds
     * @return
     */
    List<MtBomHis> eventLimitBomHisBatchQuery(Long tenantId, List<String> eventIds);

    /**
     * 获取装配清单最新历史
     * @Author peng.yuan
     * @Date 2019/9/28 10:41
     * @param tenantId : 租户id
     * @param bomId : 装配清单ID
     * @return tarzan.method.domain.vo.MtBomHisVO5
     */
    MtBomHisVO5 bomLatestHisGet(Long tenantId, String bomId);
}
