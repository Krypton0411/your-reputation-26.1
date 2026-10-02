package atonkish.reputation.mixin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;

import atonkish.reputation.entity.passive.VillagerEntityInterface;

@Mixin(Villager.class)
public class VillagerEntityMixin implements VillagerEntityInterface {
    private Map<Player, Boolean> snitchRecords = new HashMap<>();

    public boolean isSnitch(Player player) {
        return Optional
                .ofNullable(this.snitchRecords.get(player))
                .orElse(false);
    }

    public void setIsSnitch(Player player, boolean isSnitch) {
        this.snitchRecords.put(player, isSnitch);
    }
}
