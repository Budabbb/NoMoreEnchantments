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
            replaceEnchantedItems(player);
        }
    }

    @SubscribeEvent
    public void onContainerOpen(net.minecraftforge.event.entity.player.PlayerContainerEvent.Open event) {
        Player player = event.getPlayer();
        AbstractContainerMenu container = event.getContainer();

        if (player != null && container != null) {
            replaceEnchantedItemsInContainer(player, container);
        }
    }

    private void replaceEnchantedItems(Player player) {
        replaceEnchantedTools(player);
        replaceEnchantedArmor(player);
        replaceEnchantedBooks(player); // Optional if you still want to replace enchanted books in inventory
    }

    private void replaceEnchantedItemsInContainer(Player player, AbstractContainerMenu container) {
        replaceEnchantedToolsInContainer(player, container);
        replaceEnchantedArmorInContainer(player, container);
        replaceEnchantedBooksInContainer(player, container);
    }

    private void replaceEnchantedTools(Player player) {
        List<ItemStack> items = player.getInventory().items;

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (stack.isEnchanted() && stack.isDamageableItem()) { // Check if item is enchanted and can be damaged (tool/armor)
                items.set(i, new ItemStack(stack.getItem())); // Replace with unenchanted version
            }
        }
    }

    private void replaceEnchantedToolsInContainer(Player player, AbstractContainerMenu container) {
        List<net.minecraft.world.inventory.Slot> slots = container.slots;

        for (net.minecraft.world.inventory.Slot slot : slots) {
            ItemStack stack = slot.getItem();
            if (stack.isEnchanted() && stack.isDamageableItem()) {
                slot.set(new ItemStack(stack.getItem())); // Replace with unenchanted version in the same slot
            }
        }
    }

    private void replaceEnchantedArmor(Player player) {
        // Iterate through armor slots
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (armorStack.isEnchanted() && armorStack.isDamageableItem() && isArmor(armorStack.getItem())) {
                player.setItemSlot(armorStack.getEquipmentSlot(), new ItemStack(armorStack.getItem())); // Replace armor piece with unenchanted version
            }
        }
    }

    private void replaceEnchantedArmorInContainer(Player player, AbstractContainerMenu container) {
        // Iterate through container slots
        for (net.minecraft.world.inventory.Slot slot : container.slots) {
            ItemStack stack = slot.getItem();
            if (stack.isEnchanted() && stack.isDamageableItem() && isArmor(stack.getItem())) {
                slot.set(new ItemStack(stack.getItem())); // Replace with unenchanted version in the same slot
            }
        }
    }

    private boolean isArmor(net.minecraft.world.item.Item item) {
        return item instanceof net.minecraft.world.item.ArmorItem;
    }

    private void replaceEnchantedBooks(Player player) {
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
                slot.set(new ItemStack(Items.BOOK)); // Replace enchanted book with normal book in the same slot
            }
        }
    }
}
