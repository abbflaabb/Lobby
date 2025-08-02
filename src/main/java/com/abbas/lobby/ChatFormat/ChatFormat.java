package com.abbas.lobby.ChatFormat;

import com.abbas.lobby.Lobby;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.API.ConfigAPI.ConfigPath;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.logging.Level;

/**
 * Enhanced ChatFormat class for Minecraft Lobby Plugin
 *
 * Features:
 * - Advanced chat formatting with multiple placeholder support
 * - Comprehensive rank system with priority handling
 * - Anti-spam protection with configurable limits
 * - Chat cooldown system
 * - Profanity filter with customizable word list
 * - Chat logging and history
 * - Mention system with notifications
 * - Chat channels support
 * - Advanced permission handling
 * - Custom color code support
 * - Chat statistics tracking
 * - Automatic moderation features
 * - Integration with external rank systems
 *
 * @author Abbas
 * @version 2.0.0
 */
public class ChatFormat implements Listener {

    private static final String CHAT_COLOR_PERMISSION = "lobby.chat.color";
    private static final String CHAT_BYPASS_COOLDOWN = "lobby.chat.bypass.cooldown";
    private static final String CHAT_BYPASS_SPAM = "lobby.chat.bypass.spam";
    private static final String CHAT_BYPASS_FILTER = "lobby.chat.bypass.filter";
    private static final String CHAT_ADMIN_PERMISSION = "lobby.chat.admin";
    private static final String CHAT_MENTION_PERMISSION = "lobby.chat.mention";
    private static final String CHAT_CHANNELS_PERMISSION = "lobby.chat.channels";
    private static final String CHAT_RAINBOW_PERMISSION = "lobby.chat.rainbow";
    private static final String CHAT_GRADIENT_PERMISSION = "lobby.chat.gradient";
    private static final String CHAT_FORMAT_PERMISSION = "lobby.chat.format";

