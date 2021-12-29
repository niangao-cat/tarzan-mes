package com.ruike.qms.app.service;

import com.ruike.qms.api.dto.*;
import com.ruike.qms.domain.entity.QmsIqcLine;
import com.ruike.qms.domain.vo.*;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;
import com.ruike.wms.domain.vo.WmsMaterialLotExtendAttrVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * IQC检验平台
 *
 * @author tong.li 2020/5/12 10:32
 */
public interface QmsIqcCheckPlatformService {
    /**
     * IQC检验平台 主界面查询
     *
     * @param tenantId               租户ID
     * @param pageRequest            分页参数
     * @param qmsIqcCheckPlatformDTO 查询参数
     * @return io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsIqcCheckPlatformMainReturnDTO>
     * @author tong.li 2020/5/12 11:33
     */
    Page<QmsIqcCheckPlatformMainReturnDTO> mainQuery(Long tenantId, PageRequest pageRequest, QmsIqcCheckPlatformDTO qmsIqcCheckPlatformDTO);


    /**
     * IQC检验界面  头查询
     *
     * @param tenantId                    租户ID
     * @param pageRequest                 分页参数
     * @param iqcCheckPlatformIqcQueryDTO 查询参数
     * @return io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsIqcCheckPlatformIqcReturnDTO>
     * @author tong.li 2020/5/12 15:15
     */
    Page<QmsIqcCheckPlatformIqcReturnDTO> iqcHeadQuery(Long tenantId, PageRequest pageRequest, QmsIqcCheckPlatformIqcQueryDTO iqcCheckPlatformIqcQueryDTO);


    /**
     * IQC检验界面  行查询
     *
     * @param tenantId    租户ID
     * @param pageRequest 分页参数
     * @param iqcHeaderId 头ID
     * @return io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsIqcCheckPlatformIqcReturnLineDTO>
     * @author tong.li 2020/5/12 17:22
     */
    List<QmsIqcCheckPlatformIqcReturnLineDTO> iqcLineQuery(Long tenantId, PageRequest pageRequest, String iqcHeaderId);

    /**
     * IQC检验界面  明细查询
     *
     * @param tenantId    租户ID
     * @param pageRequest 分页参数
     * @param iqcLineId   行ID
     * @return io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsIqcCheckPlatformIqcReturnDetailDTO>
     * @author tong.li 2020/5/19 14:58
     */
    Page<QmsIqcCheckPlatformIqcReturnDetailDTO> iqcDetailQuery(Long tenantId, PageRequest pageRequest, String iqcLineId);

    /**
     * 新建页面进入后自动查询行信息
     *
     * @param tenantId    租户ID
     * @param pageRequest 分页参数
     * @param iqcHeaderId 头ID
     * @return io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsIqcCheckPlatformIqcReturnLineDTO>
     * @author tong.li 2020/5/14 11:17
     */
    Page<QmsIqcCheckPlatformIqcReturnLineDTO> createPageLineQuery(Long tenantId, PageRequest pageRequest, String iqcHeaderId);

    /**
     * 新建页面点击新建按钮后跳转页面   选择检验组LOV后带出信息
     *
     * @param tenantId    租户ID
     * @param pageRequest 分页参数
     * @param tagGroupId  检验组ID
     * @param iqcHeaderId 头ID
     * @return : io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsIqcCheckPlatformCreateBringDTO>
     * @author tong.li 2020/5/14 11:31
     */
    Page<QmsIqcCheckPlatformCreateBringDTO> lovBringDataQuery(Long tenantId, PageRequest pageRequest, String tagGroupId, String iqcHeaderId);

    /**
     * 选择抽样方案LOV后带出信息
     *
     * @param tenantId     租户ID
     * @param sampleTypeId 抽样方案ID
     * @param iqcHeaderId  头ID
     * @return java.util.List<com.ruike.qms.api.dto.QmsIqcCheckPlatformCreateBringDTO>
     * @author tong.li 2020/5/14 15:54
     */
    QmsIqcCheckPlatformCreateBringDTO2 sampleLovBringDataQuery(Long tenantId, String sampleTypeId, String iqcHeaderId);

    /**
     * IQC检验界面  保存按钮
     *
     * @param tenantId 租户ID
     * @param dto      保存数据
     * @return io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsIqcCheckPlatformIqcSaveDTO>
     * @author tong.li 2020/5/13 15:30
     */
    List<QmsIqcCheckPlatformIqcSaveDTO> iqcSave(Long tenantId, QmsIqcCheckPlatformIqcSaveDTO dto);

