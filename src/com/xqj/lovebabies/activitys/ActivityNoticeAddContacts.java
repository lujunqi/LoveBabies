package com.xqj.lovebabies.activitys;

import java.util.List;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.widgets.tree.Node;
import com.xqj.lovebabies.widgets.tree.ToolbarAdapter;
import com.xqj.lovebabies.widgets.tree.TreeAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 列出选择联系人列表
 */
public class ActivityNoticeAddContacts extends Activity {
	private ListView code_list;
	private LinearLayout toolBar;
	private ActivityNoticeAddContacts oThis = this;
	private TreeAdapter tree_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interaction_notice_editor_add_contacts);

		toolBar = (LinearLayout) findViewById(R.id.toolBar);
		code_list = (ListView) findViewById(R.id.code_list);
		code_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				// 这句话写在最后面
				((TreeAdapter) parent.getAdapter()).ExpandOrCollapse(position);
			}
		});

		setToolBar(new String[] { "选中结果", "", "", "退出" }, new int[] { 0, 3 });

		setPreson();
	}

	// 设置节点,可以通过循环或递归方式添加节点
	private void setPreson() {
		// 创建根节点
		Node root = new Node("合肥市公安局", "000000");
//		root.setIcon(R.drawable.baby_pic_1);// 设置图标
		// root.setCheckBox(false);//设置节点前有无复选框

		// 创建1级子节点
		Node n1 = new Node("治安警察大队", "1");
		n1.setParent(root);// 设置父节点
//		n1.setIcon(R.drawable.baby_pic_2);

		Node n11 = new Node("李伟", "13966664567");
		n11.setParent(n1);
//		n11.setIcon(R.drawable.baby_pic_3);
		n11.setRemark("13966664567");
		Node n12 = new Node("张同刚", "13966664567");
		n12.setParent(n1);
//		n12.setIcon(R.drawable.baby_pic_4);
		n12.setRemark("13966664567");

		n1.add(n11);
		n1.add(n12);

		// 创建1级子节点
		Node n2 = new Node("刑事警察大队", "2");
		n2.setParent(root);
//		n2.setIcon(R.drawable.baby_pic_5);
		Node n21 = new Node("曹梦华", "13966664567");
		n21.setParent(n2);
//		n21.setIcon(R.drawable.zsabb_album_pic_1);
		n21.setRemark("13966664567");
		Node n22 = new Node("文燕", "13966664567");
		n22.setParent(n2);
//		n22.setIcon(R.drawable.zsabb_album_pic_2);
		n22.setRemark("13966664567");
		Node n23 = new Node("赵文涛", "13766604867");
		n23.setParent(n2);
//		n23.setIcon(R.drawable.zsabb_album_pic_3);
		n23.setRemark("13766604867");
		n2.add(n21);
		n2.add(n22);
		n2.add(n23);

		// 创建1级子节点
		Node n3 = new Node("巡警防暴大队", "3");
		n3.setParent(root);
//		n3.setIcon(R.drawable.zsabb_album_pic_4);
		Node n31 = new Node("崔逊田", "15305510131");
		n31.setParent(n3);
//		n31.setIcon(R.drawable.zsabb_album_pic_5);
		n31.setRemark("15890875672");
		Node n32 = new Node("测试用户", "13855196982");
		n32.setParent(n3);
//		n32.setIcon(R.drawable.ic_launcher);
		n32.setRemark("15890875672");

		// 创建2级子节点
		Node n33 = new Node("巡警第一中队", "31");
		n33.setParent(n3);
//		n33.setIcon(R.drawable.default_head_icon);

		Node n331 = new Node("张楠", "15890875672");
		n331.setParent(n33);
		// n331.setIcon(R.drawable.icon_police);
		n331.setRemark("15890875672");
		Node n332 = new Node("阮明东", "15890875672");
		n332.setParent(n33);
//		n332.setIcon(R.drawable.default_logo);
		n332.setRemark("15890875672");
		Node n333 = new Node("司徒正雄", "15890875672");
		n333.setParent(n33);
		// n333.setIcon(R.drawable.icon_police);
		n333.setRemark("15890875672");
		n33.add(n331);
		n33.add(n332);
		n33.add(n333);

		n3.add(n31);
		n3.add(n32);
		n3.add(n33);

		root.add(n3);
		root.add(n1);
		root.add(n2);

		tree_adapter = new TreeAdapter(oThis, root);
		// 设置整个树是否显示复选框
		tree_adapter.setCheckBox(true);
		// 设置展开和折叠时图标
		tree_adapter.setExpandedCollapsedIcon(R.drawable.interaction_notice_get_contact_expanced, 
				R.drawable.interaction_notice_get_contact_collapsed);
		// 设置默认展开级别
		tree_adapter.setExpandLevel(2);
		code_list.setAdapter(tree_adapter);
	}

	// 设置底部工具栏
	private void setToolBar(String[] name_array, int[] pos_array) {
		toolBar.removeAllViews();

		GridView toolbarGrid = new GridView(this);
		toolbarGrid.setNumColumns(4);// 设置每行列数
		toolbarGrid.setGravity(Gravity.CENTER);// 位置居中
		ToolbarAdapter adapter = new ToolbarAdapter(this, name_array, null,
				pos_array);
		toolbarGrid.setAdapter(adapter);
		toolbarGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:// 显示选中结果
					List<Node> nodes = ((TreeAdapter) code_list.getAdapter())
							.getSeletedNodes();
					String msg = "";

					for (int i = 0; i < nodes.size(); i++) {
						Node n = nodes.get(i);
						if (n.isLeaf()) {
							if (!msg.equals(""))
								msg += ",";
							msg += n.getText() + "(" + n.getValue() + ")";
						}

					}
					if (msg.equals("")) {
						Toast.makeText(oThis, "没有选中任何项", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(oThis, msg, Toast.LENGTH_SHORT).show();
					}

					break;
				case 3:// 返回
					oThis.finish();
					System.exit(0);
					break;
				}
			}
		});
		toolBar.addView(toolbarGrid);
	}
}
