package tarzan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;

@Order(1)
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public void run(String... args) throws Exception {
        this.mtGenStatusRepository.initDataToRedis();
        this.mtGenTypeRepository.initDataToRedis();
        this.mtErrorMessageRepository.initDataToRedis();
    }

}
