package net.danygames2014.nyapresence.config;

import net.glasslauncher.mods.gcapi3.api.ConfigCategory;
import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

@SuppressWarnings("removal")
public class PresenceConfig {
    @ConfigEntry(name = "Enabled", description = "Enable Rich Presence")
    public Boolean enabled = true;
    
    @ConfigEntry(name = "Application ID", description = "Discord Application ID", maxLength = 32L, maxValue = 32L)
    public String applicationId = "933211021273890846";
    
    @ConfigEntry(name = "Default Image Key", description = "Default Image Key", maxLength = 32L, maxValue = 32L)
    public String defaultImageKey = "minecraft";
    
    @ConfigCategory(name = "In Game")
    public InGamePresenceConfig inGame = new InGamePresenceConfig();
    
    @ConfigCategory(name = "Menu")
    public MenuPresenceConfig menu = new MenuPresenceConfig();
    
    @SuppressWarnings("removal")
    public static class InGamePresenceConfig {
        @ConfigEntry(name = "Status Line", maxLength = 128L, maxValue = 128L)
        public String stateLine = "$status $health/$max_healthHP";

        @ConfigEntry(name = "Details Line", maxLength = 128L, maxValue = 128L)
        public String detailsLine = "$held_item_full";

        @ConfigEntry(name = "Large Image Key", maxLength = 128L, maxValue = 128L)
        public String largeImageKey = "$dimension";

        @ConfigEntry(name = "Large Image Text", maxLength = 128L, maxValue = 128L)
        public String largeImageText = "$dimension";

        @ConfigEntry(name = "Small Image Key", maxLength = 128L, maxValue = 128L)
        public String smallImageKey = "$status $default";

        @ConfigEntry(name = "Small Image Text", maxLength = 128L, maxValue = 128L)
        public String smallImageText = "$status";

        @ConfigEntry(name = "Dimensions", maxLength = 128L, maxValue = 128L)
        public String[] dimensions = {"0;overworld;Overworld", "-1;thenether;The Nether"};
    }

    @SuppressWarnings("removal")
    public static class MenuPresenceConfig {
        @ConfigEntry(name = "Status Line", maxLength = 128L, maxValue = 128L)
        public String stateLine = "Main Menu";

        @ConfigEntry(name = "Details Line", maxLength = 128L, maxValue = 128L)
        public String detailsLine = "$username";

        @ConfigEntry(name = "Large Image Key", maxLength = 128L, maxValue = 128L)
        public String largeImageKey = "minecraft";

        @ConfigEntry(name = "Large Image Text", maxLength = 128L, maxValue = 128L)
        public String largeImageText = "$status";

        @ConfigEntry(name = "Small Image Key", maxLength = 128L, maxValue = 128L)
        public String smallImageKey = "";

        @ConfigEntry(name = "Small Image Text", maxLength = 128L, maxValue = 128L)
        public String smallImageText = "";
    }
}
