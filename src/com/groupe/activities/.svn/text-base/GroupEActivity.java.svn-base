package com.groupe.activities;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.groupe.R;
import com.groupe.config.FileAccess;
import com.groupe.mail.InviteListener;
import com.groupe.persistence.DBAccess;
import com.groupe.types.Group;
import com.groupe.types.Person;

/**
 * @author ontl
 */

public class GroupEActivity extends ListActivity {

	private static final int MENU1 = Menu.FIRST;
	private static final int MENU2 = Menu.FIRST + 1;
	private static final int MENU3 = Menu.FIRST + 2;	
	private static final int MENU4 = Menu.FIRST + 3;
	private static final int MENU5 = Menu.FIRST + 4;
	private static final int MENU6 = Menu.FIRST + 5;

	public ArrayList<Group> groups;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
	}
	@Override
	public void onResume(){
		super.onResume();
//		onCreate(null);
		File file = getFileStreamPath("groupeConfigUser");
		if(!file.exists()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("No usersettings found! Go to preferences page?")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					//			                GroupEActivity.this.finish();
					startActivity(new Intent(GroupEActivity.this, ConfigMain.class));
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			}).show();
		}

		final String[] columns = { "Zeile1", "Zeile2" };
		final String[] matrix  = { "_id", "Zeile1", "Zeile2" };
		final int[] layouts = new int[] { android.R.id.text1, android.R.id.text2 };
		int key = 0;

		MatrixCursor cursor = new MatrixCursor(matrix);

		final ArrayList<Group> groups = DBAccess.getAllGroups(this);
		final ArrayList<Group> myGroups = new ArrayList<Group>();
		final Person user = FileAccess.getUserFromFile(this);
		
		
		if(groups.size() != 0) {
			for(int n = 0;n < groups.size();n++){
				if(user.checkPersonInGroup(user, groups.get(n), this)) {
					myGroups.add(groups.get(n));
					cursor.addRow(new Object[] { key++, groups.get(n).getGrpName(), 
							groups.get(n).getInformation()});
				}
			}
		}
		
		SimpleCursorAdapter data = new SimpleCursorAdapter(this, 
				android.R.layout.simple_list_item_2, 
				cursor, columns, layouts);

		setListAdapter(data);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(GroupEActivity.this, GroupView.class);
				intent.putExtra("com.groupe.types.Group", myGroups.get((int)id));
								
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){

		menu.add(0, MENU1, 0 , "Create Group").setIcon(R.drawable.ic_menu_add);
		menu.add(0, MENU2, 0 , "Preferences").setIcon(R.drawable.ic_menu_preferences);
		menu.add(0, MENU3, 0 , "Check Messages").setIcon(R.drawable.ic_menu_refresh);
		menu.add(0 ,MENU4, 0 , "Close").setIcon(R.drawable.ic_menu_close_clear_cancel);
		menu.add(0 ,MENU5, 0 , "Lecture Mode").setIcon(R.drawable.ic_menu_mapmode);
		menu.add(0 ,MENU6, 0 , "Build Group NFC").setIcon(R.drawable.ic_menu_cc);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU1:
			startActivity(new Intent(GroupEActivity.this, CreateGroup.class));
			return true;
		case MENU2:
			startActivity(new Intent(GroupEActivity.this, ConfigMain.class));
			return true;			
		case MENU3:
			File userConfig = getFileStreamPath("groupeConfigUser");
			File mailConfig = getFileStreamPath("groupeConfigEmail");
			if(!userConfig.exists() || !mailConfig.exists()) {
				Toast.makeText(this, "No configuration found! Please go to preferences.", Toast.LENGTH_LONG).show();				
			}else{	    
				startService(new Intent(this,InviteListener.class));
			}
			return true;
		case MENU4:
			finish();
			return true;
		case MENU5:
			finish();
			startActivity(new Intent(GroupEActivity.this, Courses.class));
			return true;
		case MENU6:
			startActivity(new Intent(GroupEActivity.this, BuildGroupNfc.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}