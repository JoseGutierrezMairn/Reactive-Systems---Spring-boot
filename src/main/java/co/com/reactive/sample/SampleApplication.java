package co.com.reactive.sample;


import co.com.reactive.sample.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class SampleApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		//simpleFlow();
		//fluxFlow();


	}

	void fluxFlow(){
		List<Account> accountList = new ArrayList<Account>();
		accountList.add( Account.builder().type(1).number(123456).balance(20000).build() );
		accountList.add( Account.builder().type(1).number(56789).balance(50000).build() );
		accountList.add( Account.builder().type(3).number(98765).balance(70000).build() );
		accountList.add( Account.builder().type(1).number(123456).balance(20000).build() );
		getAccounts().subscribe( accounts -> logger.info( "Flux: ".concat(accounts.toString()) ) );
		Flux.fromIterable(accountList).subscribe( account -> logger.info( "Flux: ".concat(account.toString()) ) );
		Flux.fromIterable(accountList)
				.collectList()
				.subscribe( account -> logger.info( "Flux: ".concat(account.toString()) ) );

		Flux.range(0,3)
				.flatMap( i->{
					return Flux.just(Account.builder().type(1).number(345678).balance(600000).build());
				} ).subscribe( a -> logger.info("Flux Range: ".concat(a.toString())));

		Flux.just(Account.builder().type(1).number(345678).balance(600000).build())
				.repeat(5)
				.subscribe( accounts -> logger.info( "Flux repeat: ".concat(accounts.toString()) ) );

		Flux.fromIterable(accountList)
				.groupBy( account -> account.getType() )
				.flatMap( typeFlux -> typeFlux.collectList() )
				.subscribe(accounts -> logger.info( "Flux group by: ".concat(accounts.toString()) ) );

		Flux.fromIterable(accountList)
				.filter( a -> a.getBalance() > 50000)
				.subscribe(accounts -> logger.info( "Flux filter: ".concat(accounts.toString()) ) );

		Flux.fromIterable(accountList)
				.distinct()
				.subscribe(accounts -> logger.info( "Flux distinct: ".concat(accounts.toString()) ) );

		Flux.fromIterable(accountList)
				.skip(2)
				.subscribe(accounts -> logger.info( "Flux skip: ".concat(accounts.toString()) ) );

		Flux.fromIterable(accountList)
				.concatWith(Flux.error(new RuntimeException("MY ERROR")))
				.onErrorReturn( Account.builder().build() )
				.subscribe(accounts -> logger.info( "Flux onErrorReturn: ".concat(accounts.toString()) ) );


		Flux.fromIterable(accountList)
				.concatWith(Flux.error(new RuntimeException("MY ERROR")))
				.onErrorResume( error ->{
					logger.info("OnErrorResume: ".concat(error.getMessage()));
					return Mono.just(Account.builder().build());
				} )
				.subscribe(accounts -> logger.info( "Flux onErrorReturnResume: ".concat(accounts.toString()) ) );

		Flux.fromIterable((accountList))
				.collect(Collectors.averagingDouble(Account::getBalance))
				.subscribe(calc -> logger.info("Flux averigainDouble: ".concat(calc.toString())));

		Flux.fromIterable((accountList))
				.collect(Collectors.minBy(Comparator.comparing(Account::getBalance)))
				.subscribe(calc -> logger.info("Flux minimo: ".concat(calc.toString())));

		Flux.fromIterable((accountList))
				.collect(Collectors.maxBy(Comparator.comparing(Account::getBalance)))
				.subscribe(calc -> logger.info("Flux maximo: ".concat(calc.toString())));

		Flux.fromIterable((accountList))
				.collect(Collectors.summingDouble((Account::getBalance)))
				.subscribe(calc -> logger.info("Flux suma: ".concat(calc.toString())));
		Flux.fromIterable((accountList))
				.collect(Collectors.summarizingDouble((Account::getBalance)))
				.subscribe(calc -> logger.info("Flux summarizinDouble: ".concat(calc.toString())));

		Flux.fromIterable((accountList))
				.count()
				.subscribe(calc -> logger.info("Flux Conteo: ".concat(calc.toString())));
	}

	Flux<List> getAccounts(){
		List<Account> accountList = new ArrayList<Account>();
		accountList.add( Account.builder().type(1).number(123456).balance(20000).build() );
		accountList.add( Account.builder().type(1).number(56789).balance(50000).build() );
		accountList.add( Account.builder().type(3).number(98765).balance(70000).build() );
		accountList.add( Account.builder().type(4).number(54321).balance(100000).build() );
		return Flux.just(accountList);
	}

	void simpleFlow(){
		// Create flow string and subscription
		Mono.just("DATA").subscribe(s -> logger.info("Mono: "+ s.toString()));

		var s1 = Mono.just("DATA");

		// Create model and create flow
		var account = Account.builder().type(1).number(123456).balance(50000).build();
		Mono.just(account)
				.doOnNext(account1 -> logger.info("Mono: "+ account1.toString()))
				.subscribe();

		var s2 = Mono.just(account)
				.doOnNext(account1 -> logger.info("Mono s2: "+ account1.toString()));

		s1.map(s ->{
			s = s + " JOSE";
			return s;
		}).subscribe(response -> logger.info("Mono map: "+ response.toString()));

		s2.flatMap(a -> {
					a.setBalance(a.getBalance() + 50000);
					return Mono.just(a);
				}).doOnNext(r -> logger.info("Mono s2 map do onNext: " + r.toString()))
				.subscribe();


		Mono.empty()
				.defaultIfEmpty(Account.builder().type(2).number(44444).balance(20000).build())
				.flatMap(a -> {
					return Mono.just(a);
				} ).subscribe(r -> logger.info("Mono empty: " + r.toString()));

		getAccount(33)
				.defaultIfEmpty(Account.builder().type(3).number(77777).balance(0).build())
				.flatMap(a -> {
					return Mono.just(a);
				} ).subscribe(r -> logger.info("Mono empty calling method: " + r.toString()));
	}



	Mono<Account> getAccount(int number){
		// return Mono.empty();
		return Mono.just(Account.builder().type(4).number(99).balance(60).build());
	}
}
