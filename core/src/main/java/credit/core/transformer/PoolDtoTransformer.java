package credit.core.transformer;

import credit.core.utl.DescriptionUtil;
import credit.db.entity.Pool;
import credit.model.PoolDto;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component
public class PoolDtoTransformer {

    public PoolDtoTransformer() {
    }

    public PoolDto generate(final Pool pool) {
        if (pool == null || pool.getId() == null) {
            return null;
        }

        PoolDto dto = new PoolDto();
        dto.setName(pool.getName());
        dto.setId(pool.getId());
        dto.setPublicId(pool.getPublicId());
        dto.setEditId(pool.getEditId());
        dto.setTotal(pool.getTotal());
        dto.setDescription(pool.getDescription());
        dto.setCreated(pool.getCreated());
        dto.setLevel(pool.getLevel());
        dto.setVerifiedInfo(pool.getVerifiedInfo());
        dto.setHtml(pool.getHtml());

        return dto;
    }

    public Pool generate(final PoolDto dto) {
        if (dto.getName() != null) {
            dto.setName(dto.getName()
                                .replaceAll("\\s+", ""));
            if (StringUtils.isEmpty(dto.getName())) {
                dto.setName(null);
            }
        }

        if (dto.getDescription() != null) {
            dto.setDescription(dto.getDescription()
                                       .trim());
            dto.setDescription(Jsoup.clean(dto.getDescription(), DescriptionUtil.allowedWhiteList()));
        }
        return new Pool(dto.getPublicId(), dto.getEditId(), dto.getName(), dto.getTotal(), dto.getDescription(),
                        dto.getLevel(), dto.getVerifiedInfo(), dto.getHtml());
    }
}
