package tarzan.method.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.method.api.dto.MtNcCodeDTO2;
import tarzan.method.api.dto.MtNcCodeDTO3;
import tarzan.method.api.dto.MtNcCodeDTO5;

/**
 * 不良代码数据应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
public interface MtNcCodeService {

    /**
     * UI查询不良代码
     * 
     * @author benjamin
     * @date 2019/9/10 1:48 PM
     * @param tenantId 租户Id
     * @param dto MtNcCodeDTO2
     * @param pageRequest PageRequest
     * @return page
     */
    Page<MtNcCodeDTO2> queryNcCodeListForUi(Long tenantId, MtNcCodeDTO2 dto, PageRequest pageRequest);

    /**
     * UI根据不良代码Id查询不良代码详细信息
     * 
     * @author benjamin
     * @date 2019/9/10 1:49 PM
     * @param tenantId 租户Id
     * @param ncCodeId 不良代码Id
     * @return MtNcCodeDTO3
     */
    MtNcCodeDTO3 queryNcCodeDetailForUi(Long tenantId, String ncCodeId);

    /**
     * UI保存不良代码
     * 
     * @author benjamin
     * @date 2019/9/10 2:29 PM
     * @param tenantId 租户Id
     * @param dto MtNcCodeDTO5
     * @return String
     */
    String saveNcCodeForUi(Long tenantId, MtNcCodeDTO5 dto);
}
