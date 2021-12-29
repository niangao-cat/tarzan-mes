package tarzan.iface.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.iface.domain.entity.MtBomComponentIface;
import tarzan.method.domain.entity.MtBom;

/**
 * BOM接口表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtBomComponentIfaceMapper extends BaseMapper<MtBomComponentIface> {

    /**
     * get unprocessed List
     * <p>
     * condition: status is 'N' or 'E'
     *
     * @return List
     * @author benjamin
     * @date 2019-06-26 15:33
     */
    List<MtBomComponentIface> getUnprocessedList(@Param(value = "tenantId") Long tenantId);

    /**
     * get unprocessed List
     * <p>
     * condition: status is 'N' or 'E'
     *
     * @return List
     * @author lkj
     * @date 2020年10月07日17:26:14
     */
    List<MtBomComponentIface> myGetUnprocessedList(@Param(value = "tenantId") Long tenantId, @Param(value = "batchId") Long batchId);

    /**
     * 根据bomCode 版本 类型查bom
     *
     * @param tenantId
     * @param dtoList
     * @return java.util.List<tarzan.method.domain.entity.MtBom>
     * @author sanfeng.zhang@hand-china.com 2021/11/10
     */
    List<MtBom> bomLimitBomCodeAndRevisionAndBomTypeQuery(@Param("tenantId") Long tenantId, @Param("dtoList") List<MtBomComponentIface> dtoList);

    /**
     * 查询未处理批次
     * @author penglin.sui@hand-china.com 2021-11-17 16:56
     * @param tenantId
     * @return java.util.List<java.lang.Long>
     */
    List<Long> selectBatchId(@Param(value="tenantId") Long tenantId);
}
