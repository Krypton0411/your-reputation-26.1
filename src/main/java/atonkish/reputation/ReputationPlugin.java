package atonkish.reputation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.npc.villager.Villager;

import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.JadeIds;
import snownee.jade.api.WailaPlugin;

import atonkish.reputation.provider.IronGolemProvider;
import atonkish.reputation.provider.VillagerReputationProvider;
import atonkish.reputation.provider.VillagerSnitchProvider;

@WailaPlugin
public class ReputationPlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(IronGolemProvider.INSTANCE, IronGolem.class);
        registration.registerEntityDataProvider(VillagerReputationProvider.INSTANCE, Villager.class);
        registration.registerEntityDataProvider(VillagerSnitchProvider.INSTANCE, Villager.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(IronGolemProvider.Client.INSTANCE, IronGolem.class);
        registration.registerEntityComponent(VillagerReputationProvider.Client.INSTANCE, Villager.class);
        registration.registerEntityComponent(VillagerSnitchProvider.Client.INSTANCE, Villager.class);

        registration.addTooltipCollectedCallback((rootElement, accessor) -> {
            if (accessor instanceof EntityAccessor entityAccessor) {
                Entity entity = entityAccessor.getEntity();
                if (entity instanceof Villager) {
                    rootElement.getTooltip().remove(JadeIds.CORE_OBJECT_NAME);
                }
            }
        });
    }
}
