package com.walkercase.efm.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ExportItemStackCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("exportis").executes((command) -> {
            return execute(command);
        }));
    }
    private static int execute(CommandContext<CommandSourceStack> command){
        if(command.getSource().getEntity() instanceof Player){
            Player player = (Player) command.getSource().getEntity();
            if(player.getItemInHand(InteractionHand.MAIN_HAND) != null) {
                CompoundTag tag = player.getItemInHand(InteractionHand.MAIN_HAND).serializeNBT();
                File f = new File("isexport-" + player.getItemInHand(InteractionHand.MAIN_HAND).getItem().toString() + "-" + System.currentTimeMillis() + ".txt");
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter(f));
                    pw.write(tag.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }finally{
                    if(pw != null)
                        pw.close();
                }
                player.sendSystemMessage(Component.literal("Printed to: " + f.getAbsolutePath()));
            }else{
                player.sendSystemMessage(Component.literal("You must be holding an item!"));
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
