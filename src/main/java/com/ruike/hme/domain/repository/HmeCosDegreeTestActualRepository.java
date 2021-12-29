package com.ruike.hme.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeCosDegreeTestActual;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * 偏振度和发散角测试结果资源库
 *
 * @author chaonan.hu@hand-china.com 2021-09-13 10:02:48
 */
public interface HmeCosDegreeTestActualRepository extends BaseRepository<HmeCosDegreeTestActual> {


    /**
     * 获取上次job的执行时间
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/14 09:46:01
     * @return java.util.Date
     */
    Date getLastJobDate(Long tenantId);

    /**
     * 获取此次job需要回写的数据
     *
     * @param tenantId 租户ID
     * @param lastJobDate 上次job执行时间
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/14 03:50:44
     * @return java.util.List<com.ruike.hme.domain.entity.HmeCosDegreeTestActual>
     */
    List<HmeCosDegreeTestActual> getCosDegreeTestActualData(Long tenantId, Date lastJobDate);

    /**
     * 批量回写数据，并记录历史
     *
     * @param tenantId 租户ID
     * @param hmeCosDegreeTestActualList 需要回写的数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/14 05:17:35
     * @return void
     */
    void updateCosDegreeTestActualData(Long tenantId, List<HmeCosDegreeTestActual> hmeCosDegreeTestActualList);
}
