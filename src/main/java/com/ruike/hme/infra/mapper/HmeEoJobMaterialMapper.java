package com.ruike.hme.infra.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.ruike.hme.domain.entity.HmeEoJobMaterial;
import com.ruike.hme.domain.vo.HmeEoJobMaterialVO;
import com.ruike.hme.domain.vo.HmeEoJobSnBatchVO9;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import com.ruike.hme.domain.vo.HmeEoJobSnVO9;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.material.domain.vo.MtMaterialVO;

/**
 * 工序作业平台-投料Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 21:41:23
 */
public interface HmeEoJobMaterialMapper extends BaseMapper<HmeEoJobMaterial> {
    /**
     * 序列号已投料物料
     * @param tenantId 租户id
     * @param dto      工序作业参数
     * @return MtModLocator
     */
    List<HmeEoJobSnVO9> selectReleaseEoJobMaterial(@Param("tenantId")Long tenantId,
                                                   @Param("dto") HmeEoJobSnVO3 dto);

    /**
     * 序列号已投料物料-返修
     * @param tenantId 租户id
     * @param dto      工序作业参数
     * @return MtModLocator
     */
    List<HmeEoJobSnVO9> selectReleaseEoJobMaterialOfRework(@Param("tenantId")Long tenantId,
                                                           @Param("dto") HmeEoJobSnVO3 dto);

    /**
     * 序列号物料已投料之和
     * @param tenantId 租户id
     * @param dto    传入参数
     * @return BigDecimal
     */
    BigDecimal selectReleaseQtySum(@Param("tenantId") Long tenantId,
                                   @Param("dto") HmeEoJobSnVO9 dto);

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
     * 非虚拟件投料批次物料查询
     *
     * @param tenantId    租户id
     * @param workcellId  工位ID
     * @param jobId       作业ID
     * @param jobMaterialIdList  主键ID
     * @return String
     */
    List<HmeEoJobMaterial> queryMaterialOfNoVirtualRelease(@Param("tenantId") Long tenantId,
                                                           @Param("workcellId") String workcellId,
                                                           @Param("jobId") String jobId,
                                                           @Param("jobMaterialIdList") List<String> jobMaterialIdList);

    /**
     * 虚拟件投料批次物料查询
     *
     * @param tenantId    租户id
     * @param workcellId  工位ID
     * @param jobId       作业ID
     * @param jobMaterialIdList  主键ID
     * @return String
     */
    List<HmeEoJobMaterial> queryMaterialOfVirtualRelease(@Param("tenantId") Long tenantId,
                                                         @Param("workcellId") String workcellId,
                                                         @Param("jobId") String jobId,
                                                         @Param("jobMaterialIdList") List<String> jobMaterialIdList);

    /**
     * 查询虚拟件组件
     *
     * @param tenantId   租户id
     * @param jobIdList  工序作业ID
     * @return String
     */
    List<HmeEoJobMaterial> selectVirtualComponent(@Param("tenantId") Long tenantId,
                                                  @Param("jobIdList") List<String> jobIdList);

    /**
     * 查询条码已绑定数量
     *
     * @param tenantId         租户id
     * @param materialLotCode  条码编码
     * @param jobMaterialId    主键ID
     * @return int
     */
    List<String> selectMaterialLotBindMaterialLot(@Param("tenantId") Long tenantId,
                                                  @Param("materialLotCode") String materialLotCode,
                                                  @Param("jobMaterialId") String jobMaterialId);

    /**
     * 查询条码已绑定SN
     *
     * @param tenantId         租户id
     * @param materialLotCode  条码编码
     * @return int
     */
    List<HmeEoJobSnBatchVO9> selectMaterialLotBindMaterialLot2(@Param("tenantId") Long tenantId,
                                                               @Param("materialLotCode") String materialLotCode);

    /**
     * 查询条码已绑定SN-返修
     *
     * @param tenantId         租户id
     * @param materialLotCode  条码编码
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnBatchVO9>
     */
    List<HmeEoJobSnBatchVO9> selectMaterialLotBindMaterialLot2OfRework(@Param("tenantId") Long tenantId,
                                                                       @Param("materialLotCode") String materialLotCode);

    /**
     * 查询投料数量大于0的序列物料
     *
     * @param tenantId   租户id
     * @param jobId 工序作业ID
     * @param workcellId 工位ID
     * @return String
     */
    List<HmeEoJobMaterial> selectJobMaterial(@Param("tenantId") Long tenantId,
                                             @Param("jobId") String jobId,
                                             @Param("workcellId") String workcellId);

