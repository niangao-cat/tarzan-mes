package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.domain.entity.HmeEoJobLotMaterial;
import com.ruike.hme.domain.entity.HmeEoJobTimeMaterial;
import com.ruike.hme.domain.vo.HmeEoJobSnBatchVO10;
import com.ruike.hme.domain.vo.HmeEoJobSnSingleVO5;
import com.ruike.hme.domain.vo.HmeEoJobTimeMaterialVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 工序作业平台-时效投料Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-03-22 17:08:55
 */
public interface HmeEoJobTimeMaterialMapper extends BaseMapper<HmeEoJobTimeMaterial> {
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
     * 时效物料查询
     *
     * @param tenantId  租户id
     * @param dto       传入参数
     * @return List<HmeEoJobTimeMaterial>
     */
    List<HmeEoJobTimeMaterial> queryEoJobTimeMaterial(@Param("tenantId") Long tenantId,
                                                    @Param("dto") HmeEoJobTimeMaterial dto);

    /**
     * 时效物料查询
     *
     * @param tenantId  租户id
     * @param dto       传入参数
     * @return List<HmeEoJobTimeMaterial>
     */
    List<HmeEoJobTimeMaterial> queryEoJobTimeMaterialOfNotNullMaterialLot(@Param("tenantId") Long tenantId,
                                                                          @Param("dto") HmeEoJobTimeMaterial dto);

    /**
     * 时效物料查询
     *
     * @param tenantId 租户id
     * @param workcellId  工位ID
     * @param materialId  物料ID
     * @return List<HmeEoJobTimeMaterial>
     */
    List<HmeEoJobTimeMaterial> qqueryEoJobTimeMaterial2(@Param("tenantId") Long tenantId,
                                                     @Param("workcellId") String workcellId,
                                                     @Param("materialId") String materialId);

    /**
     * 非虚拟件投料时效物料查询
     *
     * @param tenantId 租户id
     * @param workcellId 工位
     * @param jobMaterialIdList 主键ID
     * @return List<HmeEoJobTimeMaterial>
     */
    List<HmeEoJobTimeMaterial> queryTimeMaterialOfNoVirtualRelease(@Param("tenantId") Long tenantId,
                                                                   @Param("workcellId") String workcellId,
                                                                   @Param("jobMaterialIdList") List<String> jobMaterialIdList);

    /**
     * 非虚拟件投料时效物料查询
     *
     * @param tenantId 租户id
     * @param workcellId 工位
     * @param jobMaterialIdList 主键ID
     * @return List<HmeEoJobTimeMaterial>
     */
    List<HmeEoJobTimeMaterial> queryTimeMaterialOfVirtualRelease(@Param("tenantId") Long tenantId,
                                                                 @Param("workcellId") String workcellId,
                                                                 @Param("jobMaterialIdList") List<String> jobMaterialIdList);

    /**
     * 时效物料查询
     *
     * @param tenantId 租户id
     * @param workcellId  工位ID
     * @param materialId  物料ID
     * @param productionVersion 版本
     * @return List<HmeEoJobTimeMaterial>
     */
    List<HmeEoJobTimeMaterial> queryEoJobTimeMaterial3(@Param("tenantId") Long tenantId,
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
     * @Description 批量更新数据
     *
     * @author penglin.sui
     * @date 2020/10/23 16:08
     * @param hmeEoJobTimeMaterialVOList 更新数据列表
     * @return void
     *
     */
    void batchUpdateTimeMaterial(@Param(value = "userId") Long userId,
                                @Param(value = "hmeEoJobTimeMaterialVOList") List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOList);

    /**
     *
     * @Description 批量更新数据
     *
     * @author penglin.sui
     * @date 2020/11/18 23:25
     * @param hmeEoJobTimeMaterialList 更新数据列表
     * @return void
     *
     */
    void batchUpdateIsReleased(@Param(value = "userId") Long userId,
                               @Param(value = "hmeEoJobTimeMaterialList") List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterialList);
    /**
     * 查询未投料序列物料
     *
     * @param tenantId 租户id
     * @param workcellId 工位
     * @param jobId 工序作业ID
     * @return List<HmeEoJobTimeMaterial>
     */
    List<HmeEoJobTimeMaterial> selectNotReleaseTimeMaterial(@Param("tenantId") Long tenantId,
                                                            @Param("workcellId") String workcellId,
                                                            @Param("jobId") String jobId);

    /**
     * 查询未投料序列物料
     *
     * @param tenantId 租户id
     * @param workcellId 工位
     * @param jobId 工序作业ID
     * @author jiangling.zheng@hand-china.com 2020/12/22 18:47
     * @return List<HmeEoJobTimeMaterial>
     */

    List<HmeEoJobTimeMaterial> selectReleaseTimeMaterial(@Param("tenantId") Long tenantId,
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
     * 查询数据是否已存在
     *
     * @param tenantId
     * @param workcellId
     * @param materialId
     * @param productionVersion
     * @author jiangling.zheng@hand-china.com 2020/11/14 12:18
     * @return int
     */
    int getEoJobTimeMaterialCount(@Param("tenantId") Long tenantId,
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
    void batchUpdateJobTimeMaterial(@Param("jobMaterialIds") List<String> jobMaterialIds);

    /**
     * 查询未绑定条码物料
     *
     * @param tenantId 租户id
     * @param workcellId 工位ID
     * @param materialId 物料ID
     * @return HmeEoJobTimeMaterial
     */
    HmeEoJobTimeMaterial selectNotBindJobMaterial(@Param("tenantId") Long tenantId,
                                                  @Param("workcellId") String workcellId,
                                                  @Param("materialId") String materialId);

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
