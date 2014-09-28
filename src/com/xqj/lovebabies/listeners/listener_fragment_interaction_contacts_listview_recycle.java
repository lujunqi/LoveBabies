package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.R;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.AbsListView.RecyclerListener;
import android.widget.ImageView;

public class listener_fragment_interaction_contacts_listview_recycle implements RecyclerListener {

	@Override
	public void onMovedToScrapHeap(View view) {
		// TODO Auto-generated method stub
		try {
			ImageView iv = (ImageView) view.findViewById(R.id.fragment_interaction_contacts_item_user_icon);
			BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
			if (bitmapDrawable.getBitmap().isRecycled()) {
				bitmapDrawable.getBitmap().recycle();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
