package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeFreezeDocQueryDTO;
import com.ruike.hme.domain.entity.HmeFreezeDoc;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.entity.HmeSelectionDetails;
import com.ruike.hme.domain.vo.*;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;
import java.util.Set;

/**
 * 条码冻结单Mapper
 *
 * @author yonghui.zhu@hand-china.com 2021-02-22 15:44:42
 */
public interface HmeFreezeDocMapper extends BaseMapper<HmeFreezeDoc> {

    /**
     * 查询展示字段列表
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/22 04:30:25
     */
    List<HmeFreezeDocVO> selectRepresentationList(@Param("tenantId") Long tenantId,
                                                  @Param("dto") HmeFreezeDocQueryDTO dto);

    /**
     * 查询物料批数据
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocCreateSnVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/23 04:20:08
     */
    List<HmeMaterialLotVO> selectMaterialLotList(@Param("tenantId") Long tenantId,
                                                 @Param("dto") HmeFreezeDocQueryDTO dto);

    /**
     * COS类型冻结时，如果输入了wafer或金锡比或热沉编号或COS类型，需调用此方法。主要是为了SQL拆分，不然都糅杂在一个SQL里，会出现or exists的情况
     *
     * @param tenantId
     * @param dto
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/2 02:41:21
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO>
     */
    List<HmeMaterialLotVO> selectMaterialLotList2(@Param("tenantId") Long tenantId,
                                                 @Param("dto") HmeFreezeDocQueryDTO dto);

    /**
     * COS类型冻结时，如果输入了wafer或金锡比或热沉编号或COS类型或虚拟号或筛选规则，需调用此方法。主要是为了SQL拆分，不然都糅杂在一个SQL里，会出现or exists的情况
     *
     * @param tenantId
     * @param dto
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/2 02:41:21
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO>
     */
    List<HmeMaterialLotVO> selectMaterialLotList3(@Param("tenantId") Long tenantId,
                                                  @Param("dto") HmeFreezeDocQueryDTO dto);

    /**
     * 查询Cos相关物料批数据
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @param snIds    物料批
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/17 10:36:50
     */
    List<HmeMaterialLotVO> selectCosRelationMaterialLotList(@Param("tenantId") Long tenantId,
                                                            @Param("dto") HmeFreezeDocQueryDTO dto,
                                                            @Param("ids") Iterable<String> snIds);

    /**
     * 追溯芯片投料后情况下的条码
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param snIds 物料批
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/1 09:38:26
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO>
     */
    List<HmeMaterialLotVO> selectChipAfterReleaseMaterialLotList(@Param("tenantId") Long tenantId,
                                                            @Param("dto") HmeFreezeDocQueryDTO dto,
                                                            @Param("ids") List<String> snIds);

    /**
     * 追溯芯片投料前情况下的条码
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param snIds 物料批
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/1 09:49:57
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO>
     */
    List<HmeMaterialLotVO> selectChipBeforeReleaseMaterialLotList(@Param("tenantId") Long tenantId,
                                                                 @Param("dto") HmeFreezeDocQueryDTO dto,
                                                                 @Param("ids") List<String> snIds);

    /**
     * 追溯热沉投料后情况下的条码
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param snIds 物料批
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/1 09:54:33
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO>
     */
    List<HmeMaterialLotVO> selectHotSinkAfterReleaseMaterialLotList(@Param("tenantId") Long tenantId,
                                                                 @Param("dto") HmeFreezeDocQueryDTO dto,
                                                                 @Param("ids") List<String> snIds);

    /**
     * 追溯热沉投料前情况下的条码
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param snIds 物料批
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/1 10:02:50
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO>
     */
    List<HmeMaterialLotVO> selectHotSinkBeforeReleaseMaterialLotList(@Param("tenantId") Long tenantId,
                                                                    @Param("dto") HmeFreezeDocQueryDTO dto,
                                                                    @Param("ids") List<String> snIds);
    /**
     * 追溯金线投料后情况下的条码
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param snIds 物料批
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/1 10:07:37
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO>
     */
    List<HmeMaterialLotVO> selectGoldWireAfterReleaseMaterialLotList(@Param("tenantId") Long tenantId,
                                                                    @Param("dto") HmeFreezeDocQueryDTO dto,
                                                                    @Param("ids") List<String> snIds);

