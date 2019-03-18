package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;
import sun.jvm.hotspot.jdi.DoubleValueImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {

    Integer errorCounter = 0;


    public List<Item> parseItemList(String valueToParse) {

        String[] shoppingList = valueToParse.split("##");
        List<Item> retVal = new ArrayList<>();
        for(String ele: shoppingList){
            try {
                retVal.add(parseSingleItem(ele));
            }
            catch (ItemParseException e){
                errorCounter++;
                System.err.println("Error not valid");
            }

        }

        return retVal;
    }

    public Item parseSingleItem(String singleItem) throws ItemParseException {
        String name = null;
        String price = null;
        String type = null;
        String expire = null;
        ArrayList<String> parseItems;
        Item item = null;
        singleItem = singleItem.replaceAll("##","");

        String seperators = "(:|@|\\^|\\*|%)";
        String namePattern = "(N|n)(A|a)(M|m)(E|e)"+seperators;
        String pricePattern = "(P|p)(R|r)(I|i)(C|c)(E|e)"+seperators;
        String typePattern = "(T|t)(Y|y)(P|p)(E|e)"+seperators;
        String expirePattern = "(E|e)(X|x)(P|p)(I|i)(R|r)(A|a)(T|t)(I|i)(O|o)(N|n)"+seperators;

        parseItems = parseString(singleItem);
        name = getValue(parseItems, namePattern);
        price = getValue(parseItems, pricePattern);
        type = getValue(parseItems, typePattern);
        expire = getValue(parseItems, expirePattern);

        if(name == null|| price == null|| type == null|| expire== null ||
                name.equals("")|| price.equals("")|| type.equals("")|| expire.equals("")) {
            throw new ItemParseException();
        }
        else{
            Double priceDouble = Double.parseDouble(price);
            item = new Item(name, priceDouble, type, expire);
        }

        return item;
    }

    private String getValue(ArrayList<String> parseItems, String pattern)  {
        String retVal= null;
        Pattern p  = Pattern.compile(pattern);

        for(int i=0; i< parseItems.size(); i++) {
            if(p.matcher(parseItems.get(i)).find()) {
                retVal =  p.matcher(parseItems.get(i)).replaceAll("");
            }
        }

        if(retVal != null)
        {
          retVal = retVal.toLowerCase();
        }

        return retVal;
    }

    public ArrayList<String> parseString(String fullline){

        fullline = fullline+";";
        Pattern semiColin = Pattern.compile(";");
        Matcher matcher = semiColin.matcher(fullline);

        Integer lastIndex = 0;
        ArrayList<String> subElement = new ArrayList<>();

        for(int i = 0; matcher.find(); i++) {
            subElement.add(fullline.substring(lastIndex, (matcher.end()-1)));
            lastIndex = matcher.end();

        }

        return subElement;

    }


}
