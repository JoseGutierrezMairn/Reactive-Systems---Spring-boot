package co.com.reactive.sample.service;

import co.com.reactive.sample.model.Account;
import co.com.reactive.sample.model.IAccountService;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class AccountService implements IAccountService {

    @Override
    public Flux<List<Account>> getAll() {
        List<Account> accountList = new ArrayList<Account>();
        accountList.add( Account.builder().type(1).number(123456).balance(20000).build() );
        accountList.add( Account.builder().type(1).number(56789).balance(50000).build() );
        accountList.add( Account.builder().type(3).number(98765).balance(70000).build() );
        accountList.add( Account.builder().type(1).number(123456).balance(20000).build() );
        //return Flux.just(accountList);
        return Flux.fromIterable(accountList)
                .sort(Comparator.comparing(Account::getNumber))
                .buffer();
    }

    @Override
    public Mono<Account> get(int number) {
        return Mono.just(Account.builder().type(6).number(122333).balance(95000).build());
    }

    @Override
    public Mono<Boolean> create(Account account) {
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> update(Account account) {
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> delete(int number) {
        return Mono.just(false);
    }
}
