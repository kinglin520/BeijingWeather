package com.superscene.expandablelistdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.superscene.expandablelistdemo.model.ChildItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
	private ExpandableListView expandList;
	private List<String> groupData;//group������Դ
	private Map<Integer, List<ChildItem>> childData;//child������Դ
	private MyBaseExpandableListAdapter myAdapter;
	
	final int CONTEXT_MENU_GROUP_DELETE = 0;//��������Ĳ˵�ʱÿһ���˵����item ID
	final int CONTEXT_MENU_GROUP_RENAME = 1;
	final int CONTEXT_MENU_CHILD_EDIT = 2;
	final int CONTEXT_MENU_CHILD_DELETE = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initDatas();
		initView();
		initEvents();
	}
	/**
	 * group��child���������Դ
	 */
	private void initDatas() {
		
		groupData = new ArrayList<String>();
		groupData.add("��ɫˮ��");
		groupData.add("��ɫˮ��");
		groupData.add("����ˮ��");
		
		List<ChildItem> childItems = new ArrayList<ChildItem>();
		ChildItem childData1 = new ChildItem("ƻ��", R.drawable.apple_pic);
		childItems.add(childData1);
		ChildItem childData2 = new ChildItem("ӣ��", R.drawable.cherry_pic);
		childItems.add(childData2);
		ChildItem childData3 = new ChildItem("��ݮ", R.drawable.strawberry_pic);
		childItems.add(childData3);
		
		List<ChildItem> childItems2 = new ArrayList<ChildItem>();
		ChildItem childData4 = new ChildItem("�㽶", R.drawable.banana_pic);
		childItems2.add(childData4);
		ChildItem childData5 = new ChildItem("â��", R.drawable.mango_pic);
		childItems2.add(childData5);
		ChildItem childData6 = new ChildItem("����", R.drawable.orange_pic);
		childItems2.add(childData6);
		ChildItem childData7 = new ChildItem("����", R.drawable.pear_pic);
		childItems2.add(childData7);
		
		List<ChildItem> childItems3 = new ArrayList<ChildItem>();
		ChildItem childData8 = new ChildItem("����", R.drawable.grape_pic);
		childItems3.add(childData8);
		ChildItem childData9 = new ChildItem("����", R.drawable.watermelon_pic);
		childItems3.add(childData9);
		
		childData = new HashMap<Integer, List<ChildItem>>();
		childData.put(0, childItems);
		childData.put(1, childItems2);
		childData.put(2, childItems3);
		
		myAdapter = new MyBaseExpandableListAdapter(this, groupData, childData);
	}

	private void initView() {
		expandList = (ExpandableListView) findViewById(R.id.expandlist);
		//��drawable�ļ������½���indicator.xml������������Ҳ����ʵ��group��չ����ʱ��indicator�仯
		//expandList.setGroupIndicator(this.getResources().getDrawable(R.drawable.indicator));
		expandList.setGroupIndicator(null);
		expandList.setAdapter(myAdapter);
		registerForContextMenu(expandList);//��ExpandListView��������Ĳ˵�	
	}
	private void initEvents() {
		//child����ĵ����¼�
		expandList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Toast.makeText(MainActivity.this, "�㵥���ˣ�"  
                        +myAdapter.getChild(groupPosition, childPosition), Toast.LENGTH_SHORT).show();  
				return true;
			}
		});	
				
	}
	/* 
	 * ��������Ĳ˵�
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo)menuInfo;
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			menu.setHeaderTitle("Options");
			menu.add(0, CONTEXT_MENU_GROUP_DELETE, 0, "ɾ��");
			menu.add(0, CONTEXT_MENU_GROUP_RENAME, 0, "������");
		}
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			menu.setHeaderTitle("Options");
			menu.add(1, CONTEXT_MENU_CHILD_EDIT, 0, "�༭");
			menu.add(1, CONTEXT_MENU_CHILD_DELETE, 0, "ɾ��");
		}
		
	}
	/* 
	 * ÿ���˵���ľ������¼�
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
	 ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo)item.getMenuInfo();
	 switch (item.getItemId()) {
		case CONTEXT_MENU_GROUP_DELETE:
			Toast.makeText(this, "����group��ɾ��", Toast.LENGTH_SHORT).show();
			break;
		case CONTEXT_MENU_GROUP_RENAME:
			Toast.makeText(this, "����group��������", Toast.LENGTH_SHORT).show();
			break;
		case CONTEXT_MENU_CHILD_EDIT:
			Toast.makeText(this, "����child�ı༭", Toast.LENGTH_SHORT).show();
			break;
		case CONTEXT_MENU_CHILD_DELETE:
			Toast.makeText(this, "����child��ɾ��", Toast.LENGTH_SHORT).show();
			break;
	
		default:
			break;
		}   
	
	    return super.onContextItemSelected(item);
	}
}
