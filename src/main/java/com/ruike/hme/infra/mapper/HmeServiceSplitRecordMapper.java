package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO2;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO3;
import com.ruike.hme.api.dto.HmeServiceSplitRecordLineDTO;
import com.ruike.hme.domain.entity.HmeServiceSplitRecord;
import com.ruike.hme.domain.vo.*;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * 售后返品拆机表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 14:21:01
 */
public interface HmeServiceSplitRecordMapper extends BaseMapper<HmeServiceSplitRecord> {

    /**
     * 查询返修拆机信息
     *
     * @param tenantId
     * @param snNum
     * @return java.util.List<com.ruike.hme.api.dto.HmeServiceSplitRecordDTO>
     * @author jiangling.zheng@hand-china.com 2020/9/8 17:27
     */
    HmeServiceSplitRecordDTO selectSplitRecordBySn(@Param("tenantId") Long tenantId,
                                                   @Param("snNum") String snNum);

    /**
     * 查询返修拆机行信息
     *
     * @param tenantId
     * @param parentSplitRecordId
     * @return java.util.List<com.ruike.hme.api.dto.HmeServiceSplitRecordLineDTO>
     * @author jiangling.zheng@hand-china.com 2020/9/8 20:29
     */
    List<HmeServiceSplitRecordLineDTO> selectSplitRecordLine(@Param("tenantId") Long tenantId,
                                                             @Param("parentSplitRecordId") String parentSplitRecordId);

    /**
     * 查询售后登记信息
     *
     * @param tenantId
     * @param snNum
     * @param siteId
     * @return com.ruike.hme.api.dto.HmeServiceSplitRecordDTO2
     * @author jiangling.zheng@hand-china.com 2020/9/8 21:17
     */
    HmeServiceSplitRecordDTO2 selectServiceReceive(@Param("tenantId") Long tenantId,
                                                   @Param("snNum") String snNum,
                                                   @Param("siteId") String siteId);

    /**
     * 查询组件信息
     *
     * @param tenantId
     * @param materialLotCode
     * @return com.ruike.hme.api.dto.HmeServiceSplitRecordDTO3
     * @author jiangling.zheng@hand-china.com 2020/9/9 16:16
     */
    HmeServiceSplitRecordDTO3 selectMaterialLot(@Param("tenantId") Long tenantId,
                                                @Param("materialLotCode") String materialLotCode);

    /**
     * 公司机获取工单
     *
     * @param tenantId
     * @param snNum
     * @return java.lang.String
     * @author jiangling.zheng@hand-china.com 2020/9/18 10:51
     */

    String selectWorkOrderNum(@Param("tenantId") Long tenantId,
                              @Param("snNum") String snNum);

    /**
     * @param tenantId    租户ID
     * @param workOrderId 工单ID
     * @param snNum       sn号
     * @return com.ruike.hme.domain.entity.HmeServiceSplitRecord
     * @Description 查询内部订单号
     * @author yuchao.wang
     * @date 2020/9/28 15:44
     */
    HmeServiceSplitRecord queryWoNumBySnNumAndWoId(@Param("tenantId") Long tenantId,
                                                   @Param("workOrderId") String workOrderId,
                                                   @Param("snNum") String snNum);

    /**
     * @param tenantId      租户ID
     * @param splitRecordId 主键ID
     * @return com.ruike.hme.domain.entity.HmeServiceDataRecord
     * @Description
     * @author yuchao.wang
     * @date 2020/9/28 15:53
     */
    HmeServiceSplitRecord queryWoNumBySplitRecordId(@Param("tenantId") Long tenantId,
                                                    @Param("splitRecordId") String splitRecordId);

    /**
     * 查询物料类别
     *
     * @param tenantId
     * @param materialId
     * @param siteId
     * @return java.lang.String
     * @author jiangling.zheng@hand-china.com 2020/10/4 14:36
     */
    String queryMaterialCategory(@Param("tenantId") Long tenantId,
                                 @Param("materialId") String materialId,
                                 @Param("siteId") String siteId);

    /**
     * 查询工艺路线
     *
     * @param tenantId
     * @param routerName
     * @param siteId
     * @return java.lang.String
     * @author jiangling.zheng@hand-china.com 2020/10/4 15:20
     */
    String queryRouterId(@Param("tenantId") Long tenantId,
                         @Param("routerName") String routerName,
                         @Param("siteId") String siteId);

