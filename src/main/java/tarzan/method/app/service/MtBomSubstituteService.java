package tarzan.method.app.service;

import java.util.List;

import tarzan.method.domain.vo.MtBomSubstituteVO9;

/**
 * 装配清单行替代项应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomSubstituteService {
    
    List<MtBomSubstituteVO9> bomSubstituteRecordForUi(Long tenantId, String bomSubstituteGroupId);

    String saveBomSubstituteForUi(Long tenantId, MtBomSubstituteVO9 dto);
}
