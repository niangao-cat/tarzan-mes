package tarzan.iface.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.iface.domain.entity.MtLocatorSubinvReleation;

/**
 * 子库存与库位对应关系资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:39:44
 */
public interface MtLocatorSubinvReleationRepository
                extends BaseRepository<MtLocatorSubinvReleation>, AopProxy<MtLocatorSubinvReleationRepository> {

}
