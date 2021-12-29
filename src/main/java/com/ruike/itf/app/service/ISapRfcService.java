package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.RfcParamDTO;

import java.util.List;

/**
 * rfc方式导入
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
public interface ISapRfcService {

    /**
     * 物料同步接口
     *
     * @param dto sap参数
     * @author jiangling.zheng@hand-china.com 2020/7/16 15:04
     * @return
     */
    void materialRfc(RfcParamDTO dto);

    /**
     * 工单同步接口
     *
     * @param dto sap参数
     * @author jiangling.zheng@hand-china.com 2020/7/16 15:04
     * @return
     */
    void workOrderRfc(RfcParamDTO dto);

    /**
     * 物料组同步接口
     *
     * @author jiangling.zheng@hand-china.com 2020/7/16 15:04
     * @return
     */
    void itemGroupRfc();

}
