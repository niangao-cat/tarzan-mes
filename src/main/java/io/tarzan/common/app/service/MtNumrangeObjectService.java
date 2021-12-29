package io.tarzan.common.app.service;


import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtNumrangeObjectDTO2;
import io.tarzan.common.api.dto.MtNumrangeObjectDTO3;
import io.tarzan.common.domain.vo.MtNumrangeObjectVO;

/**
 * 编码对象属性应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
public interface MtNumrangeObjectService {

    /**
     * 获取编码对象列表
     * @author xiao.tang02@hand-china.com 2019年8月12日下午12:39:22
     * @param tenantId
     * @param condition
     * @param pageRequest
     * @return
     * @return Page<MtNumrangeObject>
     */
    Page<MtNumrangeObjectVO> listNumrangeObjectForUi(Long tenantId, MtNumrangeObjectDTO2 condition, PageRequest pageRequest);

    /**
     * 保存编码对象
     * @author xiao.tang02@hand-china.com 2019年8月12日下午12:39:53
     * @param dto
     * @return
     * @return MtNumrangeObject
     */
    String saveNumrangeObjectForUi(Long tenantId, MtNumrangeObjectDTO3 dto);
    
}
