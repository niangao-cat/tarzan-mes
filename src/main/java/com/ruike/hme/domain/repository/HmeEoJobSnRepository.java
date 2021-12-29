package com.ruike.hme.domain.repository;

import java.util.List;

import com.ruike.hme.domain.entity.HmeEoJobMaterial;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.api.dto.*;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.order.domain.entity.MtEo;
import com.ruike.hme.domain.entity.HmeEoJobSn;

/**
 * 工序作业平台-SN作业资源库
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 00:04:39
 */
public interface HmeEoJobSnRepository extends BaseRepository<HmeEoJobSn>, AopProxy<HmeEoJobSnRepository> {

    /**
     * 工位扫描-登入校验
     *
     * @param tenantId      租户ID
     * @param hmeEoJobSnDTO 扫描数据
     * @return HmeEoJobSnVO4
     */
    HmeEoJobSnVO4 workcellScan(Long tenantId, HmeEoJobSnDTO hmeEoJobSnDTO);

    /**
     * 进站扫描-产线检查
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/11/26 20:44
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO3
     */
    HmeEoJobSnVO3 inSiteScanCheck(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 进站扫描
     *
     * @param tenantId 租户ID
     * @param dto      扫描数据
     * @return HmeEoJobSnVO
     */
    HmeEoJobSnVO inSiteScan(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 出站校验
     *
     * @param tenantId 租户ID
     * @param dto      校验数据
     */
    void outSiteValidate(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 出站扫描
     *
     * @param tenantId 租户Id
     * @param dto      工序作业
     */
    HmeEoJobSn outSiteScan(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 批量执行订单完成
     *
     * @author yuchao.wang
     * @date 2020/10/28 18:21
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param eoIdList eoIdList
     * @return void
     *
     */
    void siteOutBatchComplete(Long tenantId, String workcellId, List<String> eoIdList);

    /**
     * 批量出站扫描
     *
     * @param tenantId 租户Id
     * @param dto      批量工序作业
     */
    List<HmeEoJobSn> batchOutSiteScan(Long tenantId, HmeEoJobSnVO6 dto);

    /**
     * EO进站时带入物料数据（包括序列物料、批次物料、时效物料）
     *
     * @param tenantId 租户Id
     * @param dto      进站数据
     * @return HmeEoJobSnVO2
     */
    HmeEoJobSnVO2 materialInSite(Long tenantId, HmeEoJobSnVO2 dto);


    /**
     * 根据工位+SN 获取SN作业记录
     *
     * @param tenantId      租户Id
     * @param dto           物料批获取工序作业
     * @return HmeEoJobSn
     */
    HmeEoJobSn materialLotLimitSnJobGet(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 创建SN记录
     *
     * @param tenantId 租户Id
     * @param dto      SN参数
     * @return HmeEoJobSnVO2
     */
    HmeEoJobSnVO2 createSnJob(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 查询工位下未出站工序作业
     *
     * @param tenantId   租户Id
     * @param workcellId 工位Id
     * @return HmeEoJobSnVO
     */
    List<HmeEoJobSnVO> querySnByWorkcell(Long tenantId, String workcellId);

    /**
     * 获取指定工位未出站时效工序作业
     *
     * @param tenantId    租户ID
     * @param workcellId  工位参数
     * @param operationId 工序ID
     * @return 时效工序作业列表
     */
    HmeEoJobTimeSnVO4 queryTimeSnByWorkcell(Long tenantId, String workcellId, String operationId);

    /**
     * 时效SN扫描
     *
     * @param tenantId 租户ID
     * @param dto      工位参数
     * @return 时效工序作业
     */
    HmeEoJobTimeSnVO2 timeSnScan(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 工单号查询
     *
     * @param tenantId 租户Id
     * @param dto      工单号参数
     * @return HmeWorkOrderVO
     */
    List<HmeWorkOrderVO> workOrderQuery(Long tenantId, HmeWorkOrderVO dto);

    /**
     * 预装物料查询
     *
     * @param tenantId 租户Id
     * @param dto      工单号参数
     * @return HmePrepareMaterialVO
     */
    List<HmePrepareMaterialVO> materialQuery(Long tenantId, HmeWorkOrderVO dto);

    /**
     * 已预装物料查询
     *
     * @param tenantId 租户Id
     * @param dto      工单号参数
     * @return HmePrepareMaterialVO
     */
    HmePrepareMaterialVO materialPreparedQuery(Long tenantId, HmeWorkOrderVO dto);

    /**
     * 预装物料进站
     *
     * @param tenantId 租户ID
     * @param dto      进站数据
     * @return 进站数据
     */
    HmeEoJobSnVO2 prepareInSite(Long tenantId, HmeEoJobSnVO2 dto);

    /**
     * 投料
     *
     * @param tenantId 租户Id
     * @param dto      工序作业
     */
    HmeEoJobSnVO3 release(Long tenantId, HmeEoJobSnVO3 dto);
    /**
     *
     * @Description 根据material_lot_id+工艺ID+'作业平台类型等于COS_ FETCH _IN'，查询表中是否存在出站时间为空的记录
     *
     * @author yuchao.wang
     * @date 2020/8/18 11:19
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @param operationId 工艺ID
     *
     */
    boolean checkGettingChipFlag(Long tenantId, String materialLotId, String operationId);

    /**
     *
     * @Description 根据ID查询是否出站 true:已出站 false:未出站
     *
     * @author yuchao.wang
     * @date 2020/8/31 18:32
     * @param tenantId 租户ID
     * @param jobId ID
     * @return boolean
     *
     */
    boolean checkSiteOutById(Long tenantId, String jobId);

    /**
     *
     * @Description 批量新增
     *
     * @author yuchao.wang
     * @date 2020/8/27 16:49
     * @param insertList 新增数据列表
     * @return void
     *
     */
    void myBatchInsert(List<HmeEoJobSn> insertList);

    /**
     *
     * @Description 根据material_lot_id+工艺ID+'作业平台类型等于CHIP_NUM_ENTERING'，查询表中是否存在出站时间为空的记录
     *
     * @author yifan.xiong
     * @date 2020-8-28 17:48:24
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @param operationId 工艺ID
     *
     */
    boolean checkChipEnterFlag(Long tenantId, String materialLotId, String operationId);

    /**
     *
     * @Description 首序作业平台-工单号查询LOV
     *
     * @author yuchao.wang
     * @date 2020/9/1 13:54
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWorkOrderVO>
     *
     */
    List<HmeWorkOrderVO>  workOrderQueryForFirst(Long tenantId, HmeWoLovQueryDTO dto);

    /**
     *
     * @Description 首序作业平台-EO查询LOV
     *
     * @author yuchao.wang
     * @date 2020/9/1 16:30
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO>
     *
     */
    List<HmeEoVO> eoQueryForFirst(Long tenantId, HmeEoLovQueryDTO dto);

    /**
     *
     * @Description 首序作业平台-物料批查询LOV
     *
     * @author yuchao.wang
     * @date 2020/9/1 19:51
     * @param tenantId 租户ID
     * @param materialLotIds 物料批ID集合
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO2>
     *
     */
    List<HmeMaterialLotVO2> materialLotLovQueryForFirst(Long tenantId, List<String> materialLotIds, HmeMaterialLotLovQueryDTO dto);

    /**
     *
     * @Description 根据EOID查询EO编号及相关首序信息
     *
     * @author yuchao.wang
     * @date 2020/9/3 14:41
     * @param tenantId 租户ID
     * @param eoId eoId
     * @return com.ruike.hme.domain.vo.HmeEoVO2
     *
     */
    HmeEoVO2 queryEoActualByEoId(Long tenantId, String eoId);

    /**
     *
     * @Description 首序SN升级-查询要更新的EoJobSn
     *
     * @author yuchao.wang
     * @date 2020/9/3 18:46
     * @param tenantId 租户ID
     * @param upgradeSnDTO 参数
     * @return java.lang.String
     *
     */
    String queryJobIdFirstProcessSnUpgrade(Long tenantId, HmeEoJobFirstProcessUpgradeSnDTO upgradeSnDTO);

    /**
     *
     * @Description 根据在制记录查询未打印的数量
     *
     * @author yuchao.wang
     * @date 2020/9/7 18:58
     * @param tenantId 租户ID
     * @param sourceJobId 在制记录ID
     * @return java.lang.Long
     *
     */
    long queryNotPrintQtyBySourceJobId(Long tenantId, String sourceJobId);

    /**
     * 退料查询
     *
     * @param tenantId  租户ID
     * @param dto       扫描数据
     * @return List<HmeEoJobSnVO9>
     */
    List<HmeEoJobSnVO9> releaseBackQuery(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 退料
     *
     * @param tenantId  租户ID
     * @param dto       退料参数
     * @return List<HmeEoJobSnVO9>
     */
    List<HmeEoJobSnVO9> releaseBack(Long tenantId, HmeEoJobSnVO9 dto);

    /**
     *
     * @Description 工段完工数据统计-工序作业平台/批量工序作业平台/时效工序作业平台/PDA工序作业平台
     *
     * @author yuchao.wang
     * @date 2020/9/21 10:59
     * @param tenantId 租户ID
     * @param dto 参数
     * @param eoJobSns eoJobSns
     * @return void
     *
     */
    void wkcCompleteOutputRecord(Long tenantId, HmeEoJobSnVO3 dto, List<HmeEoJobSn> eoJobSns);

    /**
     *
     * @Description 根据物料批ID查询最近一条未出站的EoJobSn，没有返回NULL
     *
     * @author yuchao.wang
     * @date 2020/9/27 14:06
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    HmeEoJobSn queryLastEoJobSnByMaterialLotId(Long tenantId, String materialLotId);

    /**
     * 物料升级
     *
     * @param tenantId 租户Id
     * @param dto      工序作业
     * @param hmeEoJobMaterial 序列物料
     */
    void snUpgrade(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobMaterial hmeEoJobMaterial);

    /**
     * 物料升级
     *
     * @param tenantId 租户Id
     * @param dto      参数
     */
    void snBatchUpgrade(Long tenantId, HmeEoJobSnVO20 dto);

    /**
     * 刷新
     *
     * @param tenantId 租户ID
     * @param dto      扫描数据
     * @return HmeEoJobSnVO
     */
    HmeEoJobSnVO2 refresh(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 首序作业平台-查询工单 站点下默认EO
     *
     * @author yuchao.wang
     * @date 2020/10/9 23:05
     * @param tenantId 租户ID
     * @param dto 参数
     * @return com.ruike.hme.domain.vo.HmeEoVO
     *
     */
    HmeEoVO defaultEoQueryForFirst(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 校验是否反冲料
     *
     * @author penglin.sui
     * @date 2020/10/14 21:19
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param materialId 物料ID
     * @return Boolean
     *
     */
    Boolean checkBackFlush(Long tenantId,String siteId,String materialId);

    /**
     *
     * @Description 校验是否进行投料校验
     *
     * @author penglin.sui
     * @date 2020/10/15 14:00
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param materialId 物料ID
     * @return Boolean
     *
     */
    Boolean checkIssuedFlag(Long tenantId,String siteId,String materialId);

    /**
     *
     * @Description 获取物料类型
     *
     * @author penglin.sui
     * @date 2020/10/14 21:19
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param materialId 物料ID
     * @return String
     *
     */
    String getMaterialType(Long tenantId,String siteId,String materialId);

    /**
     *
     * @Description 校验是否虚拟件组件
     *
     * @author penglin.sui
     * @date 2020/10/14 21:40
     * @param tenantId 租户ID
     * @param bomComponentId 组件ID
     * @return Boolean
     *
     */
    Boolean checkVirtualComponent(Long tenantId,String bomComponentId);

    /**
     * 库位现有量
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return
     */
    Page<HmeLocatorOnhandQuantityVO> locatorOnhandQuantityQuery(Long tenantId, HmeLocatorOnhandQuantityDTO dto, PageRequest pageRequest);

    /**
     * 反冲料查询
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return
     */
    Page<HmeBackFlushVO> backFlushQuery(Long tenantId, HmeBackFlushDTO dto, PageRequest pageRequest);

    /**
     * 获取虚拟件组件
     * @param tenantId 租户ID
     * @param bomComponentId 组件ID
     * @return List<MtExtendAttrVO1>
     */
    List<MtExtendAttrVO1> getVirtualComponent(Long tenantId, List<String> bomComponentId);

    /**
     *
     * @Description 查询工位下是否有某作业类型未出站数据
     *
     * @author yuchao.wang
     * @date 2020/10/26 15:07
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param jobType 作业类型
     * @return boolean
     *
     */
    boolean checkNotSiteOutByWkcId(Long tenantId, String workcellId, String jobType);

    /**
     * 工序作业平台-工序排队加工-批量
     *
     */
    void eoBatchWorking(Long tenantId, HmeEoJobSnVO14 hmeEoJobSnVO14, String eventRequestId);

    /**
     *
     * @Description 根据JobId批量出站
     *
     * @author yuchao.wang
     * @date 2020/11/5 19:43
     * @param tenantId 租户
     * @param userId 用户ID
     * @param jobIdList jobIdList
     * @return void
     *
     */
    void batchOutSite(Long tenantId, Long userId, List<String> jobIdList);

    /**
     * 批量出战
     *
     * @param tenantId  租户
     * @param userId    用户ID
     * @param jobIdList jobIdList
     * @param remark    备注
     */
    void batchOutSite2(Long tenantId, Long userId, List<String> jobIdList, String remark);

    /**
     *
     * @Description 分页查询炉内条码
     *
     * @author yuchao.wang
     * @date 2020/11/17 10:10
     * @param tenantId 租户ID
     * @param dto 参数
     * @param pageRequest 分页参数
     * @return com.ruike.hme.domain.vo.HmeEoJobTimeSnVO4
     *
     */
    HmeEoJobTimeSnVO4 queryPageTimeSnByWorkcell(Long tenantId, HmeEoJobSnDTO dto, PageRequest pageRequest);

    /**
     *
     * @Description 根据JobId批量出站
     *
     * @author yuchao.wang
     * @date 2020/11/19 1:22
     * @param tenantId 租户
     * @param userId 用户ID
     * @param snLineList 作业列表
     * @return void
     *
     */
    void batchOutSiteWithMaterialLot(Long tenantId, Long userId, List<HmeEoJobSnVO3> snLineList);
}