    /**
     * 查询未投料序列物料
     *
     * @param tenantId   租户id
     * @param jobId 工序作业ID
     * @param materialIdList 物料ID
     * @return List<HmeEoJobMaterial>
     */
    List<HmeEoJobMaterial> selectNotIssuedJobMaterial(@Param("tenantId") Long tenantId,
                                                      @Param("jobId") String jobId,
                                                      @Param("materialIdList") List<String> materialIdList);

    /**
     * 查询已投料序列物料
     *
     * @param tenantId   租户id
     * @param jobId 工序作业ID
     * @param materialIdList 物料ID
     * @return List<HmeEoJobMaterial>
     */
    List<HmeEoJobMaterial> selectIssuedJobMaterial(@Param("tenantId") Long tenantId,
                                                   @Param("jobId") String jobId,
                                                   @Param("materialIdList") List<String> materialIdList);

    /**
     * 序列物料保存
     * @author penglin.sui@hand-china.com 2020-10-23 14:18
     * @param tableName
     * @param jobMaterialList
     * @return
     */
    void batchInsertJobMaterial(@Param(value = "tableName") String tableName,
                               @Param(value = "jobMaterialList") List<HmeEoJobMaterial> jobMaterialList);

    /**
     * 查询未投料序列物料
     *
     * @param tenantId   租户id
     * @param jobId 工序作业ID
     * @param siteId 站点ID
     * @return List<HmeEoJobMaterial>
     */
    List<HmeEoJobMaterial> selectNotReleaseJobMaterial(@Param("tenantId") Long tenantId,
                                                       @Param("jobId") String jobId,
                                                       @Param("siteId") String siteId);


    /**
     * 查询未投料序列物料
     *
     * @param tenantId
     * @param jobId
     * @author jiangling.zheng@hand-china.com 2020/11/16 21:06
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobMaterial>
     */
    List<HmeEoJobMaterial> selectNotReleaseEoJobMaterial(@Param("tenantId") Long tenantId,
                                                         @Param("jobId") String jobId);

    /**
     * 查询已投料序列物料
     *
     * @param tenantId
     * @param jobId
     * @author jiangling.zheng@hand-china.com 2020/12/22 18:57
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobMaterial>
     */
    List<HmeEoJobMaterial> selectReleaseJobMaterial(@Param("tenantId") Long tenantId,
                                                    @Param("jobId") String jobId);


    /**
     * 序列物料批量删除
     * @author penglin.sui@hand-china.com 2020-11-10 18:40
     * @param jobMaterialIdList
     * @return
     */
    void batchDeleteByPrimary(@Param(value = "jobMaterialIdList") List<String> jobMaterialIdList);

    /**
     * 批量更新
     *
     * @param jobMaterialIds
     * @author jiangling.zheng@hand-china.com 2020/11/14 11:58
     * @return void
     */
    void batchUpdateJobMaterial(@Param("jobMaterialIds") List<String> jobMaterialIds);

    /**
     * 查询未绑定条码物料
     *
     * @param tenantId 租户id
     * @param jobId 工序作业ID
     * @param materialId 物料ID
     * @return HmeEoJobMaterial
     */
    HmeEoJobMaterial selectNotBindJobMaterial(@Param("tenantId") Long tenantId,
                                              @Param("jobId") String jobId,
                                              @Param("materialId") String materialId);

    /**
     *
     * @Description 批量更新数据
     *
     * @author penglin.sui
     * @date 2020/11/18 23:24
     * @param hmeEoJobMaterialList 更新数据列表
     * @return void
     *
     */
    void batchUpdateIsReleased(@Param(value = "userId") Long userId,
                               @Param(value = "hmeEoJobMaterialList") List<HmeEoJobMaterial> hmeEoJobMaterialList);

    List<HmeEoJobMaterialVO> selectAllMaterialLotBindMaterialLot(@Param("tenantId") Long tenantId,
                                                           @Param("materialLotCode") String materialLotCode,
                                                           @Param("jobMaterialId")String jobMaterialId);

    /**
     * 获取Sn顶层下条码作业
     *
     * @param tenantId
     * @param snNum
     * @author sanfeng.zhang@hand-china.com 2021/4/11 22:15
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnVO9>
     */
    List<HmeEoJobSnVO9> queryReleaseEoJobSnBySnNumOfRework(@Param("tenantId") Long tenantId,
                                                           @Param("snNum") String snNum);

    /**
     * 序列号已投料物料-返修2
     *
     * @param tenantId   租户id
     * @param dto       工序作业参数
     * @param jobIdList  作业id
     * @author sanfeng.zhang@hand-china.com 2021/4/11 22:18
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnVO9>
     */
    List<HmeEoJobSnVO9> selectReleaseEoJobMaterialOfRework2(@Param("tenantId")Long tenantId,
                                                              @Param("dto") HmeEoJobSnVO3 dto,
                                                              @Param("jobIdList") List<String> jobIdList);
}
