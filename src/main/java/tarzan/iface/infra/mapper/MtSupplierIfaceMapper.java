package tarzan.iface.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.iface.domain.entity.MtSupplierIface;

import java.util.List;

/**
 * 供应商数据接口表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtSupplierIfaceMapper extends BaseMapper<MtSupplierIface> {
    /**
     * get unprocessed List
     *
     * condition: status is 'N' or 'E'
     *
     * @author benjamin
     * @date 2019-07-10 15:33
     * @return List
     */
    List<MtSupplierIface> getUnprocessedList(@Param(value = "tenantId") Long tenantId);
}