    /**
     * @param tenantId 租户ID
     * @param woIdList 工单ID
     * @param snList   SN条码
     * @return java.util.List<com.ruike.hme.domain.vo.HmeServiceSplitRecordVO2>
     * @Description 批量查询内部订单号/SAP工单号
     * @author yuchao.wang
     * @date 2020/11/6 11:40
     */
    List<HmeServiceSplitRecordVO2> batchQueryWoNumBySnNumAndWoId(@Param("tenantId") Long tenantId,
                                                                 @Param("woIdList") List<String> woIdList,
                                                                 @Param("snList") List<String> snList);

    /**
     * 查询物料对应的最新的bom
     *
     * @param tenantId   租户
     * @param siteId     站点
     * @param materialId 物料ID
     * @return tarzan.method.domain.entity.MtBom
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 02:58:16
     */
    HmeServiceSplitBomHeaderVO selectLatestBomByMaterial(@Param("tenantId") Long tenantId,
                                                         @Param("siteId") String siteId,
                                                         @Param("materialId") String materialId);

    /**
     * 查询bom行信息
     *
     * @param tenantId 租户
     * @param siteId   站点
     * @param bomId    bom
     * @return java.util.List<com.ruike.hme.domain.vo.HmeServiceSplitBomLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 03:09:17
     */
    List<HmeServiceSplitBomLineVO> selectBomLineList(@Param("tenantId") Long tenantId,
                                                     @Param("siteId") String siteId,
                                                     @Param("bomId") String bomId);

    /**
     * 获取父级货位
     *
     * @param tenantId  租户
     * @param locatorId 货位ID
     * @return tarzan.modeling.domain.entity.MtModLocator
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/29 05:20:16
     */
    MtModLocator selectParentLocator(@Param("tenantId") Long tenantId,
                                     @Param("locatorId") String locatorId);

    /**
     * 查询退库检测列表
     *
     * @param tenantId 租户
     * @param snNum    序列号
     * @param allFlag  显示所有标志
     * @return java.util.List<com.ruike.hme.domain.vo.HmeServiceSplitReturnCheckVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/5 07:41:45
     */
    List<HmeServiceSplitReturnCheckVO> selectReturnCheckList(@Param("tenantId") Long tenantId,
                                                             @Param("snNum") String snNum,
                                                             @Param("allFlag") String allFlag);

    /**
     *
     * @Description 批量查询内部订单号/SAP工单号
     *
     * @author yuchao.wang
     * @date 2020/11/6 11:40
     * @param tenantId 租户ID
     * @param woId 工单ID
     * @param snNum SN条码
     * @return com.ruike.hme.domain.vo.HmeServiceSplitRecordVO2
     *
     */
    HmeServiceSplitRecordVO2 queryOrderNumBySnNumAndWoId(@Param("tenantId") Long tenantId,
                                                         @Param("woId") String woId,
                                                         @Param("snNum") String snNum);

    /**
     *
     * @Description 根据条码查询售后反品拆机信息-进站
     *
     * @author penglin.sui
     * @date 2021/3/2 23:42
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @return com.ruike.hme.domain.entity.HmeServiceSplitRecord
     *
     */
    HmeServiceSplitRecord queryLastSplitRecordOfMaterialLot(@Param("tenantId") Long tenantId,
                                                            @Param("materialLotId") String materialLotId);

    /**
     *
     * @Description 根据条码查询售后反品拆机信息-出站
     *
     * @author penglin.sui
     * @date 2021/3/2 23:42
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @return com.ruike.hme.domain.entity.HmeServiceSplitRecord
     *
     */
    HmeServiceSplitRecord queryLastSplitRecordOfMaterialLot2(@Param("tenantId") Long tenantId,
                                                             @Param("materialLotId") String materialLotId);

    /**
     * 查询拆机信息
     *
     * @param tenantId
     * @param splitRecordId
     * @return com.ruike.hme.domain.vo.HmeServiceSplitRecordVO3
     * @author sanfeng.zhang@hand-china.com 2021/4/6 14:31
     */
    HmeServiceSplitRecordVO3 querySplitRecord(@Param("tenantId") Long tenantId, @Param("splitRecordId") String splitRecordId);

    /**
     * 根据站点和SN查询最早的登记信息
     * 
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param snNum Sn
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/25 02:53:30 
     * @return java.lang.String
     */
    String getServiceReceiveIdBySiteAndSn(@Param("tenantId") Long tenantId, @Param("siteId") String siteId,
                                          @Param("snNum") String snNum);

}
