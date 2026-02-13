package net.danygames2014.nyapresence.resolving;

import de.jcm.discordgamesdk.activity.Activity;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.danygames2014.nyapresence.NyaPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldSaveConflictScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;

public class PresenceResolver {
    private final Minecraft minecraft;
    private final Object2ObjectMap<String, ResolverReplacement<String>> stringResolvers = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectMap<String, ResolverReplacement<String>> imageResolvers = new Object2ObjectOpenHashMap<>();
    
    private final Int2ObjectOpenHashMap<String> dimensionImageIds = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectOpenHashMap<String> dimensionNames = new Int2ObjectOpenHashMap<>();
    private final Object2ObjectOpenHashMap<Class<?>, String> screenStrings = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectOpenHashMap<Class<?>, String> screenImageKeys = new Object2ObjectOpenHashMap<>();

    public PresenceResolver() {
        this.minecraft = Minecraft.INSTANCE;
        initScreenStrings();
        initResolvers();
        initDimensions();
    }
    
    public void initScreenStrings() {
        // Strings
        screenStrings.put(AchievementsScreen.class, "Browsing Achievements");
        screenStrings.put(ChatScreen.class, "Chatting");
        screenStrings.put(ConfirmScreen.class, "Confirm Screen");
        screenStrings.put(DeathScreen.class, "Death Screen");
        screenStrings.put(DisconnectedScreen.class, "Disconnected Screen");
        screenStrings.put(DownloadingTerrainScreen.class, "Connecting to Server");
        screenStrings.put(FatalErrorScreen.class, "Fatal Error Screen");
        screenStrings.put(GameMenuScreen.class, "In Game Menu");
        screenStrings.put(MultiplayerScreen.class, "Multiplayer Screen");
        screenStrings.put(OutOfMemoryError.class, "Ran out of Memory");
        screenStrings.put(SleepingChatScreen.class, "Sleeping");
        screenStrings.put(StatsScreen.class, "Stats Screen");
        screenStrings.put(TitleScreen.class, "In Main Menu");
        
        screenStrings.put(CraftingScreen.class, "Crafting");
        screenStrings.put(DispenserScreen.class, "Browsing Dispenser Contents");
        screenStrings.put(DoubleChestScreen.class, "Browsing Chest Contents");
        screenStrings.put(FurnaceScreen.class, "Browsing Furnace Contents");
        screenStrings.put(HandledScreen.class, "Browing Inventory Contents");
        screenStrings.put(InventoryScreen.class, "In the Inventory");
        screenStrings.put(SignEditScreen.class, "Editing a Sign");
        
        screenStrings.put(KeybindsScreen.class, "Editing Keybinds");
        screenStrings.put(OptionsScreen.class, "Editing Options");
        screenStrings.put(VideoOptionsScreen.class, "Editing Video Options");
        screenStrings.put(PackScreen.class, "Choosing a Resource Pack");
        
        screenStrings.put(CreateWorldScreen.class, "Creating a New World");
        screenStrings.put(EditWorldScreen.class, "Editing a World");
        screenStrings.put(SelectWorldScreen.class, "Selecting a World");
        screenStrings.put(WorldSaveConflictScreen.class, "World Save Conflict Screen");
        
        // Images
        screenImageKeys.put(SleepingChatScreen.class, "bed");
        screenImageKeys.put(CraftingScreen.class, "crafting_table");
        screenImageKeys.put(DispenserScreen.class, "dispenser");
        screenImageKeys.put(DoubleChestScreen.class, "chest");
        screenImageKeys.put(FurnaceScreen.class, "furnace");
        screenImageKeys.put(SignEditScreen.class, "sign");
    }
    
    public void initDimensions() {
        for (var entry : NyaPresence.CONFIG.inGame.dimensions) {
            String[] split = entry.split(";");
            if (split.length == 3) {
                dimensionImageIds.put(Integer.parseInt(split[0]), split[1]);
                dimensionNames.put(Integer.parseInt(split[0]), split[2]);
            }
        }
    }
    
