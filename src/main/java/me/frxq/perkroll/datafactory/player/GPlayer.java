package me.frxq.perkroll.datafactory.player;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.perk.ActivePerk;
import me.frxq.perkroll.perk.Perk;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GPlayer {
    private final PerkRoll plugin;

    private UUID uuid;
    private String name;

    private int tickets, tillGuaranteed, ticketsUsed;
    private ActivePerk activePerk;

    public GPlayer(PerkRoll plugin, UUID uuid, String name, int tickets, int tillGuaranteed, int ticketsUsed, String activePerk, int activeLevel) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.name = name;
        this.tickets = tickets;
        this.tillGuaranteed = tillGuaranteed;
        this.ticketsUsed = ticketsUsed;

        this.activePerk = null; //TB REPLACED
    }

    public GPlayer(PerkRoll plugin, UUID uuid, String name) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.name = name;
        this.tickets = 0;
        this.tillGuaranteed = 100;
        this.ticketsUsed = 0;
        this.activePerk = null;
    }

    public UUID getUUID() {
        return uuid;
    }
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean hasActivePerk() {
        return activePerk != null;
    }
    public ActivePerk getActivePerk() {
        return activePerk;
    }
    public void setActivePerk(ActivePerk activePerk) {
        this.activePerk = activePerk;
    }

    public int getTickets() {
        return tickets;
    }
    public int getTicketsUsed() {
        return ticketsUsed;
    }
    public int getTillGuaranteed() {
        return tillGuaranteed;
    }

    public void addTickets(int amount) {
        this.tickets += amount;
    }
    public void setTickets(int tickets) {
        this.tickets = tickets;
    }
}
