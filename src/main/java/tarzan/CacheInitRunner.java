package tarzan;

import com.ruike.hme.api.dto.TenantDTO;
import com.ruike.hme.infra.feign.HmeHzeroIamFeignClient;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import io.choerodon.core.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 缓存初始化
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/14 16:41
 */
@Order(2)
@Component
public class CacheInitRunner implements ApplicationRunner {

    private final WmsTransactionTypeRepository wmsTransactionTypeRepository;
    private final HmeHzeroIamFeignClient hzeroIamFeignClient;

    @Autowired
    public CacheInitRunner(WmsTransactionTypeRepository wmsTransactionTypeRepository, HmeHzeroIamFeignClient hzeroIamFeignClient) {
        this.wmsTransactionTypeRepository = wmsTransactionTypeRepository;
        this.hzeroIamFeignClient = hzeroIamFeignClient;
    }

    private void initTransactionTypeCache() {
        Page<TenantDTO> tenantList = hzeroIamFeignClient.selectTenantPage(0L);
        tenantList.stream().map(TenantDTO::getTenantId).forEach(wmsTransactionTypeRepository::initCache);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initTransactionTypeCache();
    }
}
