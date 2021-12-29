package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtAssembleGroupActualHis;
import tarzan.actual.domain.vo.MtAssembleGroupActualHisVO;
import tarzan.actual.domain.vo.MtAssembleGroupActualHisVO2;

/**
 * 装配组实绩历史,记录装配组所有安装位置历史记录资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtAssembleGroupActualHisRepository
                extends BaseRepository<MtAssembleGroupActualHis>, AopProxy<MtAssembleGroupActualHisRepository> {

    /**
     * 获取装配组实绩历史记录
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtAssembleGroupActualHisVO2> assembleGroupActualHisQuery(Long tenantId, MtAssembleGroupActualHisVO condition);

}
