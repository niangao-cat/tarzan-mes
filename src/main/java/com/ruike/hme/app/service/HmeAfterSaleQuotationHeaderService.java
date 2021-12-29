package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeAfterSaleQuotationHeaderDto;
import com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO13;
import com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO3;

import java.text.ParseException;

/**
 * 售后报价单头表应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-09-26 11:07:31
 */
public interface HmeAfterSaleQuotationHeaderService {

    /**
     * SN扫描
     *
     * @param tenantId 租户ID
     * @param snNum SN编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/29 10:18:17
     * @return com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO3
     */
    HmeAfterSaleQuotationHeaderVO3 scanSn(Long tenantId, String snNum) throws ParseException;

    /**
     * 点击新建按钮时的查询
     *
     * @param tenantId 租户ID
     * @param snNum SN编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/29 10:25:29
     * @return com.ruike.hme.domain.vo.HmeAfterSaleQuotationHeaderVO3
     */
    HmeAfterSaleQuotationHeaderVO3 createQuery(Long tenantId, String snNum) throws ParseException;

    /**
     * 自动查询质保内发货日期
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/29 11:22:33
     * @return java.lang.String
     */
    String sendDateQueryByMaterial(Long tenantId, HmeAfterSaleQuotationHeaderDto dto) throws ParseException;

    /**
     * 保存逻辑
     *
     * @param tenantId 租户ID
     * @param dto 保存数据信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/29 03:24:42
     * @return void
     */
    void save(Long tenantId, HmeAfterSaleQuotationHeaderVO3 dto);

    /**
     * 提交
     *
     * @param tenantId 租户ID
     * @param dto 提交数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/30 11:08:58
     * @return java.lang.String
     */
    HmeAfterSaleQuotationHeaderVO13 submit(Long tenantId, HmeAfterSaleQuotationHeaderVO3 dto);

    /**
     * 修改
     * 
     * @param tenantId 租户ID
     * @param dto 修改数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/30 11:09:40 
     * @return java.lang.String
     */
    HmeAfterSaleQuotationHeaderVO13 edit(Long tenantId, HmeAfterSaleQuotationHeaderVO3 dto);

    /**
     * 取消
     *
     * @param tenantId 租户ID
     * @param dto 取消数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/8 09:56:31
     * @return java.lang.String
     */
    HmeAfterSaleQuotationHeaderVO13 cancel(Long tenantId, HmeAfterSaleQuotationHeaderVO3 dto);
}
