package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.*;
import com.ruike.qms.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author tong.li
 * @Date 2020/5/12 11:35
 * @Version 1.0
 */
public interface QmsIqcCheckPlatformMapper {
    /**
     * @param tenantId 1
     * @param qmsIqcCheckPlatformDTO 2
     * @return : java.util.List<com.ruike.qms.api.dto.QmsIqcCheckPlatformMainReturnDTO>
     * @Description: IQC检验平台  主界面查询
     * @author: tong.li
     * @date 2020/5/12 11:41
     * @version 1.0
     */
    List<QmsIqcCheckPlatformMainReturnDTO> mainQuery(@Param("tenantId") Long tenantId , @Param("dto") QmsIqcCheckPlatformDTO qmsIqcCheckPlatformDTO,
                                                     @Param("userId") Long userId);

    /**
     * @param tenantId 1
     * @param iqcCheckPlatformIqcQueryDTO 2
     * @return : java.util.List<com.ruike.qms.api.dto.QmsIqcCheckPlatformIqcReturnDTO>
     * @Description:  IQC检验平台  头查询
     * @author: tong.li
     * @date 2020/5/12 15:20
     * @version 1.0
     */
    List<QmsIqcCheckPlatformIqcReturnDTO> iqcHeadQuery(@Param("tenantId") Long tenantId , @Param("dto") QmsIqcCheckPlatformIqcQueryDTO iqcCheckPlatformIqcQueryDTO);


    /**
     * @param tenantId 1
     * @param sampleCode 2
     * @return : java.lang.String
     * @Description: 根据抽样条码 获取instructionId
     * @author: tong.li
     * @date 2020/5/12 15:48
     * @version 1.0
     */
    String queryInstructionId(@Param("tenantId") Long tenantId , @Param("sampleCode") String sampleCode);


    /**
     * @param tenantId 1
     * @param iqcHeaderId 2
     * @return : java.util.List<com.ruike.qms.api.dto.QmsIqcCheckPlatformIqcReturnLineDTO>
     * @Description: IQC检验平台 行查询
     * @author: tong.li
     * @date 2020/5/12 17:25
     * @version 1.0
     */
    List<QmsIqcCheckPlatformIqcReturnLineDTO> iqcLineQuery(@Param("tenantId") Long tenantId , @Param("iqcHeaderId") String  iqcHeaderId,@Param("addedFlag") String addedFlag);

    /**
     * @param tenantId 1
     * @param iqcLineId 2
     * @return : java.util.List<com.ruike.qms.api.dto.QmsIqcCheckPlatformIqcReturnDetailDTO>
     * @Description: IQC检验界面  明细查询
     * @author: tong.li
     * @date 2020/5/19 14:59
     * @version 1.0
     */
    List<QmsIqcCheckPlatformIqcReturnDetailDTO> iqcDetailQuery(@Param("tenantId") Long tenantId , @Param("iqcLineId") String  iqcLineId);


    /**
     * @param tenantId 1
     * @param tagGroupId 2
     * @return : java.util.List<com.ruike.qms.api.dto.QmsIqcCheckPlatformCreateBringDTO>
     * @Description: 新建页面点击新建按钮后跳转页面   选择检验组LOV后带出信息
     * @author: tong.li
     * @date 2020/5/14 15:24
     * @version 1.0
     */
    List<QmsIqcCheckPlatformCreateBringDTO> lovBringDataQuery(@Param("tenantId") Long tenantId ,@Param("tagGroupId") String tagGroupId);


    Long queryLineMaxNum(@Param("tenantId") Long tenantId ,@Param("iqcHeaderId") String iqcHeaderId);

    /**
     * @param tenantId 1
     * @param dto 2
     * @return : java.util.List<com.ruike.qms.api.dto.QmsIqcCheckPlatformDTO2>
     * @Description: 抽样方案类型LOV
     * @author: tong.li
     * @date 2020/5/21 20:23
     * @version 1.0
     */
    List<QmsIqcCheckPlatformDTO2> queryLov(@Param("tenantId")Long tenantId,@Param("dto") QmsIqcCheckPlatformDTO2 dto);

    /**
     * 通过instructionId获取对应的actualId
     *
     * @param tenantId 租户ID
     * @param instructionId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/16 11:14:19
     * @return java.lang.String
     */
    String getActualIdByInstruction(@Param("tenantId")Long tenantId, @Param("instructionId")String instructionId);

