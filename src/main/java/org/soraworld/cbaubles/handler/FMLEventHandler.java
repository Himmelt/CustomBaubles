package org.soraworld.cbaubles.handler;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.soraworld.cbaubles.items.Bauble;
import org.soraworld.cbaubles.items.IItemStack;

import java.util.UUID;

public class FMLEventHandler {

    private byte ticks;

    private static final UUID maxHealthUUID = UUID.fromString("da36b4ca-95ca-4614-9cb6-bc0c02bd9875");
    private static final UUID moveSpeedUUID = UUID.fromString("05be2ef3-fd85-48d9-b1d0-0df3bb11f1c2");
    private static final UUID attackDamageUUID = UUID.fromString("4652a225-f1f2-43ca-977f-aa6a8bb4a0cf");
    private static final UUID knockResistUUID = UUID.fromString("76a2b773-3e97-48fc-a901-f8b9d464b685");

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (ticks++ >= 25 && event.phase == TickEvent.Phase.START) {
            double maxHealth = 0.0D, moveSpeed = 0.0D, attackDamage = 0.0D, knockResist = 0.0D;
            IInventory baubles = BaublesApi.getBaubles(event.player);
            for (int i = 0; baubles != null && i < baubles.getSizeInventory(); i++) {
                ItemStack stack = baubles.getStackInSlot(i);
                if (stack != null) {
                    Bauble bauble = ((IItemStack) (Object) stack).getBauble();
                    if (bauble != null) {
                        maxHealth += bauble.getHP();
                        moveSpeed += bauble.getSP();
                        attackDamage += bauble.getAT();
                        knockResist += bauble.getKB() / 100.0D;
                    }
                }
            }

            AttributeModifier maxHealthModifier = new AttributeModifier(maxHealthUUID, "maxHealth", maxHealth, 0);
            event.player.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(maxHealthModifier);
            if (maxHealth != 0) {
                event.player.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(maxHealthModifier);
            }

            AttributeModifier moveSpeedModifier = new AttributeModifier(moveSpeedUUID, "moveSpeed", moveSpeed, 0);
            event.player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(moveSpeedModifier);
            if (moveSpeed != 0) {
                event.player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(moveSpeedModifier);
            }

            AttributeModifier attackDamageModifier = new AttributeModifier(attackDamageUUID, "attackDamage", attackDamage, 0);
            event.player.getEntityAttribute(SharedMonsterAttributes.attackDamage).removeModifier(attackDamageModifier);
            if (attackDamage != 0) {
                event.player.getEntityAttribute(SharedMonsterAttributes.attackDamage).applyModifier(attackDamageModifier);
            }

            AttributeModifier knockResistModifier = new AttributeModifier(knockResistUUID, "knockResist", knockResist, 0);
            event.player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).removeModifier(knockResistModifier);
            if (knockResist != 0) {
                event.player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(knockResistModifier);
            }

            ticks = 0;
        }
    }

}
