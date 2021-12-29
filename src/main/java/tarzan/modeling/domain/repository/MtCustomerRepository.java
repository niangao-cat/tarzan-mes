package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.modeling.domain.entity.MtCustomer;

/**
 * 客户资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:29:33
 */
public interface MtCustomerRepository extends BaseRepository<MtCustomer>, AopProxy<MtCustomerRepository> {

    /**
     * 根据客户编码批量查询客户信息
     *
     * @author benjamin
     * @date 2019-07-24 09:31
     * @param customerCodeList 客户编码集合
     * @return List
     */
    List<MtCustomer> queryCustomerByCode(Long tenantId,List<String> customerCodeList);

    /**
     * customerAttrPropertyUpdate-客户新增&更新扩展表属性
     *
     * @Author Xie.yiyang
     * @Date 2019/11/20 12:57
     * @param tenantId
     * @param dto
     * @return void
     */
    void customerAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);

    /**
     * 根据Id获取客户信息
     */
    MtCustomer queryCustomerById(Long tenantId, String customerId);
    /**
     * 根据Id批量获取客户信息
     */
    List<MtCustomer> queryCustomerByIds(Long tenantId, List<String> customerIds);
}
