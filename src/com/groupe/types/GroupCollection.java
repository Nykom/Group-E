package com.groupe.types;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author ontl
 */
public class GroupCollection implements Parcelable {

	private static ArrayList<Group> groups;

	public void addGroup(Group group) {
		groups.add(group);
	}
	
	
	public GroupCollection() {
		
	}
	
	public GroupCollection(ArrayList<Group> groups) {
		 GroupCollection.groups = groups;
	}
	
	public ArrayList<Group> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<Group> groups) {
		GroupCollection.groups = groups;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
	}

}
