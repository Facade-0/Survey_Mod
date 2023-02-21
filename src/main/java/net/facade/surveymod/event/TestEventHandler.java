package net.facade.surveymod.event;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TestEventHandler implements AttackEntityCallback {
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if(entity instanceof SheepEntity && !world.isClient()) {
            player.sendMessage(Text.literal(player.getName().getString() + "hurt a sheep!"));
            //player.getServer().sendMessage(Text.literal("hit"));
        }
        return ActionResult.PASS;
    }
}
