package com.ninja.contactbackup.app;
import java.util.ArrayList;
import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FullscreenActivity extends ListActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri contactsUri=ContactsContract.Contacts.CONTENT_URI;
        String[] proj1=new String[]{ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.LOOKUP_KEY};
        Cursor curContacts=getContentResolver().query(contactsUri,proj1, null, null, null);

        //declare a ArrayList object to store the data that will present to the user
        ArrayList<String> contactsList=new ArrayList<String>();
        String allPhoneNo="";
        if(curContacts.getCount()>0){
            while(curContacts.moveToNext()){
                // get all the phone numbers if exist
                if(curContacts.getInt(1)>0){
                    allPhoneNo=getAllPhoneNumbers(curContacts.getString(2));
                }
                contactsList.add(curContacts.getString(0)+" , "+allPhoneNo);
                allPhoneNo="";
            }
        }

        // binding the data to ListView 
        setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, contactsList));
        ListView lv=getListView();
        lv.setTextFilterEnabled(true);

    }

    /**
     * Get all the phone numbers of a specific contact person
     *
     * @param lookUp_Key lookUp key for a specific contact
     * @return a string containing all the phone numbers
     */
    public String getAllPhoneNumbers(String lookUp_Key){
        String allPhoneNo="";

        // Phone info are stored in the ContactsContract.Data table 
        Uri phoneUri=ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] proj2={ContactsContract.CommonDataKinds.Phone.NUMBER};
        // using lookUp key to search the phone numbers
        String selection=ContactsContract.Data.LOOKUP_KEY+"=?";
        String[] selectionArgs={lookUp_Key};
        Cursor cur=getContentResolver().query(phoneUri,proj2,selection, selectionArgs, null);
        while(cur.moveToNext()){
            allPhoneNo+=cur.getString(0)+" ";
        }

        return allPhoneNo;

    }
}