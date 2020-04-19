package net.lawaxi.mc.againstlords.utils;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class Card {

    private static ArrayList<String> Card = new ArrayList<>();
    public static ArrayList<ItemStack> cards;
    public static HashMap<Integer,Integer> allCards = new HashMap<>();
    private static ArrayList<Integer> CardStack = new ArrayList<>();

    public static void init(){

        Card.add("3");
        Card.add("4");
        Card.add("5");
        Card.add("6");
        Card.add("7");
        Card.add("8");
        Card.add("9");
        Card.add("10");
        Card.add("J");
        Card.add("Q");
        Card.add("K");
        Card.add("A");
        Card.add("2");
        Card.add("小王");
        Card.add("大王");

        for(int i=0;i<=12;i++)
        {
            allCards.put(i,4);
        }
        for(int i=13;i<=14;i++)
        {
            allCards.put(i,1);
        }

        for(int i=0;i<size();i++)
        {
            for(int j=0;j<allCards.get(i);j++)
            {
                CardStack.add(i);
            }
        }

    }


    public static String indexToCard(int index)
    {
        return Card.get(index);
    }

    public static int CardToIndex(String card)
    {
        if(!Card.contains(card))
            return -1;
        else
            return Card.indexOf(card);
    }

    public static int size(){
        return Card.size();
    }

    public static int getIndex(ItemStack stack){

        for(int i=0;i<size();i++)
        {
            if(cards.get(i).getType().equals(stack.getType()))
            {
                return i;
            }
        }
        return -1;

        /*
        stack.setAmount(1);

        if(!cards.contains(stack))
            return -1;
        else
            return cards.indexOf(stack);*/
    }

}
