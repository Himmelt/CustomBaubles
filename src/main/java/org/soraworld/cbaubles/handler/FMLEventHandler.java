package org.soraworld.cbaubles.handler;

import com.azanor.baubles.api.BaublesApi;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.soraworld.cbaubles.items.Bauble;
import org.soraworld.cbaubles.items.IItemStack;

public class FMLEventHandler {

    private byte ticks;

    @SubscribeEvent
    public void on(TickEvent.PlayerTickEvent event) {
        if (ticks++ >= 20 && event.phase == TickEvent.Phase.START) {
            float max_health = 20;
            IInventory baubles = BaublesApi.getBaubles(event.player);
            for (int i = 0; baubles != null && i < baubles.getSizeInventory(); i++) {
                ItemStack stack = baubles.getStackInSlot(i);
                if (stack != null) {
                    Bauble bauble = ((IItemStack) (Object) stack).getBauble();
                    if (bauble != null) max_health += bauble.getHP();
                }
            }
            event.player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(max_health);
            //event.player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
            ticks = 0;
        }
    }

}
