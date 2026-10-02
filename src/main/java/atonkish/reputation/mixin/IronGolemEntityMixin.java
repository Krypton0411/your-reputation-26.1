package atonkish.reputation.mixin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;

import atonkish.reputation.entity.passive.IronGolemEntityInterface;
import atonkish.reputation.entity.passive.VillagerEntityInterface;

@Mixin(IronGolem.class)
public abstract class IronGolemEntityMixin implements NeutralMob, IronGolemEntityInterface {
    private Map<Player, Set<Villager>> harmReports = new HashMap<>();

    public boolean addReport(Player player, Villager villager) {
        if (!harmReports.containsKey(player)) {
            Set<Villager> victims = new HashSet<>();
            harmReports.put(player, victims);
        }

        ((VillagerEntityInterface) villager).setIsSnitch(player, true);
        return this.harmReports.get(player).add(villager);
    }

    public void clearReports() {
        for (Player player : this.harmReports.keySet()) {
            for (Villager villager : this.harmReports.get(player)) {
                ((VillagerEntityInterface) villager).setIsSnitch(player, false);
            }
        }
        this.harmReports.clear();
    }

    @Override
    public void stopBeingAngry() {
        this.setLastHurtByMob(null);
        this.setPersistentAngerTarget(null);
        this.setTarget(null);
        this.setPersistentAngerEndTime(0);
        this.clearReports();
    }
}
