package tarzan.iface.domain.repository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import org.hzero.mybatis.base.BaseRepository;
import org.hzero.core.base.AopProxy;
import tarzan.iface.domain.entity.MtPoLine;

/**
 * 采购订单计划发运行资源库
 *
 * @author yiyang.xie@hand-china.com 2019-10-08 19:52:47
 */
public interface MtPoLineRepository extends BaseRepository<MtPoLine>, AopProxy<MtPoLineRepository> {

    /**
     * 采购订单行新增&更新扩展表属性
     * 
     * @Author peng.yuan
     * @Date 2019/11/20 14:37
     * @param tenantId :
     * @param mtExtendVO10 :
     * @return void
     */
    void poLineAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);

}
