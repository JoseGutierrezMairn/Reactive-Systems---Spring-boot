package co.com.reactive.sample.model;


import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class Account {

    private int type;
    private int number;
    private double balance;

}
