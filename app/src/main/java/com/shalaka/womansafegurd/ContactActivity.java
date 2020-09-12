package com.shalaka.womansafegurd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.shalaka.womansafegurd.Database.DatabaseHelper;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    Button b1;
    private static final int REQUEST_CODE = 1;
    DatabaseHelper mydb;
    Toolbar toolbar;
    ArrayList<String> list1, list2,list3;
    ListView listView;
    String n, p, i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        b1=findViewById(R.id.add);
        listView=findViewById(R.id.listview);
        listView.setDivider(null);
        toolbar=findViewById(R.id.tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mydb=new DatabaseHelper(getApplicationContext());
        list1=new ArrayList<String>();
        list2=new ArrayList<String>();
        list3=new ArrayList<String>();

        Cursor res = mydb.getData();
        while (res.moveToNext()) {
            i = res.getString(0);
            n = res.getString(1);
            p = res.getString(2);
            list1.add(i);
            list2.add(n);
            list3.add(p);

        }
        final MyAdapter myAdapter = new MyAdapter(getApplicationContext(), list2, list3);
        listView.setAdapter(myAdapter);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu p1 = new PopupMenu(getApplicationContext() , listView);
                p1.getMenuInflater().inflate(R.menu.popup_menu , p1.getMenu());

                p1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.delete:
                                String ID=list1.get(position);
                                Integer r=mydb.delete(ID);
                                if (r>0)
                                {
                                        Cursor res = mydb.getData();
                                        list1.clear();
                                        list2.clear();
                                        list3.clear();

                                        while (res.moveToNext())
                                        {
                                            i = res.getString(0);
                                            n = res.getString(1);
                                            p = res.getString(2);
                                            list1.add(i);
                                            list2.add(n);
                                            list3.add(p);

                                        }

                                        MyAdapter myAdapter1 = new MyAdapter(getApplicationContext(), list2, list3);
                                        listView.setAdapter(myAdapter1);
                                    Toast.makeText(ContactActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    }else
                                {
                                    Toast.makeText(getApplicationContext(),"Not Deleted",Toast.LENGTH_SHORT).show();
                                }
                                break;

                        }

                        return true;
                    }
                });
                p1.show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);

                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(nameColumnIndex);
             //   Toast.makeText(getApplicationContext(), "Name:"+name+"\nNumber:"+number, Toast.LENGTH_SHORT).show();
                boolean isInserted=mydb.insertData(name,number);
                if (isInserted==true)
                    Toast.makeText(getApplicationContext(),"Inserted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"Not Inserted",Toast.LENGTH_SHORT).show();
                Cursor res = mydb.getData();
                list1.clear();
                list2.clear();
                list3.clear();

                while (res.moveToNext()) {
                    i=res.getString(0);
                    n = res.getString(1);
                    p = res.getString(2);

                    list1.add(i);
                    list2.add(n);
                    list3.add(p);


                }
                if (name.equals(n) && number.equals(p))
                {
                    MyAdapter myAdapter=new MyAdapter(getApplicationContext(),list2,list3);
                    listView.setAdapter(myAdapter);
                }
            }
        }
    }
    class MyAdapter extends ArrayAdapter<String>
    {
        Context context;

        ArrayList<String> l2;
        ArrayList<String> l3;
        MyAdapter(Context c, ArrayList<String> l2, ArrayList<String> l3)
        {
            super(c,R.layout.list_design,R.id.name,l2);
            this.context=c;

            this.l2=l2;
            this.l3=l3;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row=layoutInflater.inflate(R.layout.list_design,parent,false);

            TextView n1=row.findViewById(R.id.name);
            TextView p1=row.findViewById(R.id.phone);

            n1.setText(l2.get(position));
            p1.setText(l3.get(position));
            return row;
        }
    }
}