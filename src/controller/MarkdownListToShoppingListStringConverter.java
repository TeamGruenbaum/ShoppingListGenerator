package controller;



import model.Ingredient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;



public class MarkdownListToShoppingListStringConverter implements Function<List<Ingredient>, String>
{
    public MarkdownListToShoppingListStringConverter() {}


    @Override
    public String apply(List<Ingredient> elements)
    {
        String shoppingList="";

        List<PairedValue<String, ArrayList<Ingredient>>> ingredientsAndStores=new ArrayList<>();
        for(String storeName:elements.stream().map(ingredient->ingredient.getStore()).collect(Collectors.toList()))
        {
            if(!(ingredientsAndStores.stream().map(pair->pair.getKey()).anyMatch(item->item.equals(storeName))))
            {
                ingredientsAndStores.add(new Pair<>(storeName, new ArrayList<>()));
            }
        }
        ingredientsAndStores.sort(Comparator.comparing(PairedValue::getKey));

        for (PairedValue<String, ArrayList<Ingredient>> storeIngredientsPair:ingredientsAndStores)
        {
            shoppingList=addItemsOfStore(shoppingList,elements,storeIngredientsPair);
        }

        return shoppingList;
    }


    private String addItemsOfStore(String shoppingList, List<Ingredient> ingredients, PairedValue<String, ArrayList<Ingredient>> storeIngredientsPair)
    {
        for(Ingredient ingredient:ingredients)
        {
            if(ingredient.getStore().equals(storeIngredientsPair.getKey()))
            {
                storeIngredientsPair.getValue().add(ingredient);
            }
        }
        storeIngredientsPair.getValue().sort(Comparator.comparingInt(Ingredient::getShelf));

        Localisator localisator=new Localisator();

        int currentShelfNumber=storeIngredientsPair.getValue().get(0).getShelf();
        shoppingList+="# "+storeIngredientsPair.getKey()+"\n## "+localisator.getString("shelf")+" "+currentShelfNumber;

        for(int index=0; index<storeIngredientsPair.getValue().size(); index++)
        {
            if(storeIngredientsPair.getValue().get(index).getShelf()==currentShelfNumber)
            {
                shoppingList+="\n- [ ] "+storeIngredientsPair.getValue().get(index).getName();
            }
            else
            {
                currentShelfNumber=storeIngredientsPair.getValue().get(index).getShelf();
                shoppingList+="\n\n## "+localisator.getString("shelf")+" "+currentShelfNumber;
                index--;
            }
        }

        return shoppingList+"\n\n";
    }
}
