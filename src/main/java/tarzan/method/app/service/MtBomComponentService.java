package tarzan.method.app.service;

import tarzan.method.domain.vo.MtBomComponentVO4;

/**
 * 装配清单行应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomComponentService {

    MtBomComponentVO4 bomComponentRecordForUi(Long tenantId, String bomComponentId);
    
    String saveBomComponentForUi(Long tenantId, MtBomComponentVO4 dto);
}
