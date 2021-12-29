package tarzan.iface.domain.repository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import org.hzero.mybatis.base.BaseRepository;
import org.hzero.core.base.AopProxy;
import tarzan.iface.domain.entity.MtPoHeader;

/**
 * 采购订单头表资源库
 *
 * @author yiyang.xie@hand-china.com 2019-10-08 19:52:47
 */
public interface MtPoHeaderRepository extends BaseRepository<MtPoHeader>, AopProxy<MtPoHeaderRepository> {

    /**
     * 采购订单头新增&更新扩展表属性
     * 
     * @Author peng.yuan
     * @Date 2019/11/20 14:24
     * @param tenantId :
     * @param mtExtendVO10 :
     * @return void
     */
    void poHeaderAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);

}
