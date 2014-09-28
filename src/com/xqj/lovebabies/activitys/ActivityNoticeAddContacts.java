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
 * �г�ѡ����ϵ���б�
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
				// ��仰д�������
				((TreeAdapter) parent.getAdapter()).ExpandOrCollapse(position);
			}
		});

		setToolBar(new String[] { "ѡ�н��", "", "", "�˳�" }, new int[] { 0, 3 });

		setPreson();
	}

	// ���ýڵ�,����ͨ��ѭ����ݹ鷽ʽ��ӽڵ�
	private void setPreson() {
		// �������ڵ�
		Node root = new Node("�Ϸ��й�����", "000000");
//		root.setIcon(R.drawable.baby_pic_1);// ����ͼ��
		// root.setCheckBox(false);//���ýڵ�ǰ���޸�ѡ��

		// ����1���ӽڵ�
		Node n1 = new Node("�ΰ�������", "1");
		n1.setParent(root);// ���ø��ڵ�
//		n1.setIcon(R.drawable.baby_pic_2);

		Node n11 = new Node("��ΰ", "13966664567");
		n11.setParent(n1);
//		n11.setIcon(R.drawable.baby_pic_3);
		n11.setRemark("13966664567");
		Node n12 = new Node("��ͬ��", "13966664567");
		n12.setParent(n1);
//		n12.setIcon(R.drawable.baby_pic_4);
		n12.setRemark("13966664567");

		n1.add(n11);
		n1.add(n12);

		// ����1���ӽڵ�
		Node n2 = new Node("���¾�����", "2");
		n2.setParent(root);
//		n2.setIcon(R.drawable.baby_pic_5);
		Node n21 = new Node("���λ�", "13966664567");
		n21.setParent(n2);
//		n21.setIcon(R.drawable.zsabb_album_pic_1);
		n21.setRemark("13966664567");
		Node n22 = new Node("����", "13966664567");
		n22.setParent(n2);
//		n22.setIcon(R.drawable.zsabb_album_pic_2);
		n22.setRemark("13966664567");
		Node n23 = new Node("������", "13766604867");
		n23.setParent(n2);
//		n23.setIcon(R.drawable.zsabb_album_pic_3);
		n23.setRemark("13766604867");
		n2.add(n21);
		n2.add(n22);
		n2.add(n23);

		// ����1���ӽڵ�
		Node n3 = new Node("Ѳ���������", "3");
		n3.setParent(root);
//		n3.setIcon(R.drawable.zsabb_album_pic_4);
		Node n31 = new Node("��ѷ��", "15305510131");
		n31.setParent(n3);
//		n31.setIcon(R.drawable.zsabb_album_pic_5);
		n31.setRemark("15890875672");
		Node n32 = new Node("�����û�", "13855196982");
		n32.setParent(n3);
//		n32.setIcon(R.drawable.ic_launcher);
		n32.setRemark("15890875672");

		// ����2���ӽڵ�
		Node n33 = new Node("Ѳ����һ�ж�", "31");
		n33.setParent(n3);
//		n33.setIcon(R.drawable.default_head_icon);

		Node n331 = new Node("���", "15890875672");
		n331.setParent(n33);
		// n331.setIcon(R.drawable.icon_police);
		n331.setRemark("15890875672");
		Node n332 = new Node("������", "15890875672");
		n332.setParent(n33);
//		n332.setIcon(R.drawable.default_logo);
		n332.setRemark("15890875672");
		Node n333 = new Node("˾ͽ����", "15890875672");
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
		// �����������Ƿ���ʾ��ѡ��
		tree_adapter.setCheckBox(true);
		// ����չ�����۵�ʱͼ��
		tree_adapter.setExpandedCollapsedIcon(R.drawable.interaction_notice_get_contact_expanced, 
				R.drawable.interaction_notice_get_contact_collapsed);
		// ����Ĭ��չ������
		tree_adapter.setExpandLevel(2);
		code_list.setAdapter(tree_adapter);
	}

	// ���õײ�������
	private void setToolBar(String[] name_array, int[] pos_array) {
		toolBar.removeAllViews();

		GridView toolbarGrid = new GridView(this);
		toolbarGrid.setNumColumns(4);// ����ÿ������
		toolbarGrid.setGravity(Gravity.CENTER);// λ�þ���
		ToolbarAdapter adapter = new ToolbarAdapter(this, name_array, null,
				pos_array);
		toolbarGrid.setAdapter(adapter);
		toolbarGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:// ��ʾѡ�н��
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
						Toast.makeText(oThis, "û��ѡ���κ���", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(oThis, msg, Toast.LENGTH_SHORT).show();
					}

					break;
				case 3:// ����
					oThis.finish();
					System.exit(0);
					break;
				}
			}
		});
		toolBar.addView(toolbarGrid);
	}
}
