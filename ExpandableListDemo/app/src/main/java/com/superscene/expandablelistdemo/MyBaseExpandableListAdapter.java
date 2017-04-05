package com.superscene.expandablelistdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.superscene.expandablelistdemo.model.ChildItem;

import java.util.List;
import java.util.Map;

/**
 * ExpandListView�����������̳���BaseExpandableListAdapter
 *
 */
public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter implements OnClickListener {
	
	private Context mContext;
	private List<String> groupTitle;
	//������һ��map��key��group��id��ÿһ��group��Ӧһ��ChildItem��list
	private Map<Integer, List<ChildItem>> childMap;
	private Button groupButton;
		
	public MyBaseExpandableListAdapter(Context context, List<String> groupTitle, Map<Integer, List<ChildItem>> childMap) {
		this.mContext = context;
		this.groupTitle = groupTitle;
		this.childMap = childMap;
	}
	/*
	 *  Gets the data associated with the given child within the given group
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		//�������ﷵ��һ��ÿ��item�����ƣ��Ա㵥��itemʱ��ʾ
		return childMap.get(groupPosition).get(childPosition).getTitle();
	}
	/*  
	 * ȡ�ø��������и�������ͼ��ID. ����ID������������Ψһ��.���벻ͬ����������ID�����鼰����Ŀ��ID��
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {		
		return childPosition;
	}
	/* 
	 *  Gets a View that displays the data for the given child within the given group
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, 
			ViewGroup parent) {
		ChildHolder childHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.childitem, null);
			childHolder = new ChildHolder();
			childHolder.childImg = (ImageView) convertView.findViewById(R.id.img_child);
			childHolder.childText = (TextView) convertView.findViewById(R.id.tv_child_text);
			convertView.setTag(childHolder);
		}else {
			childHolder = (ChildHolder) convertView.getTag();
		}
		childHolder.childImg.setBackgroundResource(childMap.get(groupPosition).get(childPosition).getMarkerImgId());
		childHolder.childText.setText(childMap.get(groupPosition).get(childPosition).getTitle());
		
		return convertView;
	}

	/* 
	 * ȡ��ָ���������Ԫ����
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childMap.get(groupPosition).size();
	}

	/**
	 * ȡ��������������������
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return groupTitle.get(groupPosition);
	}
		
	/**
	 * ȡ�÷�����
	 */
	@Override
	public int getGroupCount() {
		return groupTitle.size();
	}

	/**
	 * ȡ��ָ�������ID.����ID������������Ψһ��.���벻ͬ����������ID�����鼰����Ŀ��ID��
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	/* 
	 *Gets a View that displays the given group
	 *return: the View corresponding to the group at the specified position 
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupHolder groupHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.groupitem, null);
			groupHolder = new GroupHolder();
			groupHolder.groupImg = (ImageView) convertView.findViewById(R.id.img_indicator);
			groupHolder.groupText = (TextView) convertView.findViewById(R.id.tv_group_text);
			convertView.setTag(groupHolder);
		}else {
			groupHolder = (GroupHolder) convertView.getTag();
		}
		if (isExpanded) {
			groupHolder.groupImg.setBackgroundResource(R.drawable.downarrow);
		}else {
			groupHolder.groupImg.setBackgroundResource(R.drawable.rightarrow);
		}
		groupHolder.groupText.setText(groupTitle.get(groupPosition));
		
		groupButton = (Button) convertView.findViewById(R.id.btn_group_function);
		groupButton.setOnClickListener(this);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// Indicates whether the child and group IDs are stable across changes to the underlying data
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// Whether the child at the specified position is selectable
		return true;
	}
	/**
	 * show the text on the child and group item
	 */	
	private class GroupHolder
	{
		ImageView groupImg;
		TextView groupText;
	}
	private class ChildHolder
	{
		ImageView childImg;
		TextView childText;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_group_function:
//			Log.d("MyBaseExpandableListAdapter", "������group button");
		default:
			break;
		}
		
	}	
}
