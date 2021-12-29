package com.ruike.itf.infra.mapper;

import com.ruike.itf.api.dto.ItfOperationAnalyseDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工艺分析Mapper
 *
 * @author xin.t@raycuslaser.com 2021-11-02 17:05:08
 */
public interface ItfOperationAnalyseIfaceMapper {

    /**
     * 查询工序不良
     * @param tenantId
     * @param materialLotCodes
     * @param processNames
     * @return
     */
    List<ItfOperationAnalyseDTO.NcDTO> queryNc(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "materialLotCodes") List<String> materialLotCodes,
                                               @Param("processNames") List<String> processNames);

    /**
     * 查询工序作业记录
     * @param tenantId
     * @param begda
     * @param endda
     * @param operationIds
     * @param workcellIds
     * @param materialLotIds
     * @return
     */
    List<String> queryEoJob(@Param(value = "tenantId") Long tenantId,
                            @Param(value = "begda") String begda,
                            @Param(value = "endda") String endda,
                            @Param(value = "operationIds") List<String> operationIds,
                            @Param(value = "workcellIds") List<String> workcellIds,
                            @Param(value = "materialLotIds") List<String> materialLotIds);

    /**
     * 查询投料信息
     * @param tenantId
     * @param jobIds
     * @param materialIds
     * @return
     */
    List<ItfOperationAnalyseDTO.MaterialData> eoMaterialQuery(@Param("tenantId") Long tenantId,
                                                              @Param("jobIds") List<String> jobIds,
                                                              @Param("materialIds") List<String> materialIds);

    /**
     * 查询工序工艺质量
     * @param tenantId
     * @param topSiteId
     * @param jobIds
     * @param tagCodes
     * @return
     */
    List<ItfOperationAnalyseDTO.EoJobData> eoJobDataQuery(@Param("tenantId") Long tenantId,
                                                          @Param("topSiteId") String topSiteId,
                                                          @Param("jobIds") List<String> jobIds,
                                                          @Param("tagCodes") List<String> tagCodes);


    /**
     * 查询工序作业不良记录
     * @param tenantId
     * @param begda
     * @param endda
     * @param operationIds
     * @param workcellIds
     * @param materialLotIds
     * @return
     */
    List<ItfOperationAnalyseDTO.EoJobNcRecord> queryEoJobNcRecord(@Param(value = "tenantId") Long tenantId,
                                                                  @Param(value = "begda") String begda,
                                                                  @Param(value = "endda") String endda,
                                                                  @Param(value = "operationIds") List<String> operationIds,
                                                                  @Param(value = "workcellIds") List<String> workcellIds,
                                                                  @Param(value = "materialLotIds") List<String> materialLotIds);

    /**
     * 查询条码当前工序
     * @param tenantId
     * @param materialLotCodes
     * @return
     */
    List<ItfOperationAnalyseDTO.Process> queryCurrentProcess(@Param(value = "tenantId") Long tenantId,
                                                             @Param(value = "materialLotCodes") List<String> materialLotCodes);

    /**
     * 根据工艺步骤查工艺
     * @param tenantId
     * @param processNames
     * @return
     */
    List<ItfOperationAnalyseDTO.Process> queryOperationByWorkcellName(@Param(value = "tenantId") Long tenantId,
                                                                      @Param(value = "processNames") List<String> processNames);

    /**
     * 根据工序查工位
     * @param tenantId
     * @param processNames
     * @return
     */
    List<ItfOperationAnalyseDTO.Process> queryWorkcellByProcessName(@Param(value = "tenantId") Long tenantId,
                                                                    @Param(value = "processNames") List<String> processNames);

    /**
     * 查询当前条码产品信息
     * @param tenantId
     * @param materialLotCodes
     * @return
     */
    List<ItfOperationAnalyseDTO.NcList> queryMaterialBySNs(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "materialLotCodes") List<String> materialLotCodes);
}
