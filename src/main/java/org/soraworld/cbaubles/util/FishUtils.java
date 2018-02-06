package org.soraworld.cbaubles.util;

import net.minecraft.client.Minecraft;

import java.lang.reflect.Method;

public class FishUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static Method method;

    private static boolean enable = false;

    public static void rightClick() {
        try {
            if (method == null) {
                method = Minecraft.class.getDeclaredMethod("func_147121_ag");
                method.setAccessible(true);
            }
            method.invoke(mc);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static boolean isEnable() {
        return enable;
    }

    public static void setEnable(boolean enable) {
        FishUtils.enable = enable;
    }
}
