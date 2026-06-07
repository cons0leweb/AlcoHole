package org.cons0leweb.alcoHole;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Main extends JavaPlugin implements TabCompleter {

    private Map<String, Drink> drinks = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadDrinks();
        Objects.requireNonNull(getCommand("drink")).setTabCompleter(this);
        getLogger().info(" by cons0leweb\nAlcoHole has been Enabled\n\n" +
                "CFG LOADED");
    }

    @Override
    public void onDisable() {
        getLogger().info("by cons0leweb\nAlcoHole has been Disabled\n\n");
    }

    private void loadDrinks() {
        FileConfiguration config = getConfig();
        for (String key : Objects.requireNonNull(config.getConfigurationSection("drinks")).getKeys(false)) {
            String name = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("drinks." + key + ".name")));
            String description = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("drinks." + key + ".description")));
            String colorName = config.getString("drinks." + key + ".color");
            String effectName = config.getString("drinks." + key + ".effect");
            int duration = config.getInt("drinks." + key + ".duration");
            int amplifier = config.getInt("drinks." + key + ".amplifier");
            boolean isAlcoholic = config.getBoolean("drinks." + key + ".isAlcoholic");

            assert colorName != null;
            Color color = Color.fromRGB(getColorCode(colorName));
            assert effectName != null;
            PotionEffectType effectType = PotionEffectType.getByName(effectName);
            if (effectType != null) {
                drinks.put(key, new Drink(name, description, color, effectType, duration, amplifier, isAlcoholic));
            } else {
                getLogger().warning("Неизвестный тип эффекта для напитка: " + key);
            }
        }
    }

    private int getColorCode(String colorInput) {
        if (colorInput.matches("\\d{1,3},\\d{1,3},\\d{1,3}")) {
            String[] rgb = colorInput.split(",");
            int r = Integer.parseInt(rgb[0]);
            int g = Integer.parseInt(rgb[1]);
            int b = Integer.parseInt(rgb[2]);

            if (r >= 0 && r <= 255 && g >= 0 && g <= 255 && b >= 0 && b <= 255) {
                return (r << 16) | (g << 8) | b; 
            } else {
                getLogger().warning("Значения RGB должны быть между 0 и 255.");
            }
        }

        return switch (colorInput.toUpperCase()) {
            case "WHITE" -> 0xFFFFFF;
            case "YELLOW" -> 0xFFFF00;
            case "RED" -> 0xFF0000;
            case "GREEN" -> 0x00FF00;
            default -> 0xFFFFFF; 
        };
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("drink")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("alcohole.reload")) {
                    reloadConfig();
                    loadDrinks();
                    sender.sendMessage(ChatColor.GREEN + "Конфигурация AlcoHole успешно перезагружена!");
                    return true;
                }

                if (sender instanceof Player && sender.hasPermission("alcohole.drink")) {
                    Player player = (Player) sender;
                    String drinkType = args[0].toLowerCase();
                    Drink drink = drinks.get(drinkType);
                    if (drink != null) {
                        giveDrink(player, drink);
                    } else {
                        player.sendMessage(ChatColor.RED + "Неизвестный тип напитка!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Только игроки могут использовать эту команду!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Пожалуйста, укажите тип напитка: " + String.join(", ", drinks.keySet()));
            }
            return true;
        }
        return false;
    }

    private void giveDrink(Player player, Drink drink) {
        ItemStack drinkItem = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) drinkItem.getItemMeta();
        meta.setDisplayName(drink.getName());
        meta.setLore(java.util.Arrays.asList(drink.getDescription()));
        meta.setColor(drink.getColor());
        meta.addCustomEffect(new PotionEffect(drink.getEffectType(), drink.getDuration() * 20, drink.getAmplifier()), true);
        drinkItem.setItemMeta(meta);
        player.getInventory().addItem(drinkItem);
        player.sendMessage(ChatColor.DARK_GRAY + "[AlcoHole] Вы получили " + drink.getName() + "!");
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("drink")) {
            if (args.length == 1) {
                String arg = args[0].toLowerCase();
                if (sender.hasPermission("alcohole.reload")) {
                    completions.add("reload");
                }
                for (String drinkType : drinks.keySet()) {
                    if (drinkType.startsWith(arg)) {
                        completions.add(drinkType);
                    }
                }
            }
        }
        return completions;
    }

    private static class Drink {
        private final String name;
        private final String description;
        private final Color color;
        private final PotionEffectType effectType;
        private final int duration;
        private final int amplifier;
        private final boolean isAlcoholic;

        public Drink(String name, String description, Color color, PotionEffectType effectType, int duration, int amplifier, boolean isAlcoholic) {
            this.name = name;
            this.description = description;
            this.color = color;
            this.effectType = effectType;
            this.duration = duration;
            this.amplifier = amplifier;
            this.isAlcoholic = isAlcoholic;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Color getColor() {
            return color;
        }

        public PotionEffectType getEffectType() {
            return effectType;
        }

        public int getDuration() {
            return duration;
        }

        public int getAmplifier() {
            return amplifier;
        }

        public boolean isAlcoholic() {
            return isAlcoholic;
        }
    }
}
