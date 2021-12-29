package io.tarzan.common.infra.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.domain.entity.MtExtendTableDesc;
import io.tarzan.common.domain.vo.MtExtendVO11;
import io.tarzan.common.domain.vo.MtExtendVO12;

/**
 * 扩展说明表Mapper
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtExtendTableDescMapper extends BaseMapper<MtExtendTableDesc> {

    String selectLatestHisIdByMainT(@Param(value = "tenantId") Long tenantId,
                                    @Param(value = "mainTableName") String mainTableName,
                                    @Param(value = "mainTableKey") String mainTableKey, @Param(value = "keyId") String keyId);

    List<MtExtendVO11> selectLatestHisIdsByMainT(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "mainTableName") String mainTableName,
                                                 @Param(value = "mainTableKey") String mainTableKey, @Param(value = "keyIds") String keyIds);

    String selectLatestHisIdByHisT(@Param(value = "tenantId") Long tenantId, @Param(value = "hisTable") String hisTable,
                                   @Param(value = "hisTableKey") String hisTableKey,
                                   @Param(value = "mainTableKey") String mainTableKey, @Param(value = "keyId") String keyId,
                                   @Param(value = "eventId") String eventId);

    String selectEventIdByLatestHisId(@Param(value = "tenantId") Long tenantId,
                                      @Param(value = "hisTable") String hisTable, @Param(value = "hisTableKey") String hisTableKey,
                                      @Param(value = "latestHisId") String latestHisId);

    List<MtExtendVO12> selectEventIdsByLatestHisId(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "hisTable") String hisTable, @Param(value = "hisTableKey") String hisTableKey,
                                                   @Param(value = "hisIds") List<String> hisIds);

    List<MtExtendTableDesc> selectAllExtTab();
}
