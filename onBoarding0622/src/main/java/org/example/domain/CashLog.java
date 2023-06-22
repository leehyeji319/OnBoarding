package org.example.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "cash_log")
public class CashLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cash_log_id", nullable = false, updatable = false)
    private long id;

    @Column(name = "cash_charge_date", nullable = false)
    private LocalDateTime chargeDate;

    @Column(name = "cash_charge_type", nullable = false)
    private String chargeType;

    @Column(name = "cash_charge_amount", nullable = false)
    private int chargeAmount;

    @Column(name = "cash_charge_before")
    private int chargeBeforeCash;

    @Column(name = "cash_charge_after", nullable = false)
    private int chargeAfterCash;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder(toBuilder = true)
    public CashLog(LocalDateTime chargeDate, String chargeType, int chargeAmount, int chargeBeforeCash, int chargeAfterCash, User user) {
        this.chargeDate = chargeDate;
        this.chargeType = chargeType;
        this.chargeAmount = chargeAmount;
        this.chargeBeforeCash = chargeBeforeCash;
        this.chargeAfterCash = chargeAfterCash;
        this.user = user;
    }
}
