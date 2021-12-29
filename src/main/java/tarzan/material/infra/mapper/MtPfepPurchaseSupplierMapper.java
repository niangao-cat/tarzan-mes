package tarzan.material.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.material.domain.entity.MtPfepPurchaseSupplier;

/**
 * 物料供应商采购属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepPurchaseSupplierMapper extends BaseMapper<MtPfepPurchaseSupplier> {

    /**
     * 根据供应商ID获取供应商采购PFEP属性
     */
    List<MtPfepPurchaseSupplier> queryByIds(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "supplierIds") String supplierIds);
}

