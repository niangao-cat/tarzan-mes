package com.ruike.hme.infra.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.ruike.hme.domain.entity.HmeEoJobLotMaterial;
import com.ruike.hme.domain.vo.HmeEoJobLotMaterialVO;
import com.ruike.hme.domain.vo.HmeEoJobSnBatchVO10;
import com.ruike.hme.domain.vo.HmeEoJobSnSingleVO5;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.inventory.domain.entity.MtMaterialLot;

/**
 * 工序作业平台-投料Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-03-21 17:55:01
 */
public interface HmeEoJobLotMaterialMapper extends BaseMapper<HmeEoJobLotMaterial> {
    /**
     * 默认发料库位查询
     *
     * @param tenantId         租户id
     * @param workOrderId      产品线ID
     * @return String
     */
    String selectIssuedLocator(@Param("tenantId") Long tenantId,
                               @Param("workOrderId") String workOrderId);

    /**
     * 批次物料查询
     *
     * @param tenantId 租户id
     * @param dto      传入参数
     * @return String
     */
    List<HmeEoJobLotMaterial> queryEoJobLotMaterial(@Param("tenantId") Long tenantId,
                                                    @Param("dto") HmeEoJobLotMaterial dto);

    /**
     * 批次物料查询
     *
     * @param tenantId 租户id
     * @param dto      传入参数
     * @return String
     */
    List<HmeEoJobLotMaterial> queryEoJobLotMaterialOfNotNullMaterialLot(@Param("tenantId") Long tenantId,
                                                                        @Param("dto") HmeEoJobLotMaterial dto);

    BigDecimal queryPrimaryUomQtySum(@Param("tenantId") Long tenantId,
                                     @Param("dto") List<String> dto);

    /**
     * 批次物料查询
     *
     * @param tenantId 租户id
     * @param workcellId  工位ID
     * @param materialId  物料ID
     * @return List<HmeEoJobLotMaterial>
     */
    List<HmeEoJobLotMaterial> queryEoJobLotMaterial2(@Param("tenantId") Long tenantId,
                                                     @Param("workcellId") String workcellId,
                                                     @Param("materialId") String materialId);

    /**
     * 非虚拟件投料批次物料查询
     *
     * @param tenantId 租户id
     * @param workcellId 工位
     * @param jobMaterialIdList 主键ID
     * @return List<HmeEoJobLotMaterial>
     */
    List<HmeEoJobLotMaterial> queryLotMaterialOfNoVirtualRelease(@Param("tenantId") Long tenantId,
                                                                 @Param("workcellId") String workcellId,
                                                                 @Param("jobMaterialIdList") List<String> jobMaterialIdList);

    /**
     * 非虚拟件投料批次物料查询
     *
     * @param tenantId 租户id
     * @param workcellId 工位
     * @param jobMaterialIdList 主键ID
     * @return List<HmeEoJobLotMaterial>
     */
    List<HmeEoJobLotMaterial> queryLotMaterialOfVirtualRelease(@Param("tenantId") Long tenantId,
                                                               @Param("workcellId") String workcellId,
                                                               @Param("jobMaterialIdList") List<String> jobMaterialIdList);

    /**
     *
     * @Description 校验是否为COS类型物料
     *
     * @author yuchao.wang
     * @date 2020/9/30 11:53
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param siteId 站点ID
     * @return java.lang.String
     *
     */
    String queryCosMaterialItemGroup(@Param("tenantId") Long tenantId,
                                     @Param("materialId") String materialId,
                                     @Param("siteId") String siteId);

    /**
     *
     * @Description 校验是否为COS类型物料
     *
     * @author penglin.sui
     * @date 2020/9/30 11:53
     * @param tenantId 租户ID
     * @param materialIdList 物料ID
     * @param siteId 站点ID
     * @return java.lang.String
     *
     */
    List<MtMaterialBasic> queryCosMaterialItemGroups(@Param("tenantId") Long tenantId,
                                                     @Param("materialIdList") List<String> materialIdList,
                                                     @Param("siteId") String siteId);

    /**
     * 验是否为COS类型物料
     *
     * @param tenantId 租户id
     * @param workcellId  工位ID
     * @param materialId  物料ID
     * @param productionVersion 版本
     * @return List<HmeEoJobLotMaterial>
     */
    List<HmeEoJobLotMaterial> queryEoJobLotMaterial3(@Param("tenantId") Long tenantId,
                                                     @Param("workcellId") String workcellId,
                                                     @Param("materialId") String materialId,
                                                     @Param("productionVersion") String productionVersion);

