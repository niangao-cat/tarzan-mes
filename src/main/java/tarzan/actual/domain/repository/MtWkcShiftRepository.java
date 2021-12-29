package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.vo.MtWkcShiftVO;
import tarzan.actual.domain.vo.MtWkcShiftVO2;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.actual.domain.vo.MtWkcShiftVO4;
import tarzan.actual.domain.vo.MtWkcShiftVO5;
import tarzan.actual.domain.vo.MtWkcShiftVO6;
import tarzan.actual.domain.vo.MtWkcShiftVO7;
import tarzan.actual.domain.vo.MtWkcShiftVO8;
import tarzan.actual.domain.vo.MtWkcShiftVO9;

/**
 * 开班实绩数据表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:00:23
 */
public interface MtWkcShiftRepository extends BaseRepository<MtWkcShift>, AopProxy<MtWkcShiftRepository> {

    /**
     * 获取后一班次
     *
     * @param tenantId
     * @param wkcShiftId
     * @return
     */
    List<String> nextShiftGet(Long tenantId, String wkcShiftId);


    /**
     * 获取工作单元特定时间段内班次
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> timePeriodLimitShiftQuery(Long tenantId, MtWkcShiftVO5 dto);


    /**
     * 获取工作单元特定时间段内特定班次
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> timePeriodShiftCodeLimitShiftQuery(Long tenantId, MtWkcShiftVO dto);


    /**
     * 获取前一班次
     *
     * @param tenantId
     * @param wkcShiftId
     * @return
     */
    List<String> previousShiftGet(Long tenantId, String wkcShiftId);


    /**
     * 获取工作单元的当前班次
     *
     * @param tenantId
     * @param workcellId
     * @return
     */
    MtWkcShiftVO3 wkcCurrentShiftQuery(Long tenantId, String workcellId);


    /***
     * 获取最近的班次
     *
     * @param tenantId
     * @param workcellId
     * @return
     */
    List<String> wkcLimitLastShiftGet(Long tenantId, String workcellId);


    /**
     * 获取工作单元特定时间段内班次时间合计
     *
     * @param tenantId
     * @param dto
     * @return
     */
    Long timePeriodShiftTimeSum(Long tenantId, MtWkcShiftVO2 dto);


    /**
     * 获取特定工作单元班次
     *
     * @param tenantId
     * @param workcellId
     * @return
     */
    List<String> wkcLimitShiftQuery(Long tenantId, String workcellId);


    /**
     * 获取特定日期及工作单元的班次
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> wkcAndDateLimitShiftQuery(Long tenantId, MtWkcShiftVO6 dto);


    /**
     * 工作单元停班
     *
     * @param tenantId
     * @param workcellId
     */
    void wkcShiftEnd(Long tenantId, String workcellId);


    /**
     * 获取工位班次属性
     *
     * @param tenantId
     * @param wkcShiftId
     * @return
     */
    MtWkcShift wkcShiftGet(Long tenantId, String wkcShiftId);


    /**
     * 工作单元班次清除
     *
     * @param tenantId
     * @param dto
     */
    void wkcShiftBack(Long tenantId, MtWkcShiftVO7 dto);


    /**
     * 班次时间计算
     *
     * @param tenantId
     * @param dto
     * @return
     */
    Long wkcShiftTimeCalculate(Long tenantId, MtWkcShiftVO4 dto);


    /**
     * 获取特定日期及编码的班次
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String dateAndShiftCodeLimitShiftQuery(Long tenantId, MtWkcShiftVO7 dto);


    /**
     * 获取特定日期班次
     *
     * @param tenantId
     * @param shiftDate
     * @return
     */
    List<String> dateLimitShiftQuery(Long tenantId, String shiftDate);


    /**
     * 工作单元开班
     *
     * @param tenantId
     * @param dto
     */
    String wkcShiftStart(Long tenantId, MtWkcShiftVO7 dto);


    /**
     * 交接班
     *
     * @param tenantId
     * @param workcellId
     */
    String wkcShiftHandover(Long tenantId, String workcellId);

    /**
     * 根据属性获取工位班次属性信息
     * @Author peng.yuan
     * @Date 2019/10/10 19:08
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.actual.domain.vo.MtWkcShiftVO9>
     */
    List<MtWkcShiftVO9> propertyLimitWkcShiftPropertyQuery(Long tenantId, MtWkcShiftVO8 dto);
}
