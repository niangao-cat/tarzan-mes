package tarzan.actual.infra.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtNcRecord;

/**
 * 不良代码记录Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:55
 */
public interface MtNcRecordMapper extends BaseMapper<MtNcRecord> {

    /**
     * select nc records
     * <p>
     * conditions: EO_ID & PARENT_NC_RECORD_ID & NC_CODE_ID
     *
     * @param tenantId
     * @param ncCodeIds
     * @param eoId
     * @param parentNcRecordId
     * @return
     */
    List<MtNcRecord> selectByNcCodeIds(@Param(value = "tenantId") Long tenantId,
                                       @Param(value = "ncCodeIds") List<String> ncCodeIds, @Param(value = "eoId") String eoId,
                                       @Param(value = "parentNcRecordId") String parentNcRecordId);

    /**
     * select nc records count
     * <p>
     * condition: EO_ID & NC_CODE_ID & NC_STATUS not equals 'CANCEL'
     *
     * @param tenantId
     * @param dto
     * @return
     */
    int eoMaxNcLimitCount(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtNcRecord dto);

    /**
     * select max sequence in nc records
     * <p>
     * condition: EO_ID
     *
     * @param tenantId
     * @param eoId
     * @return
     */
    Long getMaxSequence(@Param(value = "tenantId") Long tenantId, @Param(value = "eoId") String eoId);

    /*
     * 查询当前工位当前班次的不良记录信息
     * 
     * @param tenantId 租户ID
     * @param dateTime 记录时间
     * @param workcellId 工位ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/17 15:17:00
     * @return java.util.List<tarzan.actual.domain.entity.MtNcRecord>
     */
    List<MtNcRecord> mtNcRecordQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dateTime") Date dateTime,
                                     @Param(value = "workcellId") String workcellId);
}