    /**
     * 查询已绑定工位
     *
     * @param tenantId 租户id
     * @param workcellId  工位ID
     * @param materialLotId  条码ID
     * @return String
     */
    String queryHaveBindWorkcell(@Param("tenantId") Long tenantId,@Param("materialLotId") String materialLotId,@Param("workcellId") String workcellId);

    /**
     * 查询已绑定工位
     *
     * @param tenantId 租户id
     * @param materialLotId  条码ID
     * @return String
     */
    List<HmeEoJobSnBatchVO10> queryHaveBindWorkcell2(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     *
     * @Description 批量更新接口表数据
     *
     * @author penglin.sui
     * @date 2020/10/23 15:26
     * @param hmeEoJobLotMaterialVOList 更新数据列表
     * @return void
     *
     */
    void batchUpdateLotMaterial(@Param(value = "userId") Long userId,
                                @Param(value = "hmeEoJobLotMaterialVOList") List<HmeEoJobLotMaterialVO> hmeEoJobLotMaterialVOList);

    /**
     *
     * @Description 批量更新接口表数据
     *
     * @author penglin.sui
     * @date 2020/11/18 23:19
     * @param hmeEoJobLotMaterialList 更新数据列表
     * @return void
     *
     */
    void batchUpdateIsReleased(@Param(value = "userId") Long userId,
                               @Param(value = "hmeEoJobLotMaterialList") List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList);

    /**
     *
     * @Description 物料批查询
     *
     * @author penglin.sui
     * @date 2020/10/23 15:37
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID
     * @return List<MtMaterialLot>
     *
     */
    List<MtMaterialLot> queryMaterialLot(@Param(value = "tenantId") Long tenantId,
                                         @Param(value = "materialLotIdList") List<String> materialLotIdList);

    /**
     * 查询未投料序列物料
     *
     * @param tenantId 租户id
     * @param workcellId 工位
     * @param jobId 工序作业ID
     * @return List<HmeEoJobLotMaterial>
     */
    List<HmeEoJobLotMaterial> selectNotReleaseLotMaterial(@Param("tenantId") Long tenantId,
                                                          @Param("workcellId") String workcellId,
                                                          @Param("jobId") String jobId);

    /**
     * 查询已投料批次物料
     *
     * @param tenantId 租户id
     * @param workcellId 工位
     * @param jobId 工序作业ID
     * @return List<HmeEoJobLotMaterial>
     */
    List<HmeEoJobLotMaterial> selectReleaseLotMaterial(@Param("tenantId") Long tenantId,
                                                       @Param("workcellId") String workcellId,
                                                       @Param("jobId") String jobId);
    /**
     * 批次物料批量删除
     * @author penglin.sui@hand-china.com 2020-11-10 18:40
     * @param jobMaterialIdList
     * @return
     */
    void batchDeleteByPrimary(@Param(value = "jobMaterialIdList") List<String> jobMaterialIdList);

    /**
     * 查询未绑定条码物料
     *
     * @param tenantId 租户id
     * @param workcellId 工位ID
     * @param materialId 物料ID
     * @return HmeEoJobLotMaterial
     */
    HmeEoJobLotMaterial selectNotBindJobMaterial(@Param("tenantId") Long tenantId,
                                                 @Param("workcellId") String workcellId,
                                                 @Param("materialId") String materialId);

    /**
     * 查询数据是否已存在
     *
     * @param tenantId
     * @param workcellId
     * @param materialId
     * @param productionVersion
     * @author jiangling.zheng@hand-china.com 2020/11/14 12:18
     * @return int
     */
    int getEoJobLotMaterialCount(@Param("tenantId") Long tenantId,
                                 @Param("workcellId") String workcellId,
                                 @Param("materialId") String materialId,
                                 @Param("productionVersion") String productionVersion);

    /**
     * 批量更新
     *
     * @param jobMaterialIds
     * @author jiangling.zheng@hand-china.com 2020/11/14 11:58
     * @return void
     */
    void batchUpdateJobLotMaterial(@Param("jobMaterialIds") List<String> jobMaterialIds);

    /**
     * 查询工位绑定条码物料
     *
     * @param tenantId 租户id
     * @param workcellId 工位ID
     * @param siteId 站点ID
     * @return List<HmeEoJobSnSingleVO5>
     */
    List<HmeEoJobSnSingleVO5> selectWkcBindJobMaterial(@Param("tenantId") Long tenantId,
                                                       @Param("workcellId") String workcellId,
                                                       @Param("siteId") String siteId);
}
