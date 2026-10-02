package atonkish.reputation.mixin;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import atonkish.reputation.entity.passive.IronGolemEntityInterface;

@Mixin(DefendVillageTargetGoal.class)
public class DefendVillageTargetGoalMixin {
    @Shadow
    private IronGolem golem;

    @Shadow
    @Nullable
    private LivingEntity potentialTarget;

    @Shadow
    private TargetingConditions attackTargeting;

    @Inject(at = @At("HEAD"), method = "canUse", cancellable = true)
    public void canUse(CallbackInfoReturnable<Boolean> infoReturnable) {
        AABB box = this.golem.getBoundingBox().inflate(10.0, 8.0, 10.0);
        ServerLevel serverLevel = (ServerLevel) this.golem.level();
        List<Villager> villagers = serverLevel.getNearbyEntities(Villager.class,
                this.attackTargeting, this.golem, box);
        List<Player> players = serverLevel.getNearbyPlayers(this.attackTargeting, this.golem, box);
        for (Villager villager : villagers) {
            for (Player player : players) {
                int reputation = villager.getPlayerReputation(player);
                if (reputation > -100) {
                    continue;
                }

                ((IronGolemEntityInterface) this.golem).addReport(player, villager);
                this.potentialTarget = player;
            }
        }

        if (this.potentialTarget == null) {
            infoReturnable.setReturnValue(false);
            return;
        }

        infoReturnable.setReturnValue(!(this.potentialTarget instanceof Player)
                || !this.potentialTarget.isSpectator() && !((Player) this.potentialTarget).isCreative());
    }
}
