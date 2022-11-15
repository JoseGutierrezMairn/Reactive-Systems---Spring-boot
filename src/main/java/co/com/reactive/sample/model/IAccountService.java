package co.com.reactive.sample.model;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IAccountService {

    Flux<List<Account>> getAll();
    Mono<Account> get(int number);
    Mono<Boolean> create(Account account);
    Mono<Boolean> update(Account account);
    Mono<Boolean> delete(int number);


}
