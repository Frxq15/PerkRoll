package me.frxq.perkroll.integration.integrations;

import es.edwardbelt.eddungeons.iapi.EdDungeonsAPI;
import es.edwardbelt.eddungeons.iapi.EdDungeonsBoostersAPI;
import es.edwardbelt.eddungeons.iapi.EdDungeonsCurrencyAPI;
import es.edwardbelt.eddungeons.iapi.EdDungeonsSwordAPI;
import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.integration.BoostProvider;
import me.frxq.perkroll.integration.CurrencyProvider;
import me.frxq.perkroll.integration.Integration;
import me.frxq.perkroll.integration.SwordProvider;
import me.frxq.perkroll.perk.PerkType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.UUID;

public class EdDungeonsIntegration extends Integration implements CurrencyProvider, BoostProvider, SwordProvider {
    private boolean isEnabled;

    public EdDungeonsIntegration(PerkRoll plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "EdDungeons";
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void enable() {
        if (Bukkit.getPluginManager().getPlugin("EdDungeons") != null) {
            isEnabled = true;
            plugin.log("Integrations: Enabled " + getName() + " integration");
            return;
        }
        plugin.warn("Integrations: Failed to enable " + getName() + " integration, jar could not be found");
        isEnabled = false;
    }

    @Override
    public void disable() {
        isEnabled = false;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    // CurrencyProvider

    @Override
    public String getCurrencyId() {
        return "eddungeons";
    }

    @Override
    public double getCurrencyAmount(UUID uuid, String currency) {
        return getCurrencyAPI().getCurrency(uuid, currency).doubleValue();
    }

    @Override
    public void takeCurrency(UUID uuid, String currency, double amount) {
        getCurrencyAPI().removeCurrency(uuid, currency, BigDecimal.valueOf(amount));
    }

    // BoostProvider

    @Override
    public void addBooster(Player player, PerkType type, double multiplier) {
        switch (type) {
            case ENCHANT -> addBoosterInternal(player, "all-enchants", "Enchantment Booster", "", multiplier, true);
            case DAMAGE -> addBoosterInternal(player, "damage", "Damage Booster", "damage", multiplier, false);
            case CRITICAL -> addBoosterInternal(player, "critical", "Critical Booster", "critical", multiplier, true);
        }
    }

    private void addBoosterInternal(Player player, String boostId, String name, String target, double value, boolean isEnchant) {
        getBoosterAPI().addBooster(
                player.getUniqueId(),
                boostId,
                name,
                target,
                value,
                0,
                isEnchant,
                true
        );
    }

    @Override
    public void removeBooster(Player player, String boostId) {
        getBoosterAPI().removeBooster(player.getUniqueId(), boostId);
    }

    @Override
    public void removeAllPerkBoosters(Player player) {
        removeBooster(player, "perkroll-damage");
        removeBooster(player, "perkroll-critical");
        removeBooster(player, "all-enchants");
    }

    @Override
    public void updateSword(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "edd updatesword " + player.getName());
    }

    // SwordProvider

    @Override
    public ItemStack getSwordItem(Player player) {
        return getSwordAPI().getSwordItemFromPlayer(player);
    }

    // API accessors

    public EdDungeonsCurrencyAPI getCurrencyAPI() {
        return EdDungeonsAPI.getInstance().getCurrencyAPI();
    }

    public EdDungeonsBoostersAPI getBoosterAPI() {
        return EdDungeonsAPI.getInstance().getBoostersAPI();
    }

    public EdDungeonsSwordAPI getSwordAPI() {
        return EdDungeonsAPI.getInstance().getSwordAPI();
    }
}
