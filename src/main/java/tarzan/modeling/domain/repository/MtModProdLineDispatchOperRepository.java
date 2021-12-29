package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModProdLineDispatchOper;
import tarzan.modeling.domain.vo.MtModProdLineDispatchOperVO1;

/**
 * 生产线调度指定工艺资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModProdLineDispatchOperRepository
        extends BaseRepository<MtModProdLineDispatchOper>, AopProxy<MtModProdLineDispatchOperRepository> {

    /**
     * prodLineLimitDispatchOperationQuery-根据生产线获取指定的调度工艺
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param prodLineId
     * @return java.util.List<java.lang.String>
     */
    List<String> prodLineLimitDispatchOperationQuery(Long tenantId, String prodLineId);

    /**
     * prodLineDispatchOperationValidate-校验传入工艺是否为生产线的指定调度工艺
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return void
     */
    void prodLineDispatchOperationValidate(Long tenantId, MtModProdLineDispatchOperVO1 dto);

    /**
     * operationLimitProdLineQuery-根据指定工艺获取指定该工艺调度的生产线
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param operationId
     * @return java.util.List<java.lang.String>
     */
    List<String> operationLimitProdLineQuery(Long tenantId, String operationId);

}
