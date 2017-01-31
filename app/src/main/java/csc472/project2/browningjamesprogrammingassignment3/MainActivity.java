package csc472.project2.browningjamesprogrammingassignment3;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    static final String[] MENU = new String[]{
            "Maultaschen", "Kaesespaetzle", "Rindfleisch", "Apfelsaftschorle", "Doener",
            "Koenigsberger Klopse", "Mineralwasser", "Lasagne", "Paella"
    };

    private HashMap<Integer, HashMap<String, Double>> summaryOfOrders = new HashMap<>();
    private HashMap<String, Double> menu = new HashMap<String, Double>();
    private HashMap<String, Double> order;
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private double totalPrice;
    private int count = 0;
    private int itemNum;
    private StringBuffer sb = new StringBuffer("");
    private boolean isClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.menu = fillMenu();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, MENU);
        final AutoCompleteTextView tv1 = (AutoCompleteTextView) findViewById(R.id.itemEditText);
        tv1.setAdapter(adapter);

        final Button newOrder = (Button) findViewById(R.id.newOrderButton);
        final Button newItem = (Button) findViewById(R.id.newItemButton);
        final Button totalButton = (Button) findViewById(R.id.totalButton);

        final EditText item = (EditText) findViewById(R.id.itemEditText);
        final EditText FOCUSABLE_VIEW = item;
        final EditText unitPrice = (EditText) findViewById(R.id.unitPriceField);
        final EditText quantity = (EditText) findViewById(R.id.quantityField);
        final TextView list = (TextView) findViewById(R.id.printList);

        tv1.setVisibility(View.INVISIBLE);
        quantity.setVisibility(View.INVISIBLE);
        unitPrice.setVisibility(View.INVISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                FOCUSABLE_VIEW.getWindowToken(), 0);

        final TextView totalTextView = (TextView) findViewById(R.id.totalTextView);

        newOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemNum = 0;
                if (order == null) {
                    order = new HashMap<String, Double>();
                }

                String message = "";
                totalTextView.setText(formatter.format(totalPrice));

                count++;
                summaryOfOrders.put(count, order);

                if (summaryOfOrders.get(count) != null) {
/*                    if (totalPrice > 0.00){
                        sb.append("Total : " + totalPrice + "\n");
                    }*/
                    message = "Order # " + count + "\n";
                    sb.append(message);
                }

                totalPrice = 0.0;

                resetValues(unitPrice, totalTextView, quantity, tv1);

                tv1.setVisibility(View.VISIBLE);
                quantity.setVisibility(View.VISIBLE);
                unitPrice.setVisibility(View.VISIBLE);


            }
        });

        newItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!isClicked) {
                    Toast.makeText(MainActivity.this, "Click total before entering a new item.",
                            Toast.LENGTH_SHORT).show();

                    if (!item.getText().toString().equals("")) {

                        HashMap<String, Double> order = summaryOfOrders.get(count);
                        // System.out.println(order.keySet() + "keyset");

                        String itemString = item.getText().toString();

                        double cost = order.get(item.getText().toString());
                        // System.out.println("Cost is : " + cost);

                        if (!menu.containsKey(item.getText().toString())) {
                            menu.put(itemString, cost);
                            System.out.println(menu.keySet());
                        }

                        order.put(itemString, cost);
                        summaryOfOrders.put(count, order);
                        tv1.requestFocus();
                        isClicked = false;
                    }
                }
                tv1.setVisibility(View.VISIBLE);
                quantity.setVisibility(View.VISIBLE);
                unitPrice.setVisibility(View.VISIBLE);

                tv1.requestFocus();
                tv1.setText("");
                //  System.out.println(order.keySet());
                System.out.println(order.values());
                //   resetValues(unitPrice, totalTextView, quantity, tv1);

                // Toast.makeText(MainActivity.this, "New item", Toast.LENGTH_SHORT).show();
            }
        });

        tv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (!item.getText().toString().equals("") && menu.containsKey(item.getText().toString())) {
                    double cost = menu.get(item.getText().toString());
                    unitPrice.setText(cost + "");
/*                    for (String string : order.keySet()) {
                        System.out.println("String is : " + string);
                    }*/
                }
            }
        });

        totalButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                isClicked = true;

                if (summaryOfOrders.get(count) != null &&
                        !item.getText().toString().equals("")
                        && item.getText() != null) {
                    String message = "";
                    getTotal(item, totalTextView, tv1);
                    // message = "\nItem # " + ++itemNum;
                    message = "Item : " + ++itemNum + ", " + order + "\n";



                    sb.append(message + "");

                    summaryOfOrders.put(count, order);

                    list.setText(sb);
                    list.append(String.format("Total : %.2f", totalPrice));

                    //System.out.println("Count is : " + count);
                    //System.out.println("SB is : " + sb);

                    tv1.setVisibility(View.INVISIBLE);
                    quantity.setVisibility(View.INVISIBLE);
                    unitPrice.setVisibility(View.INVISIBLE);
                }

            }
        });

    }


    private void resetValues(EditText unitPrice, TextView totalTextView,
                             EditText quantity, AutoCompleteTextView tv1) {
        totalTextView.setText(formatter.format(totalPrice));
        if ((null != tv1) && !tv1.getText().toString().equals("")) {
            tv1.setText("");
        }
        unitPrice.setText(String.format("%.2f",
                0.0));
        quantity.setText("1");
    }

    private void getTotal(EditText item, TextView totalTextView, AutoCompleteTextView tv1) {

        order = new HashMap<String, Double>();

        EditText unitPrice = (EditText) findViewById(R.id.unitPriceField);
        EditText quantity = (EditText) findViewById(R.id.quantityField);
        String unitPriceString = unitPrice.getText().toString();

        if (unitPrice != null) {
            if (unitPriceString.contains("$")) {
                unitPriceString = unitPriceString.substring(1, unitPriceString.length());
            }
        }
        double unitPriceDouble = Double.parseDouble(unitPriceString);
        int quantityInt = Integer.parseInt(quantity.getText().toString());

        if (!item.getText().toString().equals("") || item.getText() == null) {
            order.put(item.getText().toString(), (quantityInt * unitPriceDouble));
            summaryOfOrders.put(count, order);
            item.setText("");
        }
        totalPrice += (unitPriceDouble * quantityInt);

        totalTextView.setText(formatter.format(totalPrice));
        AutoCompleteTextView dud = null;
        resetValues(unitPrice, totalTextView, quantity, dud);
        Toast.makeText(MainActivity.this, "Item added to order.", Toast.LENGTH_SHORT).show();
        // itemSaved = true;
    }

    private HashMap<String, Double> fillMenu() {

        HashMap<String, Double> map = new HashMap<>();

        map.put(MENU[0], 5.0);
        map.put(MENU[1], 5.5);
        map.put(MENU[2], 12.3);
        map.put(MENU[3], 1.3);
        map.put(MENU[4], 4.0);
        map.put(MENU[5], 15.0);
        map.put(MENU[6], 1.2);

        return map;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
