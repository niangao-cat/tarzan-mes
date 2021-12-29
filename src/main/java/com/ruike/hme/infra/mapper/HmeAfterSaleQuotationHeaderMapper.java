package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeAfterSaleQuotationHeader;
import com.ruike.hme.domain.entity.HmeServiceReceive;
import com.ruike.hme.domain.entity.HmeServiceSplitRecord;
import com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO;
import com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO4;
import com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO5;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.domain.entity.MtModWorkcell;

import java.util.Date;
import java.util.List;

/**
 * 售后报价单头表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-09-26 11:07:31
 */
public interface HmeAfterSaleQuotationHeaderMapper extends BaseMapper<HmeAfterSaleQuotationHeader> {

    /**
     * 根据sn编码、状态查询最后更新时间最近的售后报价单头表数据
     *
     * @param tenantId 租户ID
     * @param snNum sn编码
     * @param statusList 状态集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/26 01:57:22
     * @return com.ruike.hme.domain.entity.HmeAfterSaleQuotationHeader
     */
    List<HmeAfterSaleQuotationHeader> getQuotationHeaderBySnNum(@Param("tenantId") Long tenantId, @Param("snNum") String snNum,
                                                                @Param(value = "statusList") List<String> statusList);

    /**
     * 根据物料批ID、状态查询最后更新时间最近的售后报价单头表数据
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @param statusList 状态
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/28 05:57:13
     * @return java.util.List<com.ruike.hme.domain.entity.HmeAfterSaleQuotationHeader>
     */
    List<HmeAfterSaleQuotationHeader> getQuotationHeaderByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId,
                                                                @Param(value = "statusList") List<String> statusList);

    /**
     * 根据物料批ID、站点查询符合条件的接收拆箱登记表主键以及售后返品拆机表主键
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @param siteId 站点ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/26 04:06:01
     * @return java.util.List<com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO>
     */
    List<HmeAfterSaleQuotationHeaderVO> getServiceReceiveByMaterialLot(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId,
                                                                         @Param("siteId") String siteId);

    /**
     * 根据SN编码查询符合条件的接收拆箱登记表主键以及售后返品拆机表主键
     *
     * @param tenantId 租户ID
     * @param snNum SN编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/26 04:14:42
     * @return java.util.List<com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO>
     */
    List<HmeAfterSaleQuotationHeaderVO> getServiceReceiveBySnNum(@Param("tenantId") Long tenantId, @Param("snNum") String snNum);

    /**
     * 根据物流信息表主键查询出创建时间最晚的那一条物流信息表主键
     *
     * @param tenantId 租户ID
     * @param logisticsInfoIdList 物流信息表主键集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/26 04:33:30
     * @return java.lang.String
     */
    String getCreationDateMaxLogisticsInfoId(@Param("tenantId") Long tenantId, @Param("logisticsInfoIdList") List<String> logisticsInfoIdList);

    /**
     * 根据ServiceReceiveId、top_split_record_id = split_record_id 查询唯一的split_record_id
     *
     * @param tenantId 租户ID
     * @param serviceReceiveId ServiceReceiveId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/26 04:47:14
     * @return com.ruike.hme.domain.entity.HmeServiceSplitRecord
     */
    HmeServiceSplitRecord getServiceSplitRecordByServiceReceiveId(@Param("tenantId") Long tenantId, @Param("serviceReceiveId") String serviceReceiveId);

    /**
     * 根据物料ID、站点ID查询物料编码、描述
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param siteId 站点ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/26 05:16:27
     * @return tarzan.material.domain.entity.MtMaterial
     */
    MtMaterial getMaterialInfoByMaterialSite(@Param("tenantId") Long tenantId, @Param("materialId") String materialId,
                                             @Param("siteId") String siteId);

    /**
     * 根据serviceReceiveId查询送达方
     *
     * @param tenantId 租户ID
     * @param serviceReceiveId serviceReceiveId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/26 05:32:24
     * @return tarzan.modeling.domain.entity.MtCustomer
     */
    MtCustomer sendToQueryByServiceReceiveId(@Param("tenantId") Long tenantId, @Param("serviceReceiveId") String serviceReceiveId);

    /**
     * 根据物料批编码查询当前工位
     *
     * @param tenantId 租户ID
     * @param materialLotCode 物料批编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/26 05:45:55
     * @return tarzan.modeling.domain.entity.MtModWorkcell
     */
    MtModWorkcell currentWorkcellQuery(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);

    /**
     * 根据SN编码查询上一次发货时间
     *
     * @param tenantId 租户ID
     * @param snNum SN编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/26 07:09:59
     * @return java.util.Date
     */
    Date lastSendDateQueryBySnNum(@Param("tenantId") Long tenantId, @Param("snNum") String snNum);

    /**
     * 根据头ID、行类型查询行数据
     *
     * @param tenantId 租户ID
     * @param quotationHeaderId 头ID
     * @param demandType 行类型
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/26 08:25:27
     * @return java.util.List<com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO4>
     */
    List<HmeAfterSaleQuotationHeaderVO4> lineDataQuery(@Param("tenantId") Long tenantId, @Param("quotationHeaderId") String quotationHeaderId,
                                                       @Param("demandType") String demandType);

    /**
     * 根据SN、送达方、物料查询满足条件的售后报价单头最后更新时间，进而求出质保内发货日期
     *
     * @param tenantId 租户ID
     * @param materialLotId SN
     * @param sendTo 送达方
     * @param quotationHeaderId 需要排除的售后报价单头
     * @param materialIdList 物料ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/28 05:33:49
     * @return java.util.List<com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO5>
     */
    List<HmeAfterSaleQuotationHeaderVO5> quotationHeaderUpdateDateQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId, @Param("snNum") String snNum,
                                                                        @Param("sendTo") String sendTo, @Param("quotationHeaderId") String quotationHeaderId,
                                                                        @Param("materialIdList") List<String> materialIdList);

    /**
     * 根据sernr、lastUpdateDate查询接口表中满足条件的日期
     *
     * @param tenantId 租户ID
     * @param sernr 扫描序列号
     * @param lastUpdateDate 最后更新时间
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/29 09:40:50
     * @return java.util.List<java.util.Date>
     */
    List<Date> snSapIfaceUpdateDateQuery(@Param("tenantId") Long tenantId, @Param("sernr") String sernr, @Param("lastUpdateDate") Date lastUpdateDate);

    /**
     * 根据主键批量删除售后报价单行表数据
     *
     * @param tenantId 租户ID
     * @param quotationLineIdList 主键ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/29 02:45:52
     * @return void
     */
    void batchDeleteLineData(@Param("tenantId") Long tenantId, @Param("quotationLineIdList") List<String> quotationLineIdList);

    /**
     * 批量更新行数据
     *
     * @param tenantId 租户ID
     * @param updateLineList 更新数据集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/29 03:09:54
     * @return void
     */
    void batchUpdateLineData(@Param("tenantId") Long tenantId, @Param("updateLineList") List<HmeAfterSaleQuotationHeaderVO4> updateLineList);

    /**
     * 根据物料ID、站点ID查询设备型号
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param siteId 站点ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/30 09:41:13
     * @return java.lang.String
     */
    String getEquipmentModelByMaterialSite(@Param("tenantId") Long tenantId, @Param("materialId") String materialId, @Param("siteId") String siteId);

    /**
     * 根据sn_num或material_lot_id在表hme_service_receive查询数据
     *
     * @param tenantId 租户ID
     * @param snNum snNum
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/21 11:00:28
     * @return java.util.List<com.ruike.hme.domain.entity.HmeServiceReceive>
     */
    List<HmeServiceReceive> serviceReciveQueryBySnNumOrMaterialLot(@Param("tenantId") Long tenantId, @Param("snNum") String snNum,
                                                                   @Param("materialLotId") String materialLotId);

    /**
     * 根据serviceReceiveId查询最后更新时间最近的售后报价单头表数据
     *
     * @param tenantId 租户ID
     * @param serviceReceiveId serviceReceiveId
     * @param statusList 状态
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/21 11:13:36
     * @return java.util.List<com.ruike.hme.domain.entity.HmeAfterSaleQuotationHeader>
     */
    List<HmeAfterSaleQuotationHeader> getQuotationHeaderByServiceReceive(@Param("tenantId") Long tenantId, @Param("serviceReceiveId") String serviceReceiveId,
                                              @Param(value = "statusList") List<String> statusList);
}
