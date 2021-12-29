package tarzan.iface.infra.mapper;

import org.apache.ibatis.annotations.Param;
import tarzan.iface.domain.entity.MtSubinvLocatorIface;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * ERP货位接口表Mapper
 *
 * @author guichuan.li@hand-china.com 2019-09-24 10:33:46
 */
public interface MtSubinvLocatorIfaceMapper extends BaseMapper<MtSubinvLocatorIface> {
    /**
     * status in ('E','N')
     * 
     * @param tenantId
     * @return
     */
    List<MtSubinvLocatorIface> getUnprocessedList(@Param(value = "tenantId") Long tenantId);

}
