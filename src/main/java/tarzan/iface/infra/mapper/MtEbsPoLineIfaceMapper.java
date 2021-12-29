package tarzan.iface.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.iface.domain.entity.MtEbsPoLineIface;

/**
 * EBS采购订单行接口表Mapper
 *
 * @author guichuan.li@hand-china.com 2019-10-08 15:19:56
 */
public interface MtEbsPoLineIfaceMapper extends BaseMapper<MtEbsPoLineIface> {
    /**
     * get unprocessed List
     *
     * condition: status is 'N' or 'E'
     *
     * @return List
     */
    List<MtEbsPoLineIface> getUnprocessedList(@Param(value = "tenantId") Long tenantId);

}
