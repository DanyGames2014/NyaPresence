package net.danygames2014.nyapresence;

import net.danygames2014.nyapresence.config.PresenceConfig;
import net.danygames2014.nyapresence.resolving.PresenceResolver;
import net.fabricmc.api.ClientModInitializer;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NyaPresence implements ClientModInitializer {
    public RichPresenceThread presenceThread;
    
    public static final Logger LOGGER = LogManager.getLogger("NyaPresence");
    
    @ConfigRoot(value = "presence", visibleName = "Rich Presence", index = 0)
    public static final PresenceConfig CONFIG = new PresenceConfig();
    
    public static PresenceResolver PRESENCE_RESOLVER;
    
    @Override
    public void onInitializeClient() {
        PRESENCE_RESOLVER = new PresenceResolver();
        
        presenceThread = new RichPresenceThread();
        presenceThread.start();
    }
}
