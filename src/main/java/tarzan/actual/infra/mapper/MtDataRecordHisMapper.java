package tarzan.actual.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtDataRecordHis;
import tarzan.actual.domain.vo.MtNcRecordHisVO;

import org.apache.ibatis.annotations.Param;

/**
 * 数据收集实绩历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:01:00
 */
public interface MtDataRecordHisMapper extends BaseMapper<MtDataRecordHis> {
    MtNcRecordHisVO dataRecordLatestHisGet(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "dataRecordId") String dataRecordId);
}
