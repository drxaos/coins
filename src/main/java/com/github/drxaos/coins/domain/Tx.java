package com.github.drxaos.coins.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Tx {

    Date date;
    Category category;

    Account outcomeAccount;
    BigDecimal outcomeValue;

    Account incomeAccount;
    BigDecimal incomeValue;

    String comment;
}
