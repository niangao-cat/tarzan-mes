package tarzan.material.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.material.api.dto.MtPfepDistributionDTO1;
import tarzan.material.api.dto.MtPfepDistributionDTO2;
import tarzan.material.api.dto.MtPfepDistributionDTO3;
import tarzan.material.api.dto.MtPfepDistributionDTO4;
import tarzan.material.api.dto.MtPfepDistributionDTO5;
import tarzan.material.api.dto.MtPfepDistributionDTO6;
import tarzan.material.api.dto.MtPfepDistributionDTO7;

/**
 * 物料配送属性应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepDistributionService {
    /**
     * TODO
     * <p>
     * 物料配送属性查询（前台）
     *
     * @return:
     * @since: 1.0.0
     * @Author: jin.xu@hand-china.com
     * @Date:
     */
    Page<MtPfepDistributionDTO4> materialPfepDistributionQueryForUi(Long tenantId, MtPfepDistributionDTO1 dto,
                                                                    PageRequest pageRequest);

    /**
     * TODO
     * <p>
     * 物料配送属性保存（前台）
     *
     * @return:
     * @since: 1.0.0
     * @Author: jin.xu@hand-china.com
     * @Date:
     */
    String materialPfepDistributionSaveForUi(Long tenantId, MtPfepDistributionDTO2 dto);

    /**
     * TODO
     * <p>
     * 物料配送属性复制（前台）
     *
     * @return:
     * @since: 1.0.0
     * @Author: jin.xu@hand-china.com
     * @Date:
     */
    MtPfepDistributionDTO7 materialPfepDistributionCopyForUi(Long tenantId, MtPfepDistributionDTO3 dto);

    /**
     * TODO
     * <p>
     * 查看明细信息
     *
     * @return:
     * @since: 1.0.0
     * @Author: jin.xu@hand-china.com
     * @Date:
     */
    MtPfepDistributionDTO6 materialPfepDistributionDetailsForUi(Long tenantId, MtPfepDistributionDTO5 dto);
}
