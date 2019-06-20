package credit.core.service;

import credit.db.entity.Stat;
import credit.db.repository.StatRepository;
import org.springframework.stereotype.Service;

@Service
public class StatService {

    private StatRepository statRepository;

    public StatService(final StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    public void insertStat(String ip, String item, final String info) {
        try {
            Stat stat = statRepository.findOneByIpAndItemAndInfo(ip, item, info);
            if (stat == null) {
                statRepository.saveAndFlush(new Stat(ip, item, info));
            }
        } catch (Exception e) {
            // do nothing
        }
    }
}
