package io.tarzan.common.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtNumrangeDTO;
import io.tarzan.common.domain.vo.MtNumrangeVO6;

/**
 * 号码段定义表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
public interface MtNumrangeService {

    /**
     * 号码段保存
     *
     * @param tenantId 租户id
     * @param dto      号码段维护对象
     * @return
     * @author YY 2019-08-16 16:40
     */
    String numrangeSaveForUi(Long tenantId, MtNumrangeDTO dto);

    /**
     * numrangeListUi-获取号码段信息
     *
     * @param tenantId       租户id
     * @param objectid       编码对象id
     * @param numDescription 号码段描述
     * @param pageRequest    分页参数
     * @return
     */
    Page<MtNumrangeVO6> numrangeListUi(Long tenantId, String objectid, String numDescription, PageRequest pageRequest);

    /**
     * queryNumrangeForUi-获取号码段信息
     *
     * @param tenantId       租户id
     * @param numrangeId     编码号码段id
     * @return
     */
    MtNumrangeDTO queryNumrangeForUi(Long tenantId, String numrangeId);

}
