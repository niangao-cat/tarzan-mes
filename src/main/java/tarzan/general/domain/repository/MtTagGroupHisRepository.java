package tarzan.general.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.general.domain.entity.MtTagGroupHis;
import tarzan.general.domain.vo.MtTagGroupHisVO;

/**
 * 数据收集组历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupHisRepository extends BaseRepository<MtTagGroupHis>, AopProxy<MtTagGroupHisRepository> {
    /**
     * 获取数据收集组最新历史
     * @Author peng.yuan
     * @Date 2019/10/14 16:55
     * @param tenantId :
     * @param tagGroupId : 数据收集组ID
     * @return tarzan.general.domain.vo.MtTagGroupHisVO
     */
    MtTagGroupHisVO tagGroupLatestHisGet(Long tenantId, String tagGroupId);
}
