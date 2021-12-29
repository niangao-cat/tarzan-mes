package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO2;
import com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO3;
import com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO4;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeAfterSaleQuotationHeader;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 售后报价单头表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-09-26 11:07:31
 */
public interface HmeAfterSaleQuotationHeaderRepository extends BaseRepository<HmeAfterSaleQuotationHeader> {

    /**
     * 根据SN编码查询接收拆箱登记表数据、售后返品拆机表数据、售后报价单头表数据
     *
     * @param tenantId 租户ID
     * @param snNum SN编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/26 05:01:06
     * @return com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO2
     */
    HmeAfterSaleQuotationHeaderVO2 serviceReceiveAndRecordInfoQuery(Long tenantId, String snNum);

    /**
     * 查询界面头部数据
     *
     * @param tenantId 租户ID
     * @param snNum SN编码
     * @param hmeAfterSaleQuotationHeaderVO2 接收拆箱登记表数据、售后返品拆机表数据、售后报价单头表数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/26 08:09:15
     * @return com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO3
     */
    HmeAfterSaleQuotationHeaderVO3 queryHeadData(Long tenantId, String snNum, HmeAfterSaleQuotationHeaderVO2 hmeAfterSaleQuotationHeaderVO2) throws ParseException;

    /**
     * 查询行数据
     *
     * @param tenantId 租户ID
     * @param hmeAfterSaleQuotationHeaderVO2 头部信息
     * @param demandType 行需求类型
     * @param sendTo 送达方
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/28 04:30:38
     * @return java.util.List<com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO4>
     */
    List<HmeAfterSaleQuotationHeaderVO4> queryLineData(Long tenantId, HmeAfterSaleQuotationHeaderVO2 hmeAfterSaleQuotationHeaderVO2, String demandType,
                                                       String sendTo) throws ParseException;

    /**
     * 点击新建时，按照场景二逻辑，根据SN编码查询接收拆箱登记表数据、售后返品拆机表数据
     *
     * @param tenantId 租户ID
     * @param snNum SN编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/29 10:27:44
     * @return com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO2
     */
    HmeAfterSaleQuotationHeaderVO2 serviceReceiveAndRecordInfoCreateQuery(Long tenantId, String snNum);

    /**
     * 点击新建时，查询工时费行
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/29 10:42:23
     * @return java.util.List<com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO4>
     */
    List<HmeAfterSaleQuotationHeaderVO4> hourfeeLineCreateQuery(Long tenantId);

    /**
     * 报价单状态为新建时的保存逻辑
     *
     * @param tenantId 租户ID
     * @param dto 新建数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/29 11:16:55
     * @return void
     */
    void newStatusSave(Long tenantId, HmeAfterSaleQuotationHeaderVO3 dto);

    /**
     * 报价单状态为暂存时的保存逻辑
     *
     * @param tenantId 租户ID
     * @param dto 暂存数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/29 02:34:33
     * @return void
     */
    void storageStatusSave(Long tenantId, HmeAfterSaleQuotationHeaderVO3 dto);

    /**
     * 计算两个日期之间相差的天数
     *
     * @param minDate 小日期
     * @param maxDate 大日期
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/29 11:48:50
     * @return java.math.BigDecimal
     */
    BigDecimal daysBetween(Date minDate, Date maxDate) throws ParseException;
}