    /**
     * 追溯金线投料前情况下的条码
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param snIds 物料批
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/1 10:07:37
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO>
     */
    List<HmeMaterialLotVO> selectGoldWireBeforeReleaseMaterialLotList(@Param("tenantId") Long tenantId,
                                                                     @Param("dto") HmeFreezeDocQueryDTO dto,
                                                                     @Param("ids") List<String> snIds);

    /**
     * 查询物料批数据
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @param materialLotIds   物料批ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocCreateSnVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/23 04:20:08
     */
    List<String> selectJobFilteredList(@Param("tenantId") Long tenantId,
                                      @Param("dto") HmeFreezeDocQueryDTO dto,
                                      @Param("materialLotIds") List<String> materialLotIds);

    /**
     * 查询作业物料批数据
     *
     * @param tenantId       租户
     * @param materialLotIds 物料批Id
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocCreateSnVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/23 04:20:08
     */
    List<HmeMaterialLotVO> selectJobMaterialLotList(@Param("tenantId") Long tenantId,
                                                    @Param("ids") List<String> materialLotIds);

    /**
     * eoJobSnLotMaterial情况下查询作业物料批数据
     *
     * @param tenantId       租户
     * @param materialLotIds 物料批Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/2 10:33:21
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO>
     */
    List<HmeMaterialLotVO> selectEoJobSnLotMaterialLotList(@Param("tenantId") Long tenantId,
                                                    @Param("ids") List<String> materialLotIds);

    /**
     * eoJobMaterial情况下查询作业物料批数据
     *
     * @param tenantId       租户
     * @param materialLotIds 物料批Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/2 10:33:21
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO>
     */
    List<HmeMaterialLotVO> selectEoJobMaterialLotList(@Param("tenantId") Long tenantId,
                                                           @Param("ids") List<String> materialLotIds);

    /**
     * 查询条码主要信息列表
     *
     * @param tenantId       租户
     * @param freezeTypeTag     冻结类型
     * @param materialLotIds 条码
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocCreateSnVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 09:51:03
     */
    List<HmeFreezeDocCreateSnVO> selectMaterialLotMainList(@Param("tenantId") Long tenantId,
                                                           @Param("freezeTypeTag") String freezeTypeTag,
                                                           @Param("ids") List<String> materialLotIds);

    /**
     * 查询物料批事务列表
     *
     * @param tenantId       租户
     * @param materialLotIds 条码
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocTrxVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 02:38:14
     */
    List<HmeFreezeDocTrxVO> selectMaterialLotTrxList(@Param("tenantId") Long tenantId,
                                                     @Param("ids") Iterable<String> materialLotIds);

    /**
     * 查询解冻物料批事务列表
     *
     * @param tenantId       租户
     * @param materialLotIds 条码
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocTrxVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 02:38:14
     */
    List<HmeFreezeDocTrxVO> selectMaterialLotUnfreezeTrxList(@Param("tenantId") Long tenantId,
                                                             @Param("ids") Iterable<String> materialLotIds);

    /**
     * 查询所有冻结货位
     *
     * @param tenantId 租户
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 02:50:02
     */
    List<MtModLocator> selectFreezeLocatorList(@Param("tenantId") Long tenantId);

    /**
     * 查询关联的装载信息
     *
     * @param tenantId       租户
     * @param dto            条件
     * @param materialLotIds 条码
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/17 11:24:46
     */
    List<HmeMaterialLotLoad> selectRelationLoadList(@Param("tenantId") Long tenantId,
                                                    @Param("dto") HmeFreezeDocQueryDTO dto,
                                                    @Param("ids") Iterable<String> materialLotIds);

    /**
     * 查询关联的装载信息-挑选后已投料
     *
     * @param tenantId 租户
     * @param dto 条件
     * @param materialLotIds 条码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/27 04:27:20
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     */
    List<HmeMaterialLotLoad> selectRelationLoadList2(@Param("tenantId") Long tenantId,
                                                     @Param("dto") HmeFreezeDocQueryDTO dto,
                                                     @Param("ids") Iterable<String> materialLotIds);

