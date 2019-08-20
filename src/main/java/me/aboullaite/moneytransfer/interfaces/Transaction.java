package me.aboullaite.moneytransfer.interfaces;


import me.aboullaite.moneytransfer.enums.TxStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Transaction extends Base{

    Account getDebit();

    Account getCredit();

    BigDecimal getAmount();

    TxStatus getStatus();

    LocalDateTime getTxDate();

    boolean run();
}
