/**
 * 软件名称:android无限级树源码
 * 作者：shaolong
 * 网址:http://londroid.5d6d.com
 * Download by http://www.codefans.net
 */

package com.xqj.lovebabies.widgets.tree;

import java.util.ArrayList;
import java.util.List;

import com.xqj.lovebabies.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 树数据源构造器
 * @author LongShao
 *
 */
public class TreeAdapter extends BaseAdapter{

	private Context con;
	private LayoutInflater lif;
	private List<Node> allsCache = new ArrayList<Node>();
	private List<Node> alls = new ArrayList<Node>();
	private TreeAdapter oThis = this;
	private boolean hasCheckBox = true;//是否拥有复选框
	private int expandedIcon = -1;
	private int collapsedIcon = -1;
	
	/**
	 * TreeAdapter构造函数
	 * @param context 
	 * @param rootNode 根节点
	 */
	public TreeAdapter(Context context,Node rootNode){
		this.con = context;
		this.lif = (LayoutInflater) con.
    	getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addNode(rootNode);
	}
	
	private void addNode(Node node){
		alls.add(node);
		allsCache.add(node);
		if(node.isLeaf())return;
		for(int i=0;i<node.getChildren().size();i++){
			addNode(node.getChildren().get(i));
		}
	}
	

	// 复选框联动
	private void checkNode(Node node,boolean isChecked){
		node.setChecked(isChecked);
		for(int i=0;i<node.getChildren().size();i++){
			checkNode(node.getChildren().get(i),isChecked);
		}
	}
	
	/**
	 * 获得选中节点
	 * @return
	 */
	public List<Node> getSeletedNodes(){
		List<Node> nodes = new ArrayList<Node>();
		for(int i=0;i<allsCache.size();i++){
			Node n = allsCache.get(i);
			if(n.isChecked()){
				nodes.add(n);
			}
		}
		return nodes;
	}
	
	// 控制节点的展开和折叠
	private void filterNode(){
		alls.clear();
		for(int i=0;i<allsCache.size();i++){
			Node n = allsCache.get(i);
			if(!n.isParentCollapsed() || n.isRoot()){
				alls.add(n);
			}
		}
	}
	
	/**
     * 设置是否拥有复选框
     * @param hasCheckBox
     */
    public void setCheckBox(boolean hasCheckBox){
    	this.hasCheckBox = hasCheckBox;
    }
	
    /**
     * 设置展开和折叠状态图标
     * @param expandedIcon 展开时图标
     * @param collapsedIcon 折叠时图标
     */
    public void setExpandedCollapsedIcon(int expandedIcon,int collapsedIcon){
    	this.expandedIcon = expandedIcon;
    	this.collapsedIcon = collapsedIcon;
    }
    
	/**
	 * 设置展开级别
	 * @param level
	 */
	public void setExpandLevel(int level){
		alls.clear();
		for(int i=0;i<allsCache.size();i++){
			Node n = allsCache.get(i);
			if(n.getLevel()<=level){
				if(n.getLevel()<level){// 上层都设置展开状态
					n.setExpanded(true);
				}else{// 最后一层都设置折叠状态
					n.setExpanded(false);
				}
				alls.add(n);
			}
		}
		this.notifyDataSetChanged();
	}
	
	/**
	 * 控制节点的展开和收缩
	 * @param position
	 */
	public void ExpandOrCollapse(int position){
		Node n = alls.get(position);
		if(n != null){
			if(!n.isLeaf()){
				n.setExpanded(!n.isExpanded());
				filterNode();
				this.notifyDataSetChanged();
			}
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return alls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return alls.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder = null;
		// 得到当前节点
		Node n = alls.get(position);
		if (view == null) {
			holder = new ViewHolder();
			view = this.lif.inflate(R.layout.tree_item_listview, null);
			holder.chbSelect = (ImageView)view.findViewById(R.id.chbSelect);
			holder.tvText = (TextView)view.findViewById(R.id.tvText);
			holder.remarText = (TextView)view.findViewById(R.id.phone_textview);
			holder.ivExEc = (ImageView)view.findViewById(R.id.ivExEc);
			holder.checkAllText = (TextView)view.findViewById(R.id.check_all_textview);
			
			// 复选框单击事件
			holder.chbSelect.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					
					Node n = (Node)v.getTag();
//					checkNode(n,((CheckBox)v).isChecked());
					checkNode(n, !n.isChecked());
//					ImageView imageView = (ImageView)v;
//					if(n.isChecked()){
//						imageView.setImageResource(R.drawable.interaction_notice_get_contact_checked);
//					}else{
//						imageView.setImageResource(R.drawable.interaction_notice_get_contact_unchecked);
//					}
					oThis.notifyDataSetChanged();
				}
				
			});
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		holder.chbSelect.setTag(n);
		if(n.isChecked()){
			holder.chbSelect.setImageResource(R.drawable.interaction_notice_get_contact_checked);
		}else{
			holder.chbSelect.setImageResource(R.drawable.interaction_notice_get_contact_unchecked);
		}
		
		// 是否显示复选框
		if(n.hasCheckBox() && hasCheckBox){
			holder.chbSelect.setVisibility(View.VISIBLE);
		}else{
			holder.chbSelect.setVisibility(View.GONE);
		}
		
		// 是否显示图标
//		if(n.getIcon() == -1){
//		    holder.ivIcon.setVisibility(View.GONE);
//		}else{
//			holder.ivIcon.setVisibility(View.VISIBLE);
//			holder.ivIcon.setImageResource(n.getIcon());
//		}
		
		// 显示文本
		holder.tvText.setText(n.getText());
		System.out.println(n.isLeaf()+"--"+n.getRemark());
		if(n.isLeaf()){
			System.out.println("是叶节点...");
			view.setBackgroundColor(con.getResources().getColor(R.color.white));
			// 是叶节点 不显示展开和折叠状态图标
			holder.ivExEc.setVisibility(View.GONE);
			holder.checkAllText.setVisibility(View.GONE);
			holder.remarText.setVisibility(View.VISIBLE);
			holder.remarText.setText(n.getRemark());
		}else{
			System.out.println("是父节点...");
			view.setBackgroundResource(R.drawable.interaction_notice_get_contact_org_bg);
			holder.remarText.setVisibility(View.GONE);
			// 单击时控制子节点展开和折叠,状态图标改变
			holder.ivExEc.setVisibility(View.VISIBLE);
			holder.checkAllText.setVisibility(View.VISIBLE);
			if(n.isExpanded()){
				if(expandedIcon != -1)
				holder.ivExEc.setImageResource(expandedIcon);
			}
			else {
				if(collapsedIcon != -1)
				holder.ivExEc.setImageResource(collapsedIcon);
			}
			
		}
		
		// 控制缩进
//		view.setPadding(35*n.getLevel(), 3,3, 3);
		
		
		return view;
	}

	
	/**
	 * 
	 * 列表项控件集合
	 *
	 */
	public class ViewHolder{
		ImageView chbSelect;//选中与否
//		ImageView ivIcon;//图标
		TextView tvText;//名字
		TextView remarText;//号码
		TextView checkAllText;//全选
		ImageView ivExEc;//展开或折叠标记">"或"v"
	}
}
