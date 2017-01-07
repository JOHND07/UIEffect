package com.jb.Ui;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Atools extends ListActivity {

	//容器
	private ArrayAdapter<Item> adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       
       Item[] items={  
    		   new Item(TestButton.class,"按钮特效"),
    		   new Item(TestRotate3d.class,"3D滑动切屏特效"),
       };
       
		adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, items);
		setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
    	startActivity(adapter.getItem(position));
    }
    
    public class Item extends Intent
    {
    	String s;
    	public Item(Class<?> c,String s)
    	{
    		super(Atools.this,c);
    		this.s=s;
    	}
    	@Override
    	public String toString()
    	{
    		return s;
    	}
    }
}
