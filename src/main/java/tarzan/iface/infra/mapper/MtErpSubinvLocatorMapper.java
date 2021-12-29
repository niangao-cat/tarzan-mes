package tarzan.iface.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.iface.domain.entity.MtErpSubinvLocator;
import tarzan.iface.domain.vo.MtErpSubinvLocatorVO;

import java.util.List;

/**
 * ERP货位业务表 Mapper
 *
 * @author guichuan.li@hand-china.com 2019-09-24 10:49:01
 */
public interface MtErpSubinvLocatorMapper extends BaseMapper<MtErpSubinvLocator> {

    List<MtErpSubinvLocator> getInsertOrUpdateData(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") List<MtErpSubinvLocatorVO> dto);
}
