package com.example.beproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {
    Button logout_btn, checkout_btn;
   private TextView itemTotal, priceTotal;
    private ArrayList<Item> items;
   public static ArrayList<Item> database;
    private ItemUI itemUI;

    final private static int BARCODE_REQUEST_CODE = 9232;
    public static int itemCount = 0;
    public static double price = 0.0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout_btn=findViewById(R.id.logout_btn);
        checkout_btn = findViewById(R.id.checkout_btn);




        checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checkout checkout = new Checkout();

                checkout.setKeyID("rzp_test_gcS4KnL7XUghWs");

                checkout.setImage(R.drawable.razor);

                JSONObject object = new JSONObject();

                try {
                    object.put("name","Divya S");
                    object.put("description","Test Payment");
                    object.put("theme.color","#ffe600");
                    object.put("currency","INR");
                    object.put("amount", price*100);
                    object.put("prefill.contact","8850485608");
                    object.put("prefill.email","dms28052000@gmail.com");

                    checkout.open(MainActivity.this, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(MainActivity.this, AuthenticationActivity.class));
                Toast.makeText(MainActivity.this, "You are logged out", Toast.LENGTH_SHORT).show();
                finish();
            }
        });




        ListView cart = findViewById(R.id.ui_main_cart);
        Button scan =  findViewById(R.id.ui_main_scan);
       itemTotal = findViewById(R.id.MainActivity_TextView_ItemTotal);
       priceTotal = findViewById(R.id.MainActivity_TextView_TotalPrice);

        itemTotal.setText("0 items");
        priceTotal.setText("Rs.00");

        items = new ArrayList<>();
        database = new ArrayList<>();
        database.add(new Item("8902442207103", "Youva 5 sub bk", 240,getDrawable(R.drawable.youva)));
        database.add(new Item("8901499010216", "Klgs org crfks", 435, getDrawable(R.drawable.kelloggs)));
        database.add(new Item("8901725109271", "B natural juice", 150, getDrawable(R.drawable.download)));
        database.add(new Item("8902635510232", "Kores white ink", 25, getDrawable(R.drawable.kores)));
        database.add(new Item("8904117400475", "Tulips cotton", 70, getDrawable(R.drawable.tulips)));








        itemUI =  new ItemUI(this, R.layout.activity_item, items);
        cart.setAdapter(itemUI);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
                startActivityForResult(intent,BARCODE_REQUEST_CODE);
            }
        });



    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==BARCODE_REQUEST_CODE){
            if(resultCode== CommonStatusCodes.SUCCESS){
                if(data!=null){
                    final Barcode barcode = data.getParcelableExtra("barcode");
                    for(Item item : database){
                        if(item.getIndicator().equals(barcode.displayValue)){
                            items.add(item);
                            itemUI.notifyDataSetChanged();
                            itemCount = items.size();
                            itemTotal.setText(itemCount+" items");
                            price += item.getPrice();
                            priceTotal.setText("Rs."+price);








                        }






                    }
                }
                else{
                    Toast.makeText(this, "No Barcode Found", Toast.LENGTH_SHORT).show();
                }
            }



        }else {

            super.onActivityResult(requestCode, resultCode, data);
        }






        }


    @Override
    public void onPaymentSuccess(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment Successful");
        builder.setMessage("Payment ID"+s);

        builder.show();

    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(MainActivity.this, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();


    }
}