    /**
     * IQC检验界面  提交按钮
     *
     * @param tenantId 租户ID
     * @param dto      提交数据
     * @return : java.util.List<com.ruike.qms.api.dto.QmsIqcCheckPlatformIqcSaveDTO>
     * @author tong.li 2020/5/13 19:26
     */
    List<QmsIqcCheckPlatformIqcSaveDTO> iqcSubmit(Long tenantId, QmsIqcCheckPlatformIqcSaveDTO dto);

    /**
     * 点击新建弹窗界面保存
     *
     * @param tenantId 租户ID
     * @param saveData 保存数据
     * @author tong.li 2020/5/15 15:09
     */
    void createPageSave(Long tenantId, QmsIqcCheckPlatformCreatePageSaveDTO2 saveData);

    /**
     * 点击新建弹窗界面删除
     *
     * @param tenantId 租户ID
     * @param dto      删除数据
     * @return Boolean
     * @author tong.li 2020/5/15 15:10
     */
    Boolean createPageDelete(Long tenantId, QmsIqcCheckPlatformIqcDeleteDTO dto);


    /**
     * 附件上传
     *
     * @param tenantId  租户ID
     * @param iqcLineId 行ID
     * @param uuid      文件UUID
     * @return com.ruike.qms.domain.entity.QmsIqcLine
     * @author tong.li 2020/5/20 11:36
     */
    QmsIqcLine uploadAttachment(Long tenantId, String iqcLineId, String uuid);

    /**
     * 界面查询
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsIqcCheckPlatformDTO2>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/17 01:12:10
     */
    Page<QmsIqcCheckPlatformDTO2> listForUi(Long tenantId, QmsIqcCheckPlatformDTO2 dto, PageRequest pageRequest);

    /**
     * 不良项数据查询
     *
     * @param tenantId     租户ID
     * @param inspectionId 来源指令ID
     * @param pageRequest  分页信息
     * @return io.choerodon.core.domain.Page<com.ruike.qms.domain.vo.QmsIqcCheckPlatformVO>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/16 13:49:14
     */
    Page<QmsIqcCheckPlatformVO> ncDataQuery(Long tenantId, String inspectionId, PageRequest pageRequest);

    /**
     * 不良项数据删除
     *
     * @param tenantId 租户ID
     * @param dtoList  删除数据
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcCheckPlatformVO>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/16 14:07:13
     */
    List<QmsIqcCheckPlatformVO> ncDataDelete(Long tenantId, List<QmsIqcCheckPlatformVO> dtoList);

    /**
     * 不良项数据新增或更新
     *
     * @param tenantId 租户ID
     * @param dto      新增数据
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcCheckPlatformVO>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/16 14:27:06
     */
    QmsIqcCheckPlatformDTO3 ncDataUpdate(Long tenantId, QmsIqcCheckPlatformDTO3 dto);

    /**
     * 库存扣减、将送货单行下的NG_QTY扩展字段更新
     *
     * @param tenantId      租户ID
     * @param instructionId 送货单行Id
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/16 04:01:46
     */
    void handleNcQty(Long tenantId, String instructionId, String eventId);

    /**
     * 物料批列表查询
     *
     * @param tenantId    租户
     * @param iqcHeaderId iqc头
     * @param supplierLot
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.qms.domain.vo.QmsIqcMaterialLotVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/22 02:11:26
     */
    Page<QmsIqcMaterialLotVO> materialLotListQuery(Long tenantId, String iqcHeaderId, String supplierLot, PageRequest pageRequest);

    /**
     * 物料批批量更新
     *
     * @param tenantId    租户
     * @param iqcHeaderId iqc头id
     * @param list        物料批列表
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/22 02:42:28
     */
    void materialLotBatchUpdate(Long tenantId, String iqcHeaderId, List<WmsMaterialLotExtendAttrVO> list);

    /**
     * IQC检验单导出
     *
     * @param tenantId 租户ID
     * @param dto      查询信息
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcCheckPlatformExportVO>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/11 11:05:05
     */
    List<QmsIqcCheckPlatformExportVO> export(Long tenantId, QmsIqcCheckPlatformDTO dto);

    /**
     * IQC检验平台-质量文件
     *
     * @param tenantId
     * @param iqcHeaderId
     * @return java.util.List<com.ruike.qms.domain.vo.QmsQualityFileVO>
     * @author sanfeng.zhang@hand-china.com 2021/4/7 16:24
     */
    List<QmsQualityFileVO> qualityFileQuery(Long tenantId, String iqcHeaderId);

    /**
     * IQC检验平台-导入信息
     *
     * @param tenantId
     * @param fileUrl
     * @param pageRequest
     * @return java.util.List<com.ruike.qms.domain.vo.QmsQualityFileVO2>
     * @author sanfeng.zhang@hand-china.com 2021/4/7 17:22
     */
    Page<QmsQualityFileVO2> qualityFileImportQuery(Long tenantId, String fileUrl, PageRequest pageRequest);
}
