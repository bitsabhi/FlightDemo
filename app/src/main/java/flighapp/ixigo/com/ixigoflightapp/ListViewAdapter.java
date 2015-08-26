package flighapp.ixigo.com.ixigoflightapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;

    HashMap<String, String> resultp = new HashMap<String, String>();

    public ListViewAdapter(Context context,
                           ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;

    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    static class ViewHolderItem{

        TextView flightName;
        TextView depTime;
        TextView arrTime;


        TextView price;
        TextView duration;
        TextView fromStn;

        TextView toStn;
        TextView flightClass;
    }

    // Using holder pattern to make scroll smooth.
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolderItem viewHolder;

        if (convertView == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
            viewHolder = new ViewHolderItem();
            viewHolder.flightName = (TextView) convertView.findViewById(R.id.flightName);
            viewHolder.depTime = (TextView) convertView.findViewById(R.id.depTime);
            viewHolder.arrTime = (TextView) convertView.findViewById(R.id.arrTime);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.duration = (TextView) convertView.findViewById(R.id.duration);
            viewHolder.fromStn = (TextView) convertView.findViewById(R.id.fromStn);
            viewHolder.toStn = (TextView) convertView.findViewById(R.id.toStn);
            viewHolder.flightClass = (TextView) convertView.findViewById(R.id.flightclass);


            convertView.setTag(viewHolder);

        }

        else {
            viewHolder = (ViewHolderItem) convertView.getTag();

        }


        // Get the position
        resultp = data.get(position);
        viewHolder.flightName.setText(resultp.get(MainActivity.FLIGHTNAME));
        viewHolder.depTime.setText(getTimeFromTimeStamp(Long.valueOf(resultp.get(MainActivity.DEPTIME))));
        viewHolder.arrTime.setText(getTimeFromTimeStamp(Long.valueOf(resultp.get(MainActivity.ARRIVALTIME))));;
        viewHolder.price.setText("\u20B9" + resultp.get(MainActivity.PRICE));
        viewHolder.duration.setText(resultp.get(MainActivity.DURATION));
        viewHolder.fromStn.setText(resultp.get(MainActivity.FROMSTATION));
        viewHolder.toStn.setText(resultp.get(MainActivity.TOSTATION));
        viewHolder.flightClass.setText(resultp.get(MainActivity.FLIGHTCLASS));

        return convertView;
    }

    private String getTimeFromTimeStamp(long value) {
        Timestamp timestamp = new Timestamp(value);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");

        return simpleDateFormat.format(timestamp);
    }
}
