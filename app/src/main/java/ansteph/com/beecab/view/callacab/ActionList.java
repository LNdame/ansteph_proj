package ansteph.com.beecab.view.callacab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ansteph.com.beecab.R;
import ansteph.com.beecab.view.extraaction.Contact;
import ansteph.com.beecab.view.extraaction.Credits;
import ansteph.com.beecab.view.profile.EditProfile;
import ansteph.com.beecab.view.referral.ReferralHistory;

public class ActionList extends AppCompatActivity {

    ListView mActionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_list);

        mActionList = (ListView) findViewById(R.id.listView);

        ArrayList<String> list = new ArrayList<>();

        list.add("Profile");
        list.add("Referral Program");
        list.add("Credits");
        list.add("Contact Us");
        list.add("Test Zone");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        mActionList.setAdapter(adapter);


        mActionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position)
                {
                    case 0 :
                        Intent i = new Intent(getApplicationContext(), EditProfile.class);
                        startActivity(i); break;
                    case 1 : startActivity(new Intent(getApplicationContext(), ReferralHistory.class));
                         break;
                    case 2:startActivity(new Intent(getApplicationContext(), Credits.class));
                        break;
                    case 3 :startActivity(new Intent(getApplicationContext(), Contact.class));
                        break;
                    case 4 :Intent j = new Intent(getApplicationContext(), EditProfile.class);
                        startActivity(j); break;
                    default:
                }

            }
        });
    }
}
