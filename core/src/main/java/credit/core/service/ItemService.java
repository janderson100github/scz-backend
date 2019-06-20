package credit.core.service;

import credit.core.exception.CreditRuntimeException;
import credit.core.transformer.ItemDtoTransformer;
import credit.core.utl.TokenGenerator;
import credit.db.entity.Account;
import credit.db.entity.Item;
import credit.db.entity.Pool;
import credit.db.repository.AccountRepository;
import credit.db.repository.ItemRepository;
import credit.db.repository.PoolRepository;
import credit.model.ItemDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ItemService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ItemRepository itemRepository;

    private final PoolRepository poolRepository;

    private final AccountRepository accountRepository;

    private final ItemDtoTransformer itemDtoTransformer;

    public ItemService(final ItemRepository itemRepository,
                       final PoolRepository poolRepository,
                       final AccountRepository accountRepository,
                       final ItemDtoTransformer itemDtoTransformer) {
        this.itemRepository = itemRepository;
        this.poolRepository = poolRepository;
        this.accountRepository = accountRepository;
        this.itemDtoTransformer = itemDtoTransformer;
    }

    public ItemDto getItem(final String publicId) {
        return itemDtoTransformer.generate(itemRepository.findOneByPublicId(publicId));
    }

    public ItemDto getItemByTransactionId(final String transactionId) {
        Item item = itemRepository.findOneByTransactionId(transactionId);
        if (item == null) {
            throw new CreditRuntimeException(HttpStatus.NOT_FOUND);
        }
        return itemDtoTransformer.generate(item);
    }

    public Item addNewItemToAccount(final String poolPrefix, final String accountPublicId, BigDecimal amount) {
        Pool pool = poolRepository.findOneByName(poolPrefix);
        Account account = accountRepository.findOneByPublicId(accountPublicId);
        Item item = generateNewItem(pool, account, amount);
        item = itemRepository.saveAndFlush(item);

        return item;
    }

    public Item generateNewItem(Pool pool, Account account, BigDecimal amount) {
        Item item = new Item();
        setInitialValues(item);
        item.setPool(pool);
        item.setAccount(account);
        item.setAmount(amount);
        item.setLevel(1); // must be 1, not 0
        return item;
    }

    public void deleteItem(final String editAccountId, final String itemId) {
        Account account = accountRepository.findFirstByEditId(editAccountId);
        if (account == null) {
            throw new CreditRuntimeException(HttpStatus.NOT_FOUND);
        }
        Optional<Item> foundItemOptional = account.getItems()
                .stream()
                .filter(i -> i.getPublicId()
                        .equals(itemId))
                .findFirst();
        if (!foundItemOptional.isPresent()) {
            throw new CreditRuntimeException(HttpStatus.NOT_FOUND);
        }

        account.getItems()
                .remove(foundItemOptional.get());
        accountRepository.saveAndFlush(account);
    }

    private void setInitialValues(Item item) {
        item.setPublicId(TokenGenerator.generateToken());
        item.setTransactionId(TokenGenerator.generateToken());
    }
}
