package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.controller.v1.HmeProductionPrintDTO;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobMaterial;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.material.domain.entity.MtMaterial;

import java.util.List;

/**
 * description 扩展表查询
 *
 * @author wengang.qiang@hand-china.com 2021/09/27 17:19
 */
public interface HmeProductionPrintMapper {

    /**
     * 扩展表 ip查询
     *
     * @param tenantId   租户id
     * @param workcellId 工位id
     * @return
     */
    List<HmeProductionPrintAttr> queryAttrByWorkcellId(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId);

    /**
     * 扫描信息查询
     *
     * @param tenantId   租户id
     * @param dto 传入参数
     * @return
     */
    List<HmeProductionPrintDTO> queryScanInfo(@Param("tenantId") Long tenantId,
                                              @Param("dto") HmeProductionPrintDTO dto);

    /**
     * 根据eo_id在表mt_eo_component_actual取material_id+assemble_qty
     * 
     * @param tenantId 租户iD
     * @param eoIdList eoId集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/15 01:53:20 
     * @return tarzan.actual.domain.entity.MtEoComponentActual
     */
    List<HmeProductionPrintVO4> eoComponentActualQueryByEo(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList);

    /**
     * 根据type查询表hme_nameplate_print_rel_line数据
     *
     * @param tenantId 租户ID
     * @param type 类型
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/15 02:07:27
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO4>
     */
    List<HmeProductionPrintVO4> nameplatePrintRelLineQueryByType(@Param("tenantId") Long tenantId, @Param("type") String type);

    /**
     * 根据质量文件头表ID查询质量文件行表结果
     *
     * @param tenantId 租户ID
     * @param docIdList 质量文件头表ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/15 03:46:00
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO5>
     */
    List<HmeProductionPrintVO5> quantityAnalyzeLineQuery(@Param("tenantId") Long tenantId, @Param("docIdList") List<String> docIdList,
                                                         @Param("tagCodeG") String tagCodeG, @Param("tagCodeD") String tagCodeD);

    /**
     * 根据EOID查询质量文件头表
     *
     * @param tenantId 租户ID
     * @param eoIdList EOID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/19 02:47:52
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO5>
     */
    List<HmeProductionPrintVO5> quantityAnalyzeDocQuery(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList);

    /**
     * 根据物料编码查询物料ID
     *
     * @param tenantId 租户ID
     * @param materialCodeList 物料编码集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/15 04:01:42
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO4>
     */
    List<MtMaterial> materialIdQueryByCode(@Param("tenantId") Long tenantId, @Param("materialCodeList") List<String> materialCodeList);

    /**
     * 根据物料+数量查询铭牌打印内部识别码对应关系头表内部识别码
     *
     * @param tenantId 租户ID
     * @param dtoList 物料+数量
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/15 04:24:01
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO4>
     */
    List<HmeProductionPrintVO4> nameplatePrintRelHeaderQuery(@Param("tenantId") Long tenantId, @Param("dtoList") List<HmeProductionPrintVO4> dtoList);

    /**
     * 根据铭牌打印内部识别码对应关系头ID查询旗下行个数
     *
     * @param tenantId 租户ID
     * @param headIdList 头ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/15 05:05:50
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO4>
     */
    List<HmeProductionPrintVO4> nameplatePrintRelLineCountQuery(@Param("tenantId") Long tenantId, @Param("headIdList") List<String> headIdList);

    /**
     * 根据工序编码查询工位
     *
     * @param tenantId 租户ID
     * @param processCodeList 工序编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/18 11:19:27
     * @return java.util.List<java.lang.String>
     */
    List<String> worlcellIdQueryByProcessCode(@Param("tenantId") Long tenantId, @Param("processCodeList") List<String> processCodeList);

    /**
     * 根据eoId和工位ID查询出站时间最近的jobId
     *
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/18 11:30:18
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     */
    List<HmeEoJobSn> jobIdQueryByEoAndWorkcell(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList,
                                               @Param("workcellIdList") List<String> workcellIdList);

    /**
     * 根据jobId和materialId在表hme_eo_job_material中查询物料批
     *
     * @param tenantId 租户ID
     * @param jobIdList
     * @param materialIdList
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/18 03:26:47
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobMaterial>
     */
    List<HmeEoJobMaterial> eoJobMaterialQuery(@Param("tenantId") Long tenantId, @Param("jobIdList") List<String> jobIdList,
                                              @Param("materialIdList") List<String> materialIdList);

    /**
     * 根据物料批ID在表hme_eo_job_pump_comb中查询数据
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/18 03:52:30
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO7>
     */
    List<HmeProductionPrintVO7> eoJobPumpCombQuery(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据eoId查询hme_eo_job_sn的jobId
     *
     * @param tenantId 租户ID
     * @param eoIdList eo集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/18 05:26:58
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     */
    List<HmeEoJobSn> jobIdQueryByEoId(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList);

    /**
     * 根据tagCode查询tagId
     *
     * @param tenantId 租户ID
     * @param tagCodeList 数据项编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/18 05:33:03
     * @return java.util.List<java.lang.String>
     */
    List<String> getTagIdByTagCode(@Param("tenantId") Long tenantId, @Param("tagCodeList") List<String> tagCodeList);

    /**
     * 根据jobId和tagId查询表hme_eo_job_data_record的result
     *
     * @param tenantId 租户ID
     * @param jobIdList
     * @param tagIdList
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/18 05:36:40
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobDataRecord>
     */
    List<HmeEoJobDataRecord> eoJobDataRecordQuery(@Param("tenantId") Long tenantId, @Param("jobIdList") List<String> jobIdList,
                                                  @Param("tagIdList") List<String> tagIdList);

    /**
     * 根据jobId、is_issued=1查询表hme_eo_job_material的material_id +material_lot_code
     *
     * @param tenantId 租户ID
     * @param jobIdList jobId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/19 09:23:48
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO7>
     */
    List<HmeProductionPrintVO7> eoJobMaterialQueryByJobId(@Param("tenantId") Long tenantId, @Param("jobIdList") List<String> jobIdList);

    /**
     * 根据eo标识查询表hme_eo_job_material的material_id +material_lot_code
     *
     * @param tenantId 租户ID
     * @param identificationList eo标识集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/19 09:40:44
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO7>
     */
    List<HmeProductionPrintVO7> eoJobMaterialQueryByIdentification(@Param("tenantId") Long tenantId, @Param("identificationList") List<String> identificationList);

    /**
     * 根据物料ID、站点ID查询物料站点扩展表attribute5所对应的扩展值
     *
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param materialIdList 物料ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/19 07:51:36
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO7>
     */
    List<HmeProductionPrintVO7> materialSiteAttrQuery(@Param("tenantId") Long tenantId, @Param("siteId") String siteId,
                                                      @Param("materialIdList") List<String> materialIdList);

    /**
     * 根据eoId查询扩展表中attr_name=REWORK_MATERIAL_LOT的扩展值
     * 
     * @param tenantId 租户ID
     * @param eoIdList eoId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/19 08:02:28 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductionPrintVO7>
     */
    List<HmeProductionPrintVO7> eoReworkMaterialLotAttrQuery(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList);

    /**
     * 查询AF_FLAG等于Y的物料批编码
     *
     * @param tenantId 租户ID
     * @param materialLotCodeList 物料批编码集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/3 10:38:06
     * @return java.util.List<java.lang.String>
     */
    List<String> afFlagYMaterialLotQuery(@Param("tenantId") Long tenantId, @Param("materialLotCodeList") List<String> materialLotCodeList);
}
