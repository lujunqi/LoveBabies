package com.xqj.lovebabies.commons;

import java.util.Comparator;
import com.xqj.lovebabies.databases.*;

public class utils_character_comparator implements Comparator<table_interaction_contacts> {

	public int compare(table_interaction_contacts o1, table_interaction_contacts o2) {
		if (o1.getFirst_letter().equals("@") || o2.getFirst_letter().equals("#")) {
			return -1;
		} else if (o1.getFirst_letter().equals("#") || o2.getFirst_letter().equals("@")) {
			return 1;
		} else {
			return o1.getFirst_letter().compareTo(o2.getFirst_letter());
		}
		
	}

}