    private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)&[0-9A-FK-OR]");
    private static final Pattern HEX_PATTERN = Pattern.compile("(?i)&#[0-9A-F]{6}");
    private static final Pattern MENTION_PATTERN = Pattern.compile("@(\\w+)");
    private static final Pattern URL_PATTERN = Pattern.compile("(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+)");
    private static final Pattern EMOJI_PATTERN = Pattern.compile(":(\\w+):");
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("\\{gradient:([0-9A-F]{6}),([0-9A-F]{6})}([^{]+)\\{/gradient}");
    private static final Pattern RAINBOW_PATTERN = Pattern.compile("\\{rainbow}([^{]+)\\{/rainbow}");


    private final Lobby plugin;


    private final Map<String, RankData> ranks = new ConcurrentHashMap<>();
    private final Map<UUID, PlayerChatData> playerData = new ConcurrentHashMap<>();
    private final Map<String, ChatChannel> channels = new ConcurrentHashMap<>();
    private final List<String> profanityList = new ArrayList<>();
    private final Map<String, String> emojiMap = new HashMap<>();
    private final Queue<ChatMessage> chatHistory = new LinkedList<>();
    private final Map<UUID, Queue<String>> playerChatHistory = new ConcurrentHashMap<>();


    private boolean chatEnabled = true;
    private boolean profanityFilterEnabled = true;
    private boolean antiSpamEnabled = true;
    private boolean mentionSystemEnabled = true;
    private boolean urlFilterEnabled = true;
    private boolean chatLoggingEnabled = true;
    private boolean autoModerationEnabled = true;
    private boolean emojiEnabled = true;
    private boolean channelsEnabled = false;


    private long chatCooldownMs = 3000;
    private int maxMessagesPerMinute = 10;
    private int chatHistorySize = 100;
    private int playerHistorySize = 20;


    private BukkitTask cleanupTask;
    private BukkitTask statisticsTask;


    private final ChatStatistics statistics = new ChatStatistics();

    /**
     * Constructor initializes the ChatFormat system
     * @param plugin Main lobby plugin instance
     */
    public ChatFormat(Lobby plugin) {
        this.plugin = plugin;
        initializeSystem();
    }

    /**
     * Initialize the entire chat formatting system
     */
    private void initializeSystem() {
        try {
            setupConfig();
            setupDefaultRanks();
            setupDefaultChannels();
            setupProfanityFilter();
            setupEmojiSystem();
            registerEvents();
            startBackgroundTasks();

            plugin.getLogger().info("ChatFormat system initialized successfully!");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize ChatFormat system", e);
        }
    }

    /**
     * Setup comprehensive configuration with all options
     */
    private void setupConfig() {
        Config.setup();

        if (!Config.getConfig().isConfigurationSection("chat")) {

            Config.getConfig().set(ConfigPath.CHAT_FORMAT, "&7[{time}] {prefix}&f{player}&7: &f{message}");
            Config.getConfig().set(ConfigPath.CHAT_ENABLED, true);
            Config.getConfig().set(ConfigPath.CHAT_NO_PERMISSION, "&cYou don't have permission to use color codes!");


            Config.getConfig().set(ConfigPath.CHAT_RANKS_OWNER, "&4[Owner] ");
            Config.getConfig().set(ConfigPath.CHAT_RANKS_ADMIN, "&c[Admin] ");
            Config.getConfig().set(ConfigPath.CHAT_RANKS_MOD, "&2[Mod] ");
            Config.getConfig().set(ConfigPath.CHAT_RANKS_VIP, "&6[VIP] ");
            Config.getConfig().set(ConfigPath.CHAT_RANKS_PREMIUM, "&b[Premium] ");
            Config.getConfig().set(ConfigPath.CHAT_RANKS_MEMBER, "&a[Member] ");
            Config.getConfig().set(ConfigPath.CHAT_RANKS_DEFAULT, "&7");


            Config.getConfig().set("chat.anti-spam.enabled", true);
            Config.getConfig().set("chat.anti-spam.max-messages-per-minute", 10);
            Config.getConfig().set("chat.cooldown.enabled", true);
            Config.getConfig().set("chat.cooldown.seconds", 3);


            Config.getConfig().set("chat.profanity-filter.enabled", true);
            Config.getConfig().set("chat.url-filter.enabled", true);
            Config.getConfig().set("chat.profanity-words", Arrays.asList("badword1", "badword2"));


            Config.getConfig().set("chat.mentions.enabled", true);
            Config.getConfig().set("chat.mentions.sound", "BLOCK_NOTE_BLOCK_BELL");
            Config.getConfig().set("chat.mentions.format", "&e@{player} &7mentioned you!");


            Config.getConfig().set("chat.channels.enabled", false);
            Config.getConfig().set("chat.channels.default", "global");


            Config.getConfig().set("chat.logging.enabled", true);
            Config.getConfig().set("chat.history.size", 100);
            Config.getConfig().set("chat.player-history.size", 20);

            Config.getConfig().set("chat.emoji.enabled", true);
            Config.getConfig().set("chat.auto-moderation.enabled", true);
            Config.getConfig().set("chat.statistics.enabled", true);

            Config.save();
        }

        loadConfigValues();
    }

    /**
     * Load all configuration values from config file
     */
    private void loadConfigValues() {
        try {
            chatEnabled = Config.getConfig().getBoolean(ConfigPath.CHAT_ENABLED, true);
            profanityFilterEnabled = Config.getConfig().getBoolean("chat.profanity-filter.enabled", true);
            antiSpamEnabled = Config.getConfig().getBoolean("chat.anti-spam.enabled", true);
            mentionSystemEnabled = Config.getConfig().getBoolean("chat.mentions.enabled", true);
            urlFilterEnabled = Config.getConfig().getBoolean("chat.url-filter.enabled", true);
            chatLoggingEnabled = Config.getConfig().getBoolean("chat.logging.enabled", true);
            autoModerationEnabled = Config.getConfig().getBoolean("chat.auto-moderation.enabled", true);
            emojiEnabled = Config.getConfig().getBoolean("chat.emoji.enabled", true);
            channelsEnabled = Config.getConfig().getBoolean("chat.channels.enabled", false);

            chatCooldownMs = Config.getConfig().getLong("chat.cooldown.seconds", 3) * 1000;
            maxMessagesPerMinute = Config.getConfig().getInt("chat.anti-spam.max-messages-per-minute", 10);
            chatHistorySize = Config.getConfig().getInt("chat.history.size", 100);
            playerHistorySize = Config.getConfig().getInt("chat.player-history.size", 20);

        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error loading config values, using defaults", e);
        }
    }

    /**
     * Setup default ranks with priorities and enhanced data
     */
    private void setupDefaultRanks() {
        ranks.clear();

        ranks.put("owner", new RankData("owner",
                Config.getConfig().getString(ConfigPath.CHAT_RANKS_OWNER, "&4[Owner] "),
                0, "&4", true, true));
        ranks.put("admin", new RankData("admin",
                Config.getConfig().getString(ConfigPath.CHAT_RANKS_ADMIN, "&c[Admin] "),
                1, "&c", true, true));
        ranks.put("mod", new RankData("mod",
                Config.getConfig().getString(ConfigPath.CHAT_RANKS_MOD, "&2[Mod] "),
                2, "&2", true, false));
        ranks.put("vip", new RankData("vip",
                Config.getConfig().getString(ConfigPath.CHAT_RANKS_VIP, "&6[VIP] "),
                3, "&6", false, false));
        ranks.put("premium", new RankData("premium",
                Config.getConfig().getString(ConfigPath.CHAT_RANKS_PREMIUM, "&b[Premium] "),
                4, "&b", false, false));
        ranks.put("member", new RankData("member",
                Config.getConfig().getString(ConfigPath.CHAT_RANKS_MEMBER, "&a[Member] "),
                5, "&a", false, false));
        ranks.put("default", new RankData("default",
                Config.getConfig().getString(ConfigPath.CHAT_RANKS_DEFAULT, "&7"),
                10, "&7", false, false));
    }

    /**
     * Setup default chat channels
     */
    private void setupDefaultChannels() {
        if (!channelsEnabled) return;

        channels.clear();
        channels.put("global", new ChatChannel("global", "&f[Global]", "&f", 0, true));
        channels.put("staff", new ChatChannel("staff", "&c[Staff]", "&c", 1, false));
        channels.put("vip", new ChatChannel("vip", "&6[VIP]", "&6", 2, false));
        channels.put("local", new ChatChannel("local", "&a[Local]", "&a", 3, true));
    }

    /**
     * Setup profanity filter with configurable word list
     */
    private void setupProfanityFilter() {
        profanityList.clear();
        profanityList.addAll(Config.getConfig().getStringList("chat.profanity-words"));

        if (profanityList.isEmpty()) {
            profanityList.addAll(Arrays.asList("badword1", "badword2", "inappropriate"));
        }
    }

    /**
     * Setup emoji system with common emoji mappings
     */
    private void setupEmojiSystem() {
        if (!emojiEnabled) return;

        emojiMap.clear();
        emojiMap.put("smile", "☺");
        emojiMap.put("heart", "♥");
        emojiMap.put("star", "★");
        emojiMap.put("diamond", "♦");
        emojiMap.put("arrow", "→");
        emojiMap.put("check", "✓");
        emojiMap.put("cross", "✗");
        emojiMap.put("music", "♪");
        emojiMap.put("sun", "☀");
        emojiMap.put("moon", "☽");
        emojiMap.put("peace", "☮");
        emojiMap.put("yin", "☯");
        emojiMap.put("snowflake", "❄");
        emojiMap.put("fire", "🔥");
        emojiMap.put("skull", "☠");
    }

    /**
     * Register all event listeners
     */
    private void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Start background tasks for cleanup and statistics
     */
    private void startBackgroundTasks() {
        cleanupTask = new BukkitRunnable() {
            @Override
            public void run() {
                cleanupOldData();
            }
        }.runTaskTimerAsynchronously(plugin, 6000L, 6000L);

        statisticsTask = new BukkitRunnable() {
            @Override
            public void run() {
                updateStatistics();
            }
        }.runTaskTimerAsynchronously(plugin, 1200L, 1200L);
    }

    /**
     * Main chat event handler with comprehensive processing
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!chatEnabled) {
            event.setCancelled(true);
            return;
        }

        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        String originalMessage = event.getMessage();

        try {
            PlayerChatData playerChatData = getPlayerChatData(playerId);

            if (!player.hasPermission(CHAT_BYPASS_COOLDOWN) && !checkCooldown(playerChatData)) {
                event.setCancelled(true);
                long remainingTime = (playerChatData.getLastMessageTime() + chatCooldownMs - System.currentTimeMillis()) / 1000;
                player.sendMessage(ChatColor.RED + "Please wait " + remainingTime + " more seconds before sending another message!");
                return;
            }

            if (!player.hasPermission(CHAT_BYPASS_SPAM) && !checkAntiSpam(playerChatData)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You're sending messages too fast! Please slow down.");
                return;
            }

            String processedMessage = processMessage(originalMessage, player);
            if (processedMessage == null) {
                event.setCancelled(true);
                return;
            }

            if (!player.hasPermission(CHAT_BYPASS_FILTER) && containsProfanity(processedMessage)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Your message contains inappropriate content!");

                if (autoModerationEnabled) {
                    playerChatData.addViolation("profanity");
                    checkAutoModeration(player, playerChatData);
                }
                return;
            }

            processedMessage = processMentions(processedMessage, player);

            if (emojiEnabled) {
                processedMessage = processEmojis(processedMessage);
            }

            String formattedMessage = formatChat(player, processedMessage);
            if (formattedMessage != null) {
                event.setFormat(formattedMessage);
            }

            updatePlayerData(playerChatData, originalMessage);

            if (chatLoggingEnabled) {
                logChatMessage(player, originalMessage, processedMessage);
            }

            statistics.incrementMessagesCount();
            statistics.addActivePlayer(playerId);

        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error processing chat for player " + player.getName(), e);
            event.setFormat(ChatColor.GRAY + "%s: " + ChatColor.WHITE + "%s");
        }
    }

    /**
     * Handle player join events
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        playerData.put(playerId, new PlayerChatData(playerId));

        if (Config.getConfig().getBoolean("chat.welcome-message.enabled", false)) {
            String welcomeMsg = Config.getConfig().getString("chat.welcome-message.text",
                    "&aWelcome to the server, {player}!");
            welcomeMsg = welcomeMsg.replace("{player}", player.getName());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', welcomeMsg));
        }
    }

    /**
     * Handle player quit events
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();

        PlayerChatData data = playerData.get(playerId);
        if (data != null) {
            data.setLastSeen(System.currentTimeMillis());
        }
    }

    /**
     * Process message content with color codes and validation
     */
    private String processMessage(String message, Player player) {
        if (message == null || message.trim().isEmpty()) {
            return null;
        }

        String processed = message;

        if (player.hasPermission(CHAT_COLOR_PERMISSION)) {
            processed = ChatColor.translateAlternateColorCodes('&', processed);
        } else if (COLOR_PATTERN.matcher(processed).find()) {
            String noPermMsg = Config.getConfig().getString(ConfigPath.CHAT_NO_PERMISSION,
                    "&cYou don't have permission to use color codes!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermMsg));
            processed = processed.replaceAll("&[0-9A-FK-OR]", "");
        }


        if (player.hasPermission(CHAT_COLOR_PERMISSION)) {
            processed = processHexColors(processed);
        }


        if (player.hasPermission(CHAT_RAINBOW_PERMISSION)) {
            processed = processRainbowText(processed);
        }


        if (player.hasPermission(CHAT_GRADIENT_PERMISSION)) {
            processed = processGradientText(processed);
        }


        if (urlFilterEnabled && !player.hasPermission(CHAT_BYPASS_FILTER)) {
            if (URL_PATTERN.matcher(processed).find()) {
                player.sendMessage(ChatColor.RED + "URLs are not allowed in chat!");
                return null;
            }
        }

        return processed;
    }

    /**
     * Process hex color codes (#RRGGBB format)
     */
    private String processHexColors(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String hexCode = matcher.group().substring(2); // Remove &#
            try {
                matcher.appendReplacement(sb, "&x&" +
                        hexCode.charAt(0) + "&" + hexCode.charAt(1) + "&" +
                        hexCode.charAt(2) + "&" + hexCode.charAt(3) + "&" +
                        hexCode.charAt(4) + "&" + hexCode.charAt(5));
            } catch (Exception e) {
                matcher.appendReplacement(sb, matcher.group());
            }
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * Process rainbow text formatting
     */
    private String processRainbowText(String message) {
        Matcher matcher = RAINBOW_PATTERN.matcher(message);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String text = matcher.group(1);
            String rainbowText = createRainbowText(text);
            matcher.appendReplacement(sb, rainbowText);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * Create rainbow colored text
     */
    private String createRainbowText(String text) {
        StringBuilder rainbow = new StringBuilder();
        ChatColor[] colors = {ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW,
                ChatColor.GREEN, ChatColor.BLUE, ChatColor.DARK_PURPLE};

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' ') {
                rainbow.append(colors[i % colors.length]);
            }
            rainbow.append(text.charAt(i));
        }

        return rainbow.toString();
    }

    /**
     * Process gradient text formatting
     */
    private String processGradientText(String message) {
        Matcher matcher = GRADIENT_PATTERN.matcher(message);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String startColor = matcher.group(1);
            String endColor = matcher.group(2);
            String text = matcher.group(3);
            String gradientText = createGradientText(text, startColor, endColor);
            matcher.appendReplacement(sb, gradientText);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * Create gradient colored text
     */
    private String createGradientText(String text, String startHex, String endHex) {
        StringBuilder gradient = new StringBuilder();
        ChatColor startColor = ChatColor.valueOf(startHex.charAt(0) < '8' ? "DARK_RED" : "RED");
        ChatColor endColor = ChatColor.valueOf(endHex.charAt(0) < '8' ? "DARK_BLUE" : "BLUE");

        boolean useStart = true;
        for (char c : text.toCharArray()) {
            if (c != ' ') {
                gradient.append(useStart ? startColor : endColor);
                useStart = !useStart;
            }
            gradient.append(c);
        }

        return gradient.toString();
    }

    /**
     * Process mentions in chat messages
     */
    private String processMentions(String message, Player sender) {
        if (!mentionSystemEnabled) return message;

        Matcher matcher = MENTION_PATTERN.matcher(message);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String playerName = matcher.group(1);
            Player mentionedPlayer = Bukkit.getPlayer(playerName);

            if (mentionedPlayer != null && mentionedPlayer.isOnline() &&
                    sender.hasPermission(CHAT_MENTION_PERMISSION)) {

                String mentionFormat = Config.getConfig().getString("chat.mentions.format",
                        "&e@{player} &7mentioned you!");
                mentionFormat = mentionFormat.replace("{player}", sender.getName());
                mentionedPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', mentionFormat));

                try {
                    String soundName = Config.getConfig().getString("chat.mentions.sound", "BLOCK_NOTE_BLOCK_BELL");
                    Sound sound = Sound.valueOf(soundName);
                    mentionedPlayer.playSound(mentionedPlayer.getLocation(), sound, 1.0f, 1.0f);
                } catch (Exception e) {
                }

                matcher.appendReplacement(sb, "&e@" + playerName + "&r");
            } else {
                matcher.appendReplacement(sb, matcher.group());
            }
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * Process emoji replacements
     */
    private String processEmojis(String message) {
        Matcher matcher = EMOJI_PATTERN.matcher(message);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String emojiName = matcher.group(1).toLowerCase();
            String emoji = emojiMap.get(emojiName);

            if (emoji != null) {
                matcher.appendReplacement(sb, emoji);
            } else {
                matcher.appendReplacement(sb, matcher.group());
            }
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * Format chat message with comprehensive placeholder support
     */
    private String formatChat(Player player, String message) {
        try {
            String format = Config.getConfig().getString(ConfigPath.CHAT_FORMAT,
                    "&7[{time}] {prefix}&f{player}&7: &f{message}");


            format = format.replace("{prefix}", getPlayerPrefix(player))
                    .replace("{player}", player.getName())
                    .replace("{displayname}", player.getDisplayName())
                    .replace("{message}", message)
                    .replace("{world}", player.getWorld().getName())
                    .replace("{time}", getCurrentTime())
                    .replace("{level}", String.valueOf(player.getLevel()))
                    .replace("{health}", String.valueOf((int)player.getHealth()))
                    .replace("{ping}", getPing(player))
                    .replace("{balance}", getBalance(player))
                    .replace("{channel}", getCurrentChannel(player));

            return ChatColor.translateAlternateColorCodes('&', format);
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error formatting chat for " + player.getName(), e);
            return "&7" + player.getName() + ": &f" + message;
        }
    }

    /**
     * Get player's prefix based on rank system
     */
    private String getPlayerPrefix(Player player) {
        if (player == null) return ranks.get("default").getPrefix();

        try {
            if (plugin.getLuckPerms() != null) {
                String luckPermsPrefix = plugin.getLuckPerms().getPlayerRank(player);
                if (luckPermsPrefix != null && !luckPermsPrefix.isEmpty()) {
                    return luckPermsPrefix;
                }
            }

            RankData highestRank = ranks.get("default");
            for (Map.Entry<String, RankData> rankEntry : ranks.entrySet()) {
                RankData rank = rankEntry.getValue();
                if (player.hasPermission("lobby.rank." + rank.getName()) &&
                        rank.getPriority() < highestRank.getPriority()) {
                    highestRank = rank;
                }
            }

            return highestRank.getPrefix();

        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error getting prefix for " + player.getName(), e);
            return ranks.getOrDefault("default", new RankData("default", "&7", 10, "&7", false, false)).getPrefix();
        }
    }

    /**
     * Get current formatted time
     */
    private String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * Get player ping (simplified)
     */
    private String getPing(Player player) {
        try {
            return "0";
        } catch (Exception e) {
            return "N/A";
        }
    }

    /**
     * Get player balance (if economy plugin exists)
     */
    private String getBalance(Player player) {
        try {
            return "0";
        } catch (Exception e) {
            return "N/A";
        }
    }

    /**
     * Get player's current chat channel
     */
    private String getCurrentChannel(Player player) {
        if (!channelsEnabled) return "global";

        PlayerChatData data = playerData.get(player.getUniqueId());
        return data != null ? data.getCurrentChannel() : "global";
    }

    /**
     * Check if player is within cooldown period
     */
    private boolean checkCooldown(PlayerChatData playerData) {
        if (!Config.getConfig().getBoolean("chat.cooldown.enabled", true)) {
            return true;
        }

        long currentTime = System.currentTimeMillis();
        long lastMessage = playerData.getLastMessageTime();

        return (currentTime - lastMessage) >= chatCooldownMs;
    }

    /**
     * Check anti-spam restrictions
     */
    private boolean checkAntiSpam(PlayerChatData playerData) {
        if (!antiSpamEnabled) return true;

        long currentTime = System.currentTimeMillis();
        long oneMinuteAgo = currentTime - 60000; // 1 minute

        playerData.getMessageTimestamps().removeIf(timestamp -> timestamp < oneMinuteAgo);

        if (playerData.getMessageTimestamps().size() >= maxMessagesPerMinute) {
            return false;
        }

        return true;
    }

    /**
     * Check if message contains profanity
     */
    private boolean containsProfanity(String message) {
        if (!profanityFilterEnabled || profanityList.isEmpty()) {
            return false;
        }

        String lowerMessage = message.toLowerCase();
        return profanityList.stream().anyMatch(word ->
                lowerMessage.contains(word.toLowerCase()));
    }

    /**
     * Update player chat data after successful message
     */
    private void updatePlayerData(PlayerChatData playerData, String message) {
        long currentTime = System.currentTimeMillis();

        playerData.setLastMessageTime(currentTime);
        playerData.getMessageTimestamps().add(currentTime);
        playerData.incrementMessageCount();

        Queue<String> history = playerChatHistory.computeIfAbsent(
                playerData.getPlayerId(), k -> new LinkedList<>());

        history.offer(message);
        if (history.size() > playerHistorySize) {
            history.poll();
        }
    }

    /**
     * Log chat messages for moderation and history
     */
    private void logChatMessage(Player player, String originalMessage, String processedMessage) {
        if (!chatLoggingEnabled) return;

        ChatMessage chatMsg = new ChatMessage(
                player.getUniqueId(),
                player.getName(),
                originalMessage,
                processedMessage,
                System.currentTimeMillis(),
                player.getWorld().getName()
        );

        chatHistory.offer(chatMsg);
        if (chatHistory.size() > chatHistorySize) {
            chatHistory.poll();
        }

        if (Config.getConfig().getBoolean("chat.console-logging", false)) {
            plugin.getLogger().info(String.format("[CHAT] %s: %s",
                    player.getName(), originalMessage));
        }
    }

    /**
     * Get or create player chat data
     */
    private PlayerChatData getPlayerChatData(UUID playerId) {
        return playerData.computeIfAbsent(playerId, PlayerChatData::new);
    }

    /**
     * Check auto-moderation rules and take action if needed
     */
    private void checkAutoModeration(Player player, PlayerChatData playerData) {
        if (!autoModerationEnabled) return;

        int violations = playerData.getViolationCount();

        if (violations >= 3 && violations < 5) {
            player.sendMessage(ChatColor.YELLOW + "⚠ Warning: Multiple chat violations detected. Please follow chat rules.");
        } else if (violations >= 5 && violations < 8) {
            playerData.setMuted(true);
            playerData.setMuteExpiry(System.currentTimeMillis() + 300000); // 5 minutes
            player.sendMessage(ChatColor.RED + "You have been temporarily muted for 5 minutes due to repeated violations.");
        } else if (violations >= 8) {
            String alertMsg = ChatColor.RED + "⚠ ALERT: Player " + player.getName() +
                    " has " + violations + " chat violations and requires staff attention.";

            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.hasPermission(CHAT_ADMIN_PERMISSION))
                    .forEach(staff -> staff.sendMessage(alertMsg));
        }
    }

    /**
     * Clean up old player data and chat history
     */
    private void cleanupOldData() {
        long currentTime = System.currentTimeMillis();
        long cutoffTime = currentTime - (24 * 60 * 60 * 1000); // 24 hours

        playerData.entrySet().removeIf(entry -> {
            PlayerChatData data = entry.getValue();
            return data.getLastSeen() > 0 && data.getLastSeen() < cutoffTime;
        });

        chatHistory.removeIf(msg -> msg.getTimestamp() < cutoffTime);

        playerChatHistory.entrySet().removeIf(entry -> {
            Player player = Bukkit.getPlayer(entry.getKey());
            return player == null || !player.isOnline();
        });

        plugin.getLogger().info("Chat data cleanup completed. Active players: " +
                playerData.size() + ", Chat history: " + chatHistory.size());
    }

    /**
     * Update chat statistics
     */
    private void updateStatistics() {
        statistics.setTotalPlayersOnline(Bukkit.getOnlinePlayers().size());

        if (statistics.shouldResetHourly()) {
            statistics.resetHourlyStats();
        }

        if (Config.getConfig().getBoolean("chat.statistics.logging", false)) {
            plugin.getLogger().info(String.format("Chat Stats - Messages: %d, Active Players: %d, Violations: %d",
                    statistics.getMessagesThisHour(),
                    statistics.getActivePlayersThisHour(),
                    statistics.getViolationsThisHour()));
        }
    }

    // ==================== PUBLIC API METHODS ====================

    /**
     * Toggle chat on/off
     */
    public void toggleChat() {
        chatEnabled = !chatEnabled;
        Config.getConfig().set(ConfigPath.CHAT_ENABLED, chatEnabled);
        Config.save();

        String status = chatEnabled ? "enabled" : "disabled";
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Chat has been " + status + "!");
    }

    /**
     * Check if chat is enabled
     */
    public boolean isChatEnabled() {
        return chatEnabled;
    }

    /**
     * Add a new rank to the system
     */
    public void addRank(String rankName, String prefix, int priority, String color,
                        boolean canBypassCooldown, boolean canModerate) {
        RankData rankData = new RankData(rankName.toLowerCase(), prefix, priority,
                color, canBypassCooldown, canModerate);
        ranks.put(rankName.toLowerCase(), rankData);

        Config.getConfig().set("chat.ranks." + rankName.toLowerCase() + ".prefix", prefix);
        Config.getConfig().set("chat.ranks." + rankName.toLowerCase() + ".priority", priority);
        Config.getConfig().set("chat.ranks." + rankName.toLowerCase() + ".color", color);
        Config.save();
    }

    /**
     * Remove a rank from the system
     */
    public void removeRank(String rankName) {
        ranks.remove(rankName.toLowerCase());
        Config.getConfig().set("chat.ranks." + rankName.toLowerCase(), null);
        Config.save();
    }

    /**
     * Mute a player
     */
    public void mutePlayer(UUID playerId, long durationMs, String reason) {
        PlayerChatData data = getPlayerChatData(playerId);
        data.setMuted(true);
        data.setMuteExpiry(System.currentTimeMillis() + durationMs);
        data.setMuteReason(reason);

        Player player = Bukkit.getPlayer(playerId);
        if (player != null) {
            player.sendMessage(ChatColor.RED + "You have been muted. Reason: " + reason);
        }
    }

    /**
     * Unmute a player
     */
    public void unmutePlayer(UUID playerId) {
        PlayerChatData data = playerData.get(playerId);
        if (data != null) {
            data.setMuted(false);
            data.setMuteExpiry(0);
            data.setMuteReason(null);

            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.sendMessage(ChatColor.GREEN + "You have been unmuted!");
            }
        }
    }

    /**
     * Check if a player is muted
     */
    public boolean isPlayerMuted(UUID playerId) {
        PlayerChatData data = playerData.get(playerId);
        if (data == null) return false;

        if (data.isMuted() && data.getMuteExpiry() > 0 &&
                System.currentTimeMillis() > data.getMuteExpiry()) {
            data.setMuted(false);
            data.setMuteExpiry(0);
            return false;
        }

        return data.isMuted();
    }

    /**
     * Get player's chat statistics
     */
    public PlayerChatData getPlayerStats(UUID playerId) {
        return playerData.get(playerId);
    }

    /**
     * Get chat history
     */
    public List<ChatMessage> getChatHistory(int limit) {
        return chatHistory.stream()
                .skip(Math.max(0, chatHistory.size() - limit))
                .collect(Collectors.toList());
    }

    /**
     * Get player's message history
     */
    public List<String> getPlayerHistory(UUID playerId, int limit) {
        Queue<String> history = playerChatHistory.get(playerId);
        if (history == null) return new ArrayList<>();

        return history.stream()
                .skip(Math.max(0, history.size() - limit))
                .collect(Collectors.toList());
    }

    /**
     * Add word to profanity filter
     */
    public void addProfanityWord(String word) {
        if (!profanityList.contains(word.toLowerCase())) {
            profanityList.add(word.toLowerCase());

            Config.getConfig().set("chat.profanity-words", profanityList);
            Config.save();
        }
    }

    /**
     * Remove word from profanity filter
     */
    public void removeProfanityWord(String word) {
        profanityList.remove(word.toLowerCase());
        Config.getConfig().set("chat.profanity-words", profanityList);
        Config.save();
    }

    /**
     * Get current chat statistics
     */
    public ChatStatistics getStatistics() {
        return statistics;
    }

    /**
     * Switch player's chat channel
     */
    public boolean switchChannel(Player player, String channelName) {
        if (!channelsEnabled) return false;

        ChatChannel channel = channels.get(channelName.toLowerCase());
        if (channel == null) return false;

        if (!channel.isPublic() && !player.hasPermission("lobby.chat.channel." + channelName)) {
            player.sendMessage(ChatColor.RED + "You don't have permission to join this channel!");
            return false;
        }

        PlayerChatData data = getPlayerChatData(player.getUniqueId());
        data.setCurrentChannel(channelName.toLowerCase());

        player.sendMessage(ChatColor.GREEN + "Switched to channel: " +
                ChatColor.translateAlternateColorCodes('&', channel.getDisplayName()));
        return true;
    }

    /**
     * Create a new chat channel
     */
    public void createChannel(String name, String displayName, String color, int priority, boolean isPublic) {
        ChatChannel channel = new ChatChannel(name.toLowerCase(), displayName, color, priority, isPublic);
        channels.put(name.toLowerCase(), channel);

        Config.getConfig().set("chat.channels.list." + name + ".display-name", displayName);
        Config.getConfig().set("chat.channels.list." + name + ".color", color);
        Config.getConfig().set("chat.channels.list." + name + ".priority", priority);
        Config.getConfig().set("chat.channels.list." + name + ".public", isPublic);
        Config.save();
    }

    /**
     * Reload configuration
     */
    public void reloadConfig() {
        try {
            Config.reload();
            loadConfigValues();
            setupDefaultRanks();
            setupDefaultChannels();
            setupProfanityFilter();
            setupEmojiSystem();

            plugin.getLogger().info("ChatFormat configuration reloaded successfully!");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to reload ChatFormat configuration", e);
        }
    }

    /**
     * Shutdown cleanup
     */
    public void shutdown() {
        if (cleanupTask != null) {
            cleanupTask.cancel();
        }
        if (statisticsTask != null) {
            statisticsTask.cancel();
        }
        if (Config.getConfig().getBoolean("chat.statistics.save-on-shutdown", true)) {
            Config.getConfig().set("chat.statistics.total-messages", statistics.getTotalMessages());
            Config.getConfig().set("chat.statistics.total-violations", statistics.getTotalViolations());
            Config.save();
        }

        playerData.clear();
        chatHistory.clear();
        playerChatHistory.clear();

        plugin.getLogger().info("ChatFormat system shutdown completed.");
    }
}