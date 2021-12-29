package io.tarzan.common.domain.repository;

import io.tarzan.common.domain.vo.MtExtendRpcVO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.entity.MtExtendTableDesc;

/**
 * 扩展说明表资源库
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtExtendTableDescRepository
                extends BaseRepository<MtExtendTableDesc>, AopProxy<MtExtendTableDescRepository> {

    void initDataToRedis();

    MtExtendTableDesc attrTableNameLimitGet(Long tenantId, String attrTable);

    String extTabBasicPropertyUpdate(Long tenantId, MtExtendTableDesc dto);

    /**
     * tableLimitAttrNameQuery-查询扩展表结构
     *
     * @author benjamin
     * @date 2020/7/7 1:50 PM
     * @param tenantId 租户Id
     * @param tableName 扩展表表名
     * @return MtExtendRpcVO
     */
    MtExtendRpcVO tableLimitAttrNameQuery(Long tenantId, String tableName);

}