    /**
     * 查询关联的装载信息-挑选后未装盒
     *
     * @param tenantId 租户
     * @param dto 条件
     * @param materialLotIds 条码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/27 04:27:20
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     */
    List<HmeMaterialLotLoad> selectRelationLoadList3(@Param("tenantId") Long tenantId,
                                                     @Param("dto") HmeFreezeDocQueryDTO dto,
                                                     @Param("ids") Iterable<String> materialLotIds);

    /**
     * 批量更新物料批装载信息的ATTRIBUTE14
     *
     * @param materialLotLoadIds
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/2 11:21:09
     * @return void
     */
    void batchUpdateMaterialLotLoad(@Param("materialLotLoadIds") List<String> materialLotLoadIds,
                                    @Param("flag") String flag);

    /**
     * 批量根据物料ID查询编码和描述
     *
     * @param tenantId 租户ID
     * @param materialIds 物料ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/30 06:13:57
     * @return java.util.List<tarzan.material.domain.entity.MtMaterial>
     */
    List<MtMaterial> materialInfoBatchQuery(@Param("tenantId") Long tenantId,
                                            @Param("materialIds") List<String> materialIds);

    /**
     * 查询投料前的旧盒号和新盒号
     * 
     * @param tenantId  租户ID
     * @param materialLotIdList 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/3 01:06:13 
     * @return java.util.List<com.ruike.hme.domain.entity.HmeSelectionDetails>
     */
    List<HmeSelectionDetails> selectBeforeReleaseMaterialLotList(@Param("tenantId") Long tenantId,
                                                                 @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 查询投料后的新盒号
     *
     * @param tenantId  租户ID
     * @param materialLotIdList 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/3 01:06:13
     * @return java.util.List<com.ruike.hme.domain.entity.HmeSelectionDetails>
     */
    List<String> selectAfterReleaseMaterialLotList(@Param("tenantId") Long tenantId,
                                                   @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 批量根据物料批Id查询mfFlag
     *
     * @param tenantId
     * @param materialLotIdList
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/3 01:29:54
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO>
     */
    List<HmeMaterialLotVO> mfFlagBatchQuery(@Param("tenantId") Long tenantId,
                                            @Param("materialLotIdList") List<String> materialLotIdList);
    
    /**
     * 查询有效的、有货位、有仓库的条码
     * 
     * @param tenantId 租户ID
     * @param materialLotIdList 条码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/3 05:02:27 
     * @return java.util.List<java.lang.String>
     */
    List<String> selectFreezeMaterialLotIdList(@Param("tenantId") Long tenantId,
                                               @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据冻结单头查询冻结行表信息
     *
     * @param tenantId 租户ID
     * @param freezeDocId 冻结单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/3 06:05:01
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocVO2>
     */
    List<HmeFreezeDocVO2> selectFreeLineInfo(@Param("tenantId") Long tenantId,
                                             @Param("freezeDocId") String freezeDocId);

    /**
     * 批量更新冻结行表的冻结标识
     *
     * @param freezeDocLineIds 冻结行表主键
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/3 10:21:35
     * @return void
     */
    void batchUpdataFreezeDocLine(@Param("freezeDocLineIds") List<String> freezeDocLineIds);

    /**
     * 根据热沉编码查询新盒号
     *
     * @param tenantId 租户ID
     * @param hotSinkCode 热沉编号集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/3 05:02:27
     * @return java.util.List<java.lang.String>
     */
    List<String> getNewMaterialLotByHotSinkCode(@Param("tenantId") Long tenantId,
                                                @Param("hotSinkCode") String hotSinkCode);

    /**
     * 根据热沉编码查询挑选前的旧盒号
     *
     * @param tenantId 租户ID
     * @param hotSinkCode 热沉编号集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/3 05:02:27
     * @return java.util.List<java.lang.String>
     */
    List<String> getMaterialLotByHotSinkCode2(@Param("tenantId") Long tenantId,
                                             @Param("hotSinkCode") String hotSinkCode);
}
