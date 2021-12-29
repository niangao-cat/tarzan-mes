package tarzan.iface.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.iface.domain.entity.MtPoHeader;
import tarzan.iface.domain.vo.MtPoHeaderVO;

/**
 * 采购订单头表Mapper
 *
 * @author yiyang.xie@hand-china.com 2019-10-08 19:52:47
 */
public interface MtPoHeaderMapper extends BaseMapper<MtPoHeader> {
    List<MtPoHeader> selectByPoReleaseId(@Param(value = "tenantId") Long tenantId,
                                         @Param(value = "poReleaseIds") List<String> poReleaseIds);

    List<MtPoHeader> selectByPoHeaderId(@Param(value = "tenantId") Long tenantId,
                                        @Param(value = "poHeaderIds") List<String> poHeaderIds);


    List<MtPoHeader> selectOnlyByPoHeaderId(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "poHeaderIds") List<MtPoHeaderVO> poHeaderIds);

    List<MtPoHeader> selectOnlyByPoNumber(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "poNumber") String poNumber);

    List<MtPoHeader> selectOnlyByReleaseNum(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "poNumber") String poNumber);
}