    /**
     * 更新mt_instruct_act_detail_attr表的扩展值为空
     *
     * @param tenantId 租户ID
     * @param actualDetailId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/16 14:17:43
     * @return void
     */
    void updateAttr(@Param("tenantId")Long tenantId, @Param("actualDetailId")String actualDetailId);

    /**
     * 更新mt_instruct_act_detail_attr表的扩展值
     *
     * @param tenantId 租户ID
     * @param actualDetailId
     * @param attrValue 扩展值
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/16 14:17:43
     * @return void
     */
    void updateAttr2(@Param("tenantId")Long tenantId, @Param("actualDetailId")String actualDetailId, @Param("attrValue")String attrValue);

    /**
     *
     *
     * @param tenantId
     * @param actualDetailId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/16 03:10:48
     * @return int
     */
    int getAttrCount(@Param("tenantId")Long tenantId, @Param("actualDetailId")String actualDetailId);

    /**
     * 插入扩展表数据
     *
     * @param tenantId
     * @param attrId
     * @param actualDetailId
     * @param attrValue
     * @param cid
     * @param userId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/16 03:31:45
     * @return void
     */
    void insertAttr(@Param("tenantId") Long tenantId, @Param("attrId") String attrId, @Param("actualDetailId") String actualDetailId,
                    @Param("attrValue") String attrValue, @Param("cid") String cid, @Param("userId") String userId);

    /**
     * 根据iqc头Id查询物料批列表
     *
     * @param tenantId    租户
     * @param iqcHeaderId iqc头
     * @param supplierLot
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcMaterialLotVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/22 02:24:18
     */
    List<QmsIqcMaterialLotVO> selectMaterialLotListByIqcHeader(@Param("tenantId") Long tenantId, @Param("iqcHeaderId") String iqcHeaderId, @Param("supplierLot") String supplierLot);

    /**
     * 根据送货单行Id查询实绩明细下条码的供应商批次
     *
     * @param tenantId 租户ID
     * @param instructionId 送货单行Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/22 21:17:56
     * @return java.util.List<java.lang.String>
     */
    List<String> getSupplierLotByInstructionId(@Param("tenantId") Long tenantId, @Param("instructionId") String instructionId);

    /**
     * 导出数据查询
     *
     * @param tenantId 租户ID
     * @param qmsIqcCheckPlatformDTO 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/11 11:11:09
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcCheckPlatformExportVO>
     */
    List<QmsIqcCheckPlatformExportVO> exportQuery(@Param("tenantId") Long tenantId, @Param("dto") QmsIqcCheckPlatformDTO qmsIqcCheckPlatformDTO,
                                                  @Param("userId") Long userId);

    /**
     * 根据物料id与版本查询检验组描述
     *
     * @param materialId      物料id
     * @param materialVersion 物料版本
     * @return 检验组描述
     */
    List<QmsIqcTagGroupVO> queryTagGroupDescriptionByMaterial(@Param("materialId") String materialId, @Param("materialVersion") String materialVersion);

    /**
     * 根据物料id与版本查询检验组描述
     *
     * @param materialId      物料id
     * @param materialVersion 物料版本
     * @return 检验组描述
     */
    List<QmsIqcTagGroupVO> queryTagGroupDescriptionByMaterial2(@Param("materialId") String materialId, @Param("materialVersion") String materialVersion, @Param("inspectionType") String inspectionType);
    /**
     * 等级汇总
     *
     * @param tenantId
     * @param instructionIdList
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcGradeVO>
     * @author sanfeng.zhang@hand-china.com 2020/12/11 15:38
     */
    List<QmsIqcGradeVO> batchQueryGrade(@Param("tenantId") Long tenantId, @Param("instructionIdList") List<String> instructionIdList);

    /**
     * IQC检验平台-质量文件
     *
     * @param tenantId
     * @param docLineId
     * @return java.util.List<com.ruike.qms.domain.vo.QmsQualityFileVO>
     * @author sanfeng.zhang@hand-china.com 2021/4/7 16:40
     */
    List<QmsQualityFileVO> qualityFileQuery(@Param("tenantId") Long tenantId, @Param("docLineId") String docLineId);

    /**
     * IQC检验平台-导入信息
     *
     * @param tenantId
     * @param fileUrl
     * @return java.util.List<com.ruike.qms.domain.vo.QmsQualityFileVO2>
     * @author sanfeng.zhang@hand-china.com 2021/4/7 17:25
     */
    List<QmsQualityFileVO2> qualityFileImportQuery(@Param("tenantId") Long tenantId, @Param("fileUrl") String fileUrl);
}
