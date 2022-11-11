package co.com.reactive.sample;


import co.com.reactive.sample.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SampleApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SampleApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
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
