package tarzan.order.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.order.domain.entity.MtWorkOrderRel;
import tarzan.order.domain.vo.MtWorkOrderRelVO;
import tarzan.order.domain.vo.MtWorkOrderVO53;
import tarzan.order.domain.vo.MtWorkOrderVO54;

/**
 * 生产指令关系,标识生产指令的父子关系Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
public interface MtWorkOrderRelMapper extends BaseMapper<MtWorkOrderRel> {

    void batchDelete(@Param(value = "tenantId") Long tenantId, @Param(value = "relType") String relType,
                    @Param(value = "ids") List<MtWorkOrderRelVO> ids);

    List<MtWorkOrderVO53> selectByWorkOrderId(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "dto") MtWorkOrderVO54 dto);
}