    public void initResolvers() {
        // Text Resolvers
        this.stringResolvers.put("$status", () -> {
            Screen currentScreen = minecraft.currentScreen;
            if (currentScreen == null) {
                return "In Game";
            }
            
            if (screenStrings.containsKey(currentScreen.getClass())) {
                return screenStrings.get(currentScreen.getClass());
            }
            
            return minecraft.currentScreen.getClass().getSimpleName();
        });
        
        this.stringResolvers.put("$username", () -> {
            if (minecraft.session == null) {
                return "";
            }
            
            return minecraft.session.username;
        });
        
        this.stringResolvers.put("$world_type", () -> {
            if (minecraft.world == null) {
                return "Main Menu";
            } else {
                return minecraft.world.isRemote ? "Multiplayer" : "Singleplayer";
            }
        });
        
        this.stringResolvers.put("$dimension", () -> {
            if (minecraft.world == null) {
                return "";
            }
            
            return dimensionNames.getOrDefault(minecraft.world.dimension.id, String.valueOf(minecraft.world.dimension.id));
        });
        
        this.stringResolvers.put("$health", () -> {
           if (minecraft.world != null && minecraft.player != null) {
               return String.valueOf(minecraft.player.health);
           }
           
           return "";
        });

        this.stringResolvers.put("$max_health", () -> {
            if (minecraft.world != null && minecraft.player != null) {
                return String.valueOf(minecraft.player.maxHealth);
            }

            return "";
        });

        this.stringResolvers.put("$hearts", () -> {
            if (minecraft.world != null && minecraft.player != null) {
                String value = String.valueOf(minecraft.player.health / 2D);
                return value.endsWith(".0") ? value.substring(0, value.length() - 2) : value;
            }

            return "";
        });
        
        this.stringResolvers.put("$held_item", () -> {
           if (minecraft.player != null) {
               ItemStack stack = minecraft.player.inventory.getSelectedItem();
               if (stack != null) {
                   return I18n.getTranslation(stack.getItem().getTranslationKey(stack) + ".name");
               }
           } 
           
           return "";
        });

        this.stringResolvers.put("$held_item_count", () -> {
            if (minecraft.player != null) {
                ItemStack stack = minecraft.player.inventory.getSelectedItem();
                if (stack != null) {
                    return String.valueOf(stack.count);
                }
            }

            return "";
        });

        this.stringResolvers.put("$held_item_full", () -> {
            if (minecraft.player != null) {
                ItemStack stack = minecraft.player.inventory.getSelectedItem();
                if (stack != null) {
                    return stack.count + "x " + I18n.getTranslation(stack.getItem().getTranslationKey(stack) + ".name");
                } else {
                    return "Nothing";
                }
            }

            return "";
        });
        
        this.stringResolvers.put("$time_value", () -> {
            if (minecraft.world != null) {
                return String.valueOf(minecraft.world.getTime() % 24000L);
            }
            
            return "";
        });

        this.stringResolvers.put("$time_of_day", () -> {
            if (minecraft.world != null) {
                long time = minecraft.world.getTime() % 24000L;
                if (time > 23000L) {
                    return "Sunrise";
                } else if (time > 18000L) {
                    return "Midnight";
                } else if (time > 13000L) {
                    return "Night";
                } else if (time > 12000L) {
                    return "Sunset";
                } else if (time > 6000L) {
                    return "Noon";
                } else {
                    return "Day";
                }
            }

            return "";
        });
        
        // Image Resolvers
        this.imageResolvers.put("$default", () -> {
           return NyaPresence.CONFIG.defaultImageKey; 
        });
        
        this.imageResolvers.put("$status", () -> {
            Screen currentScreen = minecraft.currentScreen;
            if (currentScreen == null) {
                return null;
            }

            if (screenImageKeys.containsKey(currentScreen.getClass())) {
                return screenImageKeys.get(currentScreen.getClass());
            }

            return null;
        });
        
        this.imageResolvers.put("$dimension", () -> {
            if (minecraft.world == null) {
                return null;
            }
            
            if (dimensionImageIds.containsKey(minecraft.world.dimension.id)) {
                return dimensionImageIds.get(minecraft.world.dimension.id);
            }
            
            return null;
        });

        this.imageResolvers.put("$time_of_day", () -> {
            if (minecraft.world != null) {
                long time = minecraft.world.getTime() % 24000L;
                if (time > 23000L) {
                    return "sunrise";
                } else if (time > 18000L) {
                    return "midnight";
                } else if (time > 13000L) {
                    return "night";
                } else if (time > 12000L) {
                    return "sunset";
                } else if (time > 6000L) {
                    return "noon";
                } else {
                    return "day";
                }
            }

            return null;
        });
    }
    
    public String resolveString(String text) {
        for (var resolver : stringResolvers.object2ObjectEntrySet()) {
            if (text.contains(resolver.getKey())) {
                text = text.replace(resolver.getKey(), resolver.getValue().resolve());
            }
        }
        
        return text;
    }
    
    public String resolveImageKey(String text, boolean large) {
        String[] split = text.split(" ");

        for (String key : split) {
            for (var resolver : imageResolvers.object2ObjectEntrySet()) {
                if (key.contains(resolver.getKey())) {
                    String resolvedKey = resolver.getValue().resolve();
                    if (resolvedKey != null) {
                        return resolvedKey;
                    }
                }
            }
        }
        
        
        return large ? NyaPresence.CONFIG.defaultImageKey : null;
    }

    public synchronized void resolve(Activity activity) {
        boolean inGame = minecraft.world != null;
        
        String state = resolveString(inGame ? NyaPresence.CONFIG.inGame.stateLine : NyaPresence.CONFIG.menu.stateLine);
        if (!state.isBlank()) {
            activity.setState(state);
        }
        
        String details = resolveString(inGame ? NyaPresence.CONFIG.inGame.detailsLine : NyaPresence.CONFIG.menu.detailsLine);
        if (!details.isBlank()) {
            activity.setDetails(details);
        }
        
        activity.assets().setLargeImage(resolveImageKey(inGame ? NyaPresence.CONFIG.inGame.largeImageKey : NyaPresence.CONFIG.menu.largeImageKey, true));
        String largeImageText = resolveString(inGame ? NyaPresence.CONFIG.inGame.largeImageText : NyaPresence.CONFIG.menu.largeImageText);
        if (!largeImageText.isBlank()) {
            activity.assets().setLargeText(largeImageText);
        }

        activity.assets().setSmallImage(resolveImageKey(inGame ? NyaPresence.CONFIG.inGame.smallImageKey : NyaPresence.CONFIG.menu.smallImageKey, false));
        String smallImageText = resolveString(inGame ? NyaPresence.CONFIG.inGame.smallImageText : NyaPresence.CONFIG.menu.smallImageText);
        if (!smallImageText.isBlank()) {
            activity.assets().setSmallText(smallImageText);
        }
    }
}
