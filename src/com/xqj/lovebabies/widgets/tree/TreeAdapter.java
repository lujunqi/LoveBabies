/**
 * �������:android���޼���Դ��
 * ���ߣ�shaolong
 * ��ַ:http://londroid.5d6d.com
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
 * ������Դ������
 * @author LongShao
 *
 */
public class TreeAdapter extends BaseAdapter{

	private Context con;
	private LayoutInflater lif;
	private List<Node> allsCache = new ArrayList<Node>();
	private List<Node> alls = new ArrayList<Node>();
	private TreeAdapter oThis = this;
	private boolean hasCheckBox = true;//�Ƿ�ӵ�и�ѡ��
	private int expandedIcon = -1;
	private int collapsedIcon = -1;
	
	/**
	 * TreeAdapter���캯��
	 * @param context 
	 * @param rootNode ���ڵ�
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
	

	// ��ѡ������
	private void checkNode(Node node,boolean isChecked){
		node.setChecked(isChecked);
		for(int i=0;i<node.getChildren().size();i++){
			checkNode(node.getChildren().get(i),isChecked);
		}
	}
	
	/**
	 * ���ѡ�нڵ�
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
	
	// ���ƽڵ��չ�����۵�
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
     * �����Ƿ�ӵ�и�ѡ��
     * @param hasCheckBox
     */
    public void setCheckBox(boolean hasCheckBox){
    	this.hasCheckBox = hasCheckBox;
    }
	
    /**
     * ����չ�����۵�״̬ͼ��
     * @param expandedIcon չ��ʱͼ��
     * @param collapsedIcon �۵�ʱͼ��
     */
    public void setExpandedCollapsedIcon(int expandedIcon,int collapsedIcon){
    	this.expandedIcon = expandedIcon;
    	this.collapsedIcon = collapsedIcon;
    }
    
	/**
	 * ����չ������
	 * @param level
	 */
	public void setExpandLevel(int level){
		alls.clear();
		for(int i=0;i<allsCache.size();i++){
			Node n = allsCache.get(i);
			if(n.getLevel()<=level){
				if(n.getLevel()<level){// �ϲ㶼����չ��״̬
					n.setExpanded(true);
				}else{// ���һ�㶼�����۵�״̬
					n.setExpanded(false);
				}
				alls.add(n);
			}
		}
		this.notifyDataSetChanged();
	}
	
	/**
	 * ���ƽڵ��չ��������
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
		// �õ���ǰ�ڵ�
		Node n = alls.get(position);
		if (view == null) {
			holder = new ViewHolder();
			view = this.lif.inflate(R.layout.tree_item_listview, null);
			holder.chbSelect = (ImageView)view.findViewById(R.id.chbSelect);
			holder.tvText = (TextView)view.findViewById(R.id.tvText);
			holder.remarText = (TextView)view.findViewById(R.id.phone_textview);
			holder.ivExEc = (ImageView)view.findViewById(R.id.ivExEc);
			holder.checkAllText = (TextView)view.findViewById(R.id.check_all_textview);
			
			// ��ѡ�򵥻��¼�
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
		
		// �Ƿ���ʾ��ѡ��
		if(n.hasCheckBox() && hasCheckBox){
			holder.chbSelect.setVisibility(View.VISIBLE);
		}else{
			holder.chbSelect.setVisibility(View.GONE);
		}
		
		// �Ƿ���ʾͼ��
//		if(n.getIcon() == -1){
//		    holder.ivIcon.setVisibility(View.GONE);
//		}else{
//			holder.ivIcon.setVisibility(View.VISIBLE);
//			holder.ivIcon.setImageResource(n.getIcon());
//		}
		
		// ��ʾ�ı�
		holder.tvText.setText(n.getText());
		System.out.println(n.isLeaf()+"--"+n.getRemark());
		if(n.isLeaf()){
			System.out.println("��Ҷ�ڵ�...");
			view.setBackgroundColor(con.getResources().getColor(R.color.white));
			// ��Ҷ�ڵ� ����ʾչ�����۵�״̬ͼ��
			holder.ivExEc.setVisibility(View.GONE);
			holder.checkAllText.setVisibility(View.GONE);
			holder.remarText.setVisibility(View.VISIBLE);
			holder.remarText.setText(n.getRemark());
		}else{
			System.out.println("�Ǹ��ڵ�...");
			view.setBackgroundResource(R.drawable.interaction_notice_get_contact_org_bg);
			holder.remarText.setVisibility(View.GONE);
			// ����ʱ�����ӽڵ�չ�����۵�,״̬ͼ��ı�
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
		
		// ��������
//		view.setPadding(35*n.getLevel(), 3,3, 3);
		
		
		return view;
	}

	
	/**
	 * 
	 * �б���ؼ�����
	 *
	 */
	public class ViewHolder{
		ImageView chbSelect;//ѡ�����
//		ImageView ivIcon;//ͼ��
		TextView tvText;//����
		TextView remarText;//����
		TextView checkAllText;//ȫѡ
		ImageView ivExEc;//չ�����۵����">"��"v"
	}
}
