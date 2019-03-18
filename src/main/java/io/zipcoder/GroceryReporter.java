package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;

import java.util.*;

public class GroceryReporter {
    private final String originalFileText;

    public GroceryReporter(String jerksonFileName) {

        this.originalFileText = FileReader.readFile(jerksonFileName);
    }

    @Override
    public String toString() {


        ItemParser itemsFromFile= new ItemParser();
        List<Item> shoppingList;
        HashMap<String, Integer> output = new LinkedHashMap();
        HashMap<String, Integer> amount = new LinkedHashMap();
        StringBuilder retVal = new StringBuilder();

        shoppingList = itemsFromFile.parseItemList(originalFileText);

        for(Item ele: shoppingList ){
            output.put(ele.getName(), countObjectsName(shoppingList, ele.getName()));
            amount.put(ele.getName()+ " | "  +ele.getPrice(), countObjects(shoppingList, ele, ele.getPrice()));
        }

        Iterator outer = output.entrySet().iterator();

        String innerkeyPreSplit;


        while (outer.hasNext()){
            Map.Entry outerPair = (Map.Entry)outer.next();
            String nameString = String.format("name:%8s", outerPair.getKey()  );
            String valueString = String.format("seen:%2s times", outerPair.getValue());
            retVal.append(nameString+"        "+valueString+ "\n");
            retVal.append(doubleSeperator()+ "        " + doubleSeperator()+ "\n");
            Iterator innner = amount.entrySet().iterator();
            String handleLastLine = "";

            while(innner.hasNext()){
                Map.Entry innerPair = (Map.Entry)innner.next();
                innerkeyPreSplit = innerPair.getKey().toString();
                String[] innerkeyPostSplit = innerkeyPreSplit.split(" | ");
                retVal.append(handleLastLine);

                if(innerkeyPostSplit[0].equals(outerPair.getKey())){
                    String priceString = String.format("Price:%7s", innerkeyPostSplit[2]  );
                    String valueString2 = String.format("seen:%2s times", innerPair.getValue());
                    retVal.append(priceString+"        "+valueString2+ "\n");
                    handleLastLine  = singleSeperator()+ "        " + singleSeperator()+ "\n";
                }
                else {
                    handleLastLine = "";
                }
            }
            retVal.append( "\n");

        }

        String errorInfo = getErrorCountInString(itemsFromFile);
        retVal.append(errorInfo+ "\n");

        return retVal.toString();
    }

    private String getErrorCountInString(ItemParser itemsFromFile) {
        String retVal = null;
        if(itemsFromFile.errorCounter > 0)
        {
            retVal = String.format("Errors         	 	 seen: %d times", itemsFromFile.errorCounter);
        }

        return retVal;
    }

    public Integer countObjectsName(List<Item> array, String items){
        Integer count = 0;

        for(Item i: array){
            if(i.getName().equals(items) )
            {
                count++;
            }
        }

        return count;

    }



    public Integer countObjects(List<Item> array, Item item, Double price){
        Integer count = 0;

        for(Item i: array){
            if(i.getName().equals(item.getName()) && i.getPrice().equals(price) )
            {
                count++;
            }
        }

        return count;

    }

    private String upperFirst(String string){
        return string.toUpperCase().charAt(0) + string.substring(1) ;
    }

    private String doubleSeperator(){
        return  "=============";
    }

    private String singleSeperator(){
        return  "-------------";
    }
}
