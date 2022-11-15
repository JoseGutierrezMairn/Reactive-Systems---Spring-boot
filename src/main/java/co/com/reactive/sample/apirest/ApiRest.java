package co.com.reactive.sample.apirest;


import co.com.reactive.sample.model.Account;
import co.com.reactive.sample.model.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class ApiRest {

    @Autowired
    private IAccountService iAccountService;


    @GetMapping(path = "/getAll")
    Flux<List<Account>> getAll(){
        return iAccountService.getAll();
    }

    @GetMapping(path = "/get")
    Mono<Account> get( @RequestParam int number){
        return iAccountService.get(number);
    }

    @PostMapping(path = "/create")
    Mono<Boolean> create(@RequestBody Account account){
        return iAccountService.create(account);
    }

    @PutMapping(path = "update")
    Mono<Boolean> update(@RequestBody Account account){
        return iAccountService.update(account);
    }

    @DeleteMapping(path = "/delete")
    Mono<Boolean> delete(@RequestParam int number){
        return iAccountService.delete(number);
    }

}
