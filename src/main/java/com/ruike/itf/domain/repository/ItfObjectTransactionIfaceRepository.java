package com.ruike.itf.domain.repository;

import com.ruike.itf.domain.vo.ItfRouterStepAttrVO;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfObjectTransactionIface;

import java.util.*;

/**
 * 事务汇总接口表资源库
 *
 * @author yonghui.zhu@hand-china.com 2020-08-11 09:44:09
 */
public interface ItfObjectTransactionIfaceRepository extends BaseRepository<ItfObjectTransactionIface> {

    /**
     * 批量插入接口表
     *
     * @param ifaceList 数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/11 01:48:31
     */
    void batchInsertIface(List<ItfObjectTransactionIface> ifaceList);

    /**
     * 查询事物表数据非SN数据
     *
     * @param status 固定状态('N','E')
     * @author kejin.liu01@hand-china.com 2020/8/13 16:49
     */
    List<ItfObjectTransactionIface> selectNotSnByStatus(String status);


    /**
     * 查询事物表数据SN数据
     *
     * @param status 固定状态('N','E')
     * @author kejin.liu01@hand-china.com 2020/8/13 16:49
     */
    List<ItfObjectTransactionIface> selectSnByStatus(String status);

    /**
     * 根据主键修改 状态和信息
     *
     * @param itfObjectTransactionIface
     * @author kejin.liu01@hand-china.com 2020/8/15 14:27
     */
    void updateStatusAndMsgByKey(ItfObjectTransactionIface itfObjectTransactionIface);

    /**
     *
     * @Description 查询所有报工事务相关数据
     *
     * @author yuchao.wang
     * @date 2020/8/20 15:02
     * @param workOrderNumList 工单号
     * @param operationSequenceList 工序号
     * @return java.util.List<com.ruike.itf.domain.vo.ItfRouterStepAttrVO>
     *
     */
    List<ItfRouterStepAttrVO> queryAllWorkReportData(Long tenantId,
                                                     List<String> workOrderNumList,
                                                     List<String> operationSequenceList);

    /**
     * 查询事物表数据
     *
     * @param status   固定状态('N','E')
     * @author kejin.liu01@hand-china.com 2020/8/13 16:49
     */
    List<ItfObjectTransactionIface> selectDataByStatusAndType(String status);

    /**
     *
     * @Description 获取当前的静默期账期
     *
     * @author yuchao.wang
     * @date 2020/9/29 15:05
     * @param tenantId 租户ID
     * @return java.util.Date
     *
     */
    Date querySilentAccountSet(Long tenantId);
}
