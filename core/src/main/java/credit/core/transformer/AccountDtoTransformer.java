package credit.core.transformer;

import credit.core.utl.DescriptionUtil;
import credit.db.entity.Account;
import credit.model.AccountDto;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class AccountDtoTransformer {

    private ItemDtoTransformer itemDtoTransformer;

    public AccountDtoTransformer(final ItemDtoTransformer itemDtoTransformer) {
        this.itemDtoTransformer = itemDtoTransformer;
    }

    public AccountDto generate(final Account account) {
        if (account == null || account.getId() == null) {
            return null;
        }

        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setPublicId(account.getPublicId());
        dto.setEditId(account.getEditId());
        dto.setCreated(account.getCreated());
        dto.setDescription(account.getDescription());
        dto.setVisibility(account.getVisibility());
        dto.setScore(account.getScore());
        dto.setAlias(account.getAlias());
        dto.setHtml(account.getHtml());
        dto.setVerifiedInfo(account.getVerifiedInfo());

        dto.setItems(account.getItems()
                             .stream()
                             .map(itemDtoTransformer::generate)
                             .sorted()
                             .collect(Collectors.toList()));

        return dto;
    }

    public AccountDto generateShallow(final Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setPublicId(account.getPublicId());
        dto.setCreated(account.getCreated());
        dto.setDescription(account.getDescription());
        dto.setVisibility(account.getVisibility());
        dto.setScore(account.getScore());
        dto.setAlias(account.getAlias());
        return dto;
    }

    public Account generate(final AccountDto dto) {
        if (dto.getAlias() != null) {
            dto.setAlias(dto.getAlias()
                                 .replaceAll("\\s+", ""));
            if (StringUtils.isEmpty(dto.getAlias())) {
                dto.setAlias(null);
            }
        }
        if (StringUtils.isEmpty(dto.getDescription())) {
            dto.setDescription(null);
        }
        if (dto.getDescription() != null) {
            dto.setDescription(Jsoup.clean(dto.getDescription()
                                                   .trim(), DescriptionUtil.allowedWhiteList()));
        }
        return new Account(dto.getPublicId(), dto.getEditId(), dto.getDescription(), dto.getAlias(), dto.getHtml(),
                           Arrays.asList());
    }
}
