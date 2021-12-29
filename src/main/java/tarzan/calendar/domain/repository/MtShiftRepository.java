package tarzan.calendar.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.calendar.domain.entity.MtShift;
import tarzan.calendar.domain.vo.MtShiftVO;
import tarzan.calendar.domain.vo.MtShiftVO1;

/**
 * 班次信息资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:39
 */
public interface MtShiftRepository extends BaseRepository<MtShift>, AopProxy<MtShiftRepository> {

    /**
     * shiftTempletGet-获取班次模板基础属性.
     *
     * @param tenantId
     * @param shiftId
     * @return MtShift
     */
    MtShift shiftTempletGet(Long tenantId, String shiftId);

    /**
     * typeLimitShiftTempletQuery-获取特定类型班次
     *
     * @param tenantId
     * @param shiftType
     * @return
     */
    List<String> typeLimitShiftTempletQuery(Long tenantId, String shiftType);

    /**
     * availableShiftTempletQuery-获取有效模板班次
     *
     * @param tenantId
     * @param shiftType
     * @return MtShift
     */
    List<String> availableShiftTempletQuery(Long tenantId, String shiftType);

    /**
     * 根据获取班次模板属性信息
     * @Author peng.yuan
     * @Date 2019/10/10 18:48
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.calendar.domain.vo.MtShiftVO1>
     */
    List<MtShiftVO1> propertyLimitshiftTempletPropertyQuery(Long tenantId, MtShiftVO dto);
}
