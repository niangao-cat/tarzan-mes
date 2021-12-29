package com.ruike.itf.infra.repository.impl;

import com.ruike.itf.domain.vo.ItfRouterStepAttrVO;
import com.ruike.itf.infra.mapper.ItfObjectTransactionIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.itf.domain.entity.ItfObjectTransactionIface;
import com.ruike.itf.domain.repository.ItfObjectTransactionIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 事务汇总接口表 资源库实现
 *
 * @author yonghui.zhu@hand-china.com 2020-08-11 09:44:09
 */
@Component
public class ItfObjectTransactionIfaceRepositoryImpl extends BaseRepositoryImpl<ItfObjectTransactionIface> implements ItfObjectTransactionIfaceRepository {

    private final ItfObjectTransactionIfaceMapper mapper;

    @Autowired
    public ItfObjectTransactionIfaceRepositoryImpl(ItfObjectTransactionIfaceMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void batchInsertIface(List<ItfObjectTransactionIface> ifaceList) {
        int batchNum = 500;
        List<List<ItfObjectTransactionIface>> list = InterfaceUtils.splitSqlList(ifaceList, batchNum);
        list.forEach(mapper::batchInsertIface);
    }

    /**
     * 查询事物表数据非SN数据
     *
     * @param status 固定状态('N','E')
     * @author kejin.liu01@hand-china.com 2020/8/13 16:49
     */
    @Override
    public List<ItfObjectTransactionIface> selectNotSnByStatus(String status) {
        return mapper.selectNotSnByStatus(status);
    }

    /**
     * 查询事物表数据SN数据
     *
     * @param status 固定状态('N','E')
     * @author kejin.liu01@hand-china.com 2020/8/13 16:49
     */
    @Override
    public List<ItfObjectTransactionIface> selectSnByStatus(String status) {
        return mapper.selectSnByStatus(status);
    }

    /**
     *
     * 根据主键修改 状态和信息
     * @param itfObjectTransactionIface
     * @author kejin.liu01@hand-china.com 2020/8/15 14:27
     */
    @Override
    public void updateStatusAndMsgByKey(ItfObjectTransactionIface itfObjectTransactionIface) {
        mapper.updateStatusAndMsgByKey(itfObjectTransactionIface);
    }

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
    @Override
    public List<ItfRouterStepAttrVO> queryAllWorkReportData(Long tenantId, List<String> workOrderNumList, List<String> operationSequenceList) {
        return mapper.queryAllWorkReportData(tenantId, workOrderNumList, operationSequenceList);
    }

    /**
     * 查询事物表数据
     *
     * @param status   固定状态('N','E')
     * @author kejin.liu01@hand-china.com 2020/8/13 16:49
     */
    @Override
    public List<ItfObjectTransactionIface> selectDataByStatusAndType(String status) {
        return mapper.selectDataByStatusAndType(status);
    }

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
    @Override
    public Date querySilentAccountSet(Long tenantId) {
        return mapper.querySilentAccountSet(tenantId);
    }


}
