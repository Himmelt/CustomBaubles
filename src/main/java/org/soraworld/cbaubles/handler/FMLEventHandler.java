package org.soraworld.cbaubles.handler;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.soraworld.cbaubles.items.Bauble;
import org.soraworld.cbaubles.items.IItemStack;

public class FMLEventHandler {

    private byte ticks;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (ticks++ >= 20 && event.phase == TickEvent.Phase.START) {
            double maxHealth = 20.0D;
            double movementSpeed = 0.1D;
            double attackDamage = 1.0D;
            double knockbackResistance = 0.0D;

            PotionEffect effect = event.player.getActivePotionEffect(Potion.field_76434_w);
            if (effect != null) maxHealth += 4 * (effect.getAmplifier() + 1);
            effect = event.player.getActivePotionEffect(Potion.moveSpeed);
            if (effect != null) movementSpeed += 0.02 * (effect.getAmplifier() + 1);
            effect = event.player.getActivePotionEffect(Potion.moveSlowdown);
            if (effect != null) movementSpeed -= 0.015 * (effect.getAmplifier() + 1);
            effect = event.player.getActivePotionEffect(Potion.damageBoost);
            if (effect != null) attackDamage += 1.3 * (effect.getAmplifier() + 1);
            effect = event.player.getActivePotionEffect(Potion.weakness);
            if (effect != null) attackDamage -= 0.5 * (effect.getAmplifier() + 1);

            IInventory baubles = BaublesApi.getBaubles(event.player);
            for (int i = 0; baubles != null && i < baubles.getSizeInventory(); i++) {
                ItemStack stack = baubles.getStackInSlot(i);
                if (stack != null) {
                    Bauble bauble = ((IItemStack) (Object) stack).getBauble();
                    if (bauble != null) {
                        maxHealth += bauble.getHP();
                        movementSpeed += bauble.getSP();
                        attackDamage += bauble.getAT();
                        knockbackResistance += bauble.getKB() / 100.0D;
                    }
                }
            }

            event.player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
            event.player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(movementSpeed);
            event.player.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(attackDamage);
            event.player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(knockbackResistance);

            ticks = 0;
        }
    }

}
