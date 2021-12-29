package tarzan.actual.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtInstructionActualHis;
import tarzan.actual.domain.vo.MtInstructionActualHisVO;

/**
 * 指令实绩汇总历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtInstructionActualHisRepository
                extends BaseRepository<MtInstructionActualHis>, AopProxy<MtInstructionActualHisRepository> {

    /**
     *
     * @Author peng.yuan
     * @Date 2019/9/28 12:00
     * @param tenantId :
     * @param actualId : 指令实绩ID
     * @return tarzan.actual.domain.vo.MtInstructionActualHisVO
     */
    MtInstructionActualHisVO instructionActualLatestHisGet(Long tenantId, String actualId);
}
