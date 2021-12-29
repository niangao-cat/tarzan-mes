package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtDataRecord;
import tarzan.general.domain.vo.MtDataRecordVO5;

/**
 * 数据收集实绩Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:01:00
 */
public interface MtDataRecordMapper extends BaseMapper<MtDataRecord> {
    /**
     * select by id list
     *
     * @author benjamin
     * @date 2019-07-01 11:10
     * @param dataRecordIdList Id List
     * @return List
     */
    List<MtDataRecord> selectByIdList(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dataRecordIdList") List<String> dataRecordIdList);


    List<MtDataRecord> selectPropertyLimitDataRecord(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtDataRecordVO5 dto);

}
