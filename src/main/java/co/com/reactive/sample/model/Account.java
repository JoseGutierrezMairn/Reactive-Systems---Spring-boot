package co.com.reactive.sample.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {

    private int type;
    private int number;
    private double balance;

}
