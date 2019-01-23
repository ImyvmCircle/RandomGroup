package com.imyvm.RandomGroup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Commands implements CommandExecutor {

    private RandomGroup plugin;

    public Commands(RandomGroup pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmdObj, String label, String[] args) {
        if (args.length<=1){
            sender.sendMessage("/rgroup start [amount/Integer]");
            return false;
        }
        String cmd = args[0];
        if (cmd.equalsIgnoreCase("start")){
            if (!sender.hasPermission("RandomGroup.start")){
                return false;
            }
            List<String> players = getPlayers();
            List<Integer> seeds = new ArrayList<>();
            List<String> sortedPlayers = new ArrayList<>();
            int ii = 0;
            if (!isInt(args[1])){
                sender.sendMessage("/rgroup start [amount/Integer]");
                return false;
            }
            if (Integer.valueOf(args[1])>players.size()){
                sender.sendMessage("组数应不大于 "+players.size());
                return false;
            }
            for (String i:players){
                int seed = IntRandomNumberGenerator(0, players.size());
                seeds.add(seed);
            }
            Integer[] strArr = seeds.parallelStream().toArray(Integer[]::new);
            int[] sortedIndices = IntStream.range(0, strArr.length)
                    .boxed().sorted((i, j) -> strArr[i].compareTo(strArr[j]) )
                    .mapToInt(ele -> ele).toArray();
            for (Integer j:sortedIndices){
                String p = players.get(j);
                sortedPlayers.add(p);
            }
            if (Integer.valueOf(args[1])>plugin.names.size()){
                sender.sendMessage("error config");
                return false;
            }
            List<List<String>> groups = nPartition(sortedPlayers, Integer.valueOf(args[1]));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&6分组结果如下："));
            for (List<String> g:groups){
                String group = String.join(", ", g);
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&"+plugin.names.get(ii)+": "+group));
                ii++;
            }
        }

        return true;
    }

    private List<String> getPlayers(){
        List<String> players = new ArrayList<>();
        for(Player p : plugin.getServer().getOnlinePlayers()) {
            players.add(p.getDisplayName());
        }
        return players;
    }

    private boolean isInt(String str){
        return (str.lastIndexOf("-") == 0 && !str.equals("-0")) ? str.substring(1).matches(
                "\\d+") : str.matches("\\d+");
    }

    private int IntRandomNumberGenerator(int min, int max) {
        Random rn = new Random();
        return rn.nextInt(max - min + 1) + min;
    }

    private <T> List<List<T>> nPartition(List<T> objs, final int N) {
        return new ArrayList<>(IntStream.range(0, objs.size()).boxed().collect(
                Collectors.groupingBy(e->e%N,Collectors.mapping(objs::get, Collectors.toList())
                )).values());
    }

}
