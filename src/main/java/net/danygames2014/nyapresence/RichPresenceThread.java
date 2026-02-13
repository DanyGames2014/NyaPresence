package net.danygames2014.nyapresence;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;

import java.time.Instant;

public class RichPresenceThread extends Thread {
    boolean runRpc = false;
    Activity activity;
    Core core;
    int updateCounter = 0;

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        try (CreateParams params = new CreateParams()) {
            params.setClientID(Long.parseLong(NyaPresence.CONFIG.applicationId));
            params.setFlags(CreateParams.getDefaultFlags());
            
            try (Core core = new Core(params)){
                this.core = core;
                activity = initActivity(core);
                
                while (runRpc) {
                    core.runCallbacks();

                    updateCounter++;
                    if (updateCounter >= 10) {
                        if (NyaPresence.CONFIG.enabled) {
                            updateActivity();
                        } else {
                            core.activityManager().clearActivity();
                        }
                        
                        updateCounter = 0;
                    }
                    
                    try {
                        //noinspection BusyWait
                        Thread.sleep(NyaPresence.CONFIG.enabled ? 200 : 2000);
                    } catch (InterruptedException e) {
                        NyaPresence.LOGGER.error("Rich Presence thread interrupted.", e);
                    }
                }
            }
        }
    }
    
    public void updateActivity() {
        NyaPresence.PRESENCE_RESOLVER.resolve(activity);
        core.activityManager().updateActivity(activity);
    }
    
    public Activity initActivity(Core core) {
        try (Activity activity = new Activity()){
            activity.setType(ActivityType.PLAYING);

            activity.timestamps().setStart(Instant.now());

            core.activityManager().updateActivity(activity);
            
            runRpc = true;
            return activity;
        } catch (Exception e) {
            return null;
        }
    }
}
