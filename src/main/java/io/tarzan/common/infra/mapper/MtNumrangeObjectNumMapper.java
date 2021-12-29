package io.tarzan.common.infra.mapper;

import java.util.List;

import feign.Param;
import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.domain.entity.MtNumrangeObjectNum;

/**
 * 号码段按对象序列号记录表Mapper
 *
 * @author MrZ 2019-08-22 21:38:58
 */
public interface MtNumrangeObjectNumMapper extends BaseMapper<MtNumrangeObjectNum> {

    List<MtNumrangeObjectNum> selectByObjectCombination(@Param("tenantId") Long tenantId,
                                                        @Param("dto") MtNumrangeObjectNum dto, @Param("combinations") List<String> combinations);
}
