package flighapp.ixigo.com.ixigoflightapp;

import java.util.Comparator;
import java.util.Map;

class MapComparator implements Comparator<Map<String, String>>
{
    private final String key;

    public MapComparator(String key)
    {
        this.key = key;
    }

    public int compare(Map<String, String> first,
                       Map<String, String> second)
    {

        long firstValue = Long.valueOf(first.get(key));
        long secondValue = Long.valueOf(second.get(key));
        return (int)(firstValue - secondValue);
    }
}


