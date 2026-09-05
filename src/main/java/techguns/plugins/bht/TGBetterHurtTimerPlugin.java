package techguns.plugins.bht;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TGBetterHurtTimerPlugin {

    private static final String[] DAMAGE_SOURCE_PATTERNS = new String[] {
        "tg_bullet",
        "tg_explosion",
        "tg_poison",
        "tg_fire",
        "tg_knockback",
        "tg_energy",
        "tg_lightning",
        "tg_dark",
        "tg_sonic",
        "tg_rad",
        "tg_rad_poisoning"
    };

    public static void init() {
        try {
            Class<?> infoClass = Class.forName("arekkuusu.betterhurttimer.api.capability.data.HurtSourceInfo");
            Constructor<?> infoConstructor = infoClass.getConstructor(CharSequence.class, boolean.class, int.class);
            Method addSource = Class.forName("arekkuusu.betterhurttimer.api.BHTAPI").getMethod("addSource", infoClass);

            for (String pattern : DAMAGE_SOURCE_PATTERNS) {
                addSource.invoke(null, infoConstructor.newInstance(pattern, false, 0));
            }
        } catch (ReflectiveOperationException e) { // Th3_Sl1ze: hope that shit won't get a random api change update..
        }
    }
}
