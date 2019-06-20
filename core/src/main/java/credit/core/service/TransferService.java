package credit.core.service;

import credit.core.exception.CreditRuntimeException;
import credit.core.utl.TokenGenerator;
import credit.db.entity.Account;
import credit.db.entity.Item;
import credit.db.repository.AccountRepository;
import credit.db.repository.ItemRepository;
import credit.model.TransferDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferService {

    private AccountRepository accountRepository;

    private AccountService accountService;

    private ItemRepository itemRepository;

    private ItemService itemService;

    public TransferService(final AccountRepository accountRepository,
                           final AccountService accountService,
                           final ItemRepository itemRepository,
                           final ItemService itemService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.itemRepository = itemRepository;
        this.itemService = itemService;
    }

    public TransferDto transfer(String fromAccountEditId,
                                String fromItemPublicId,
                                String toAccountPublicId,
                                BigDecimal amount,
                                String message) {
        Account fromAccount = accountRepository.findOneByEditId(fromAccountEditId);
        if (fromAccount == null) {
            throw new CreditRuntimeException(HttpStatus.FORBIDDEN, "no account " + fromAccountEditId);
        }
        Account toAccount = accountRepository.findOneByPublicId(TokenGenerator.stringToId(toAccountPublicId));
        if (toAccount == null) {
            throw new CreditRuntimeException(HttpStatus.FORBIDDEN, "no account " + toAccountPublicId);
        }
        Item fromItem = fromAccount.getItems()
                .stream()
                .filter(i -> i.getPublicId()
                        .equals(fromItemPublicId))
                .findFirst()
                .orElse(null);
        if (fromItem == null) {
            throw new CreditRuntimeException(HttpStatus.FORBIDDEN, "no credit");
        }
        if (fromItem.getAmount()
                    .compareTo(amount) < 0) {
            throw new CreditRuntimeException(HttpStatus.FORBIDDEN, "insufficient credit");
        }

        Item toItem = itemService.generateNewItem(fromItem.getPool(), toAccount, amount);
        toItem.setLevel(fromItem.getLevel() + 1);
        toItem.setMessage(message);

        fromItem.setAmount(fromItem.getAmount()
                                   .subtract(amount));
        if (BigDecimal.ZERO.compareTo(fromItem.getAmount()) == 1) {
            itemRepository.delete(fromItem);
        } else {
            itemRepository.save(fromItem);
        }

        itemRepository.save(toItem);
        itemRepository.flush();

        toAccount.getItems()
                .add(toItem);

        accountService.calculateAndSaveAccountScore(fromAccount);
        accountService.calculateAndSaveAccountScore(toAccount);

        TransferDto transferDto = new TransferDto(fromAccountEditId, fromItemPublicId, toAccountPublicId, amount,
                                                  message);
        transferDto.setTransactionId(toItem.getTransactionId());
        transferDto.setToItemPublicId(toItem.getPublicId());

        return transferDto;
    }
}
