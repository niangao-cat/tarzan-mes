package com.ruike.itf.infra.mapper;

import com.ruike.itf.api.dto.SapMaterialVoucherDTO;
import com.ruike.itf.api.dto.ItfObjectTransactionResultQueryDTO;
import com.ruike.itf.domain.entity.ItfObjectTransactionIface;
import com.ruike.itf.domain.vo.ItfMaterialVoucherVO;
import com.ruike.itf.domain.vo.ItfObjectTransactionIfaceVO;
import com.ruike.itf.domain.vo.ItfRouterStepAttrVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 事务汇总接口表Mapper
 *
 * @author yonghui.zhu@hand-china.com 2020-08-11 09:44:09
 */
public interface ItfObjectTransactionIfaceMapper extends BaseMapper<ItfObjectTransactionIface> {

    /**
     * 批量插入接口表
     *
     * @param ifaceList 数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/11 01:48:31
     */
    void batchInsertIface(@Param(value = "ifaceList") List<ItfObjectTransactionIface> ifaceList);

    /**
     * 查询事物表数据非SN数据
     *
     * @param status 固定状态('N','E')
     * @author kejin.liu01@hand-china.com 2020/8/13 16:49
     */
    List<ItfObjectTransactionIface> selectNotSnByStatus(@Param("status") String status);

    /**
     * 查询事物表数据SN数据
     *
     * @param status 固定状态('N','E')
     * @author kejin.liu01@hand-china.com 2020/8/13 16:49
     */
    List<ItfObjectTransactionIface> selectSnByStatus(@Param("status") String status);

    /**
     * 根据主键修改 状态和信息
     *
     * @param itfObjectTransactionIface
     * @author kejin.liu01@hand-china.com 2020/8/15 14:27
     */
    void updateStatusAndMsgByKey(ItfObjectTransactionIface itfObjectTransactionIface);

    /**
     * @param workOrderNumList      工单号
     * @param operationSequenceList 工序号
     * @return java.util.List<com.ruike.itf.domain.vo.ItfRouterStepAttrVO>
     * @Description 查询所有报工事务相关数据
     * @author yuchao.wang
     * @date 2020/8/20 15:02
     */
    List<ItfRouterStepAttrVO> queryAllWorkReportData(@Param("tenantId") Long tenantId,
                                                     @Param("workOrderNumList") List<String> workOrderNumList,
                                                     @Param("operationSequenceList") List<String> operationSequenceList);

    /**
     * 查询事物表数据
     *
     * @param status 固定状态('N','E')
     * @author kejin.liu01@hand-china.com 2020/8/13 16:49
     */
    List<ItfObjectTransactionIface> selectDataByStatusAndType(@Param("status") String status);

    /**
     * @param tenantId 租户ID
     * @return java.util.Date
     * @Description 获取当前的静默期账期
     * @author yuchao.wang
     * @date 2020/9/29 15:05
     */
    Date querySilentAccountSet(Long tenantId);

    void batchUpdateStatusByPrimaryKey(@Param("keyId") List<String> keyId);

    /**
     *
     * @param status
     * @return
     */
    List<ItfObjectTransactionIface> selectObjectTransactionByStatus(@Param("status") String status);

    /**
     * 查询物料凭证
     *
     * @param dto
     * @return
     */
    List<ItfMaterialVoucherVO> selectMaterialVoucher(SapMaterialVoucherDTO dto);


    /**
     * @description 事务基础数据查询
     * @param tenantId
     * @param query
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/11
     * @time 16:54
     * @version 0.0.1
     * @return java.util.List<com.ruike.itf.domain.entity.ItfObjectTransactionIface>
     */
    List<ItfObjectTransactionIfaceVO> selectList(@Param("tenantId") Long tenantId, @Param("dto")ItfObjectTransactionResultQueryDTO query);

    /**
     * @description 查询工单物料
     * @param tenantId 租户ID
     * @param workOrderNumList 工单号集合
     * @author penglin.sui
     * @date 2021/06/30
     * @time 11:27
     * @return java.util.List<com.ruike.itf.domain.entity.ItfObjectTransactionIface>
     */
    List<ItfObjectTransactionIface> selectWorkOrderMaterial(@Param("tenantId") Long tenantId, @Param("workOrderNumList")List<String> workOrderNumList);
}
