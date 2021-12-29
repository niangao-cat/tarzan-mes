package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtAssemblePointActualHis;
import tarzan.actual.domain.vo.MtAssemblePointActualHisVO;
import tarzan.actual.domain.vo.MtAssemblePointActualHisVO1;

/**
 * 装配点实绩历史，记录装配组下装配点实际装配物料和数量变更记录资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtAssemblePointActualHisRepository
                extends BaseRepository<MtAssemblePointActualHis>, AopProxy<MtAssemblePointActualHisRepository> {

    /**
     * 获取装配点实绩历史记录
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtAssemblePointActualHisVO1> assemblePointActualHisQuery(Long tenantId, MtAssemblePointActualHisVO condition);

}
