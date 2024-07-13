package hu.budabbb.nomoreenchantments;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.world.entity.player.Player;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.List;

@Mod("nomoreenchantments")
public class NoMoreEnchantments
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public NoMoreEnchantments()
    {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
    }

    private void processIMC(final InterModProcessEvent event)
    {
    }
    @SubscribeEvent
    public void onPlayerItemPickup(PlayerEvent.ItemPickupEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            replaceEnchantedBook(player);
        }
    }

    @SubscribeEvent
    public void onContainerOpen(net.minecraftforge.event.entity.player.PlayerContainerEvent.Open event) {
        Player player = event.getPlayer();
        AbstractContainerMenu container = event.getContainer();

        if (player != null && container != null) {
            replaceEnchantedBooksInContainer(player, container);
        }
    }

    private void replaceEnchantedBook(Player player) {
        List<ItemStack> items = player.getInventory().items;

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (stack.getItem() == Items.ENCHANTED_BOOK) {
                items.set(i, new ItemStack(Items.BOOK));
            }
        }
    }

    private void replaceEnchantedBooksInContainer(Player player, AbstractContainerMenu container) {
        List<net.minecraft.world.inventory.Slot> slots = container.slots;

        for (net.minecraft.world.inventory.Slot slot : slots) {
            ItemStack stack = slot.getItem();
            if (stack.getItem() == Items.ENCHANTED_BOOK) {
                // Replace enchanted book with a normal book in the same slot
                slot.set(new ItemStack(Items.BOOK));
            }
        }
    }
}
