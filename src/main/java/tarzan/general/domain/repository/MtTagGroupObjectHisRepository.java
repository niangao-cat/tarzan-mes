package tarzan.general.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.general.domain.entity.MtTagGroupObjectHis;

/**
 * 数据收集组关联对象历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupObjectHisRepository
                extends BaseRepository<MtTagGroupObjectHis>, AopProxy<MtTagGroupObjectHisRepository> {

}
