package me.frxq.perkroll.integration;

import java.util.UUID;

public interface CurrencyProvider {
    String getCurrencyId();
    double getCurrencyAmount(UUID uuid, String currency);
    void takeCurrency(UUID uuid, String currency, double amount);
}
