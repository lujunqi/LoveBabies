package com.xqj.lovebabies.contants;

import com.xqj.lovebabies.R;

public class global_contants {
	public final static String application_server_ip = "220.168.86.34";// 220.168.86.34���Կ�
	public final static String im_server_ip = "220.168.86.34";

	public final static int application_server_port = 8080;
	public final static int im_server_port = 5222;
	public final static String oper_mation = "1";// 1 ��ʾ Android
	// --
	// public final static String application_server_ip = "192.168.1.120";
	// public final static String im_server_ip = "192.168.1.120";
	public final static String lovebaby_url = "http://" + im_server_ip + ":"
			+ application_server_port + "/lovebaby/";
	// ������apk���ص�ַ
	public final static String ibaby_apk_download_path = "http://www.lovebaobei.com.cn:8052/BabyVaccineRemind/download/baby.apk";
	// ����������
	public final static String share_health_info_url = lovebaby_url
			+ "zx/index.jsp";
	// ���ѧϰ��ַ
	public final static String study_web_url = "http://"
			+ application_server_ip + ":" + application_server_port
			+ "/LoveBabyDDXX/";
	public final static String study_web_url_index = study_web_url
			+ "html5/index.jsp";

	public static int[] faceImgId = new int[] { R.drawable.img000,
			R.drawable.img001, R.drawable.img002, R.drawable.img003,
			R.drawable.img004, R.drawable.img005, R.drawable.img006,
			R.drawable.img007, R.drawable.img008, R.drawable.img009,
			R.drawable.img010, R.drawable.img011, R.drawable.img012,
			R.drawable.img013, R.drawable.img014, R.drawable.img015,
			R.drawable.img016, R.drawable.img017, R.drawable.img018,
			R.drawable.img019, R.drawable.img020, R.drawable.img021,
			R.drawable.img022, R.drawable.img023, R.drawable.img024,
			R.drawable.img025, R.drawable.img026, R.drawable.img027,
			R.drawable.img028, R.drawable.img029, R.drawable.img030,
			R.drawable.img031, R.drawable.img032, R.drawable.img033,
			R.drawable.img034, R.drawable.img035, R.drawable.img036,
			R.drawable.img037, R.drawable.img038, R.drawable.img039,
			R.drawable.img040, R.drawable.img041, R.drawable.img042,
			R.drawable.img043, R.drawable.img044, R.drawable.img045,
			R.drawable.img046, R.drawable.img047, R.drawable.img048,
			R.drawable.img049, R.drawable.img050, R.drawable.img051,
			R.drawable.img052, R.drawable.img053, R.drawable.img054,
			R.drawable.img055, R.drawable.img056, R.drawable.img057,
			R.drawable.img058, R.drawable.img059, R.drawable.img060,
			R.drawable.img061, R.drawable.img062, R.drawable.img063,
			R.drawable.img064, R.drawable.img065, R.drawable.img066,
			R.drawable.img067, R.drawable.img068, R.drawable.img069,
			R.drawable.img070, R.drawable.img071, R.drawable.img072,
			R.drawable.img073, R.drawable.img074, R.drawable.img075,
			R.drawable.img076, R.drawable.img077, R.drawable.img078,
			R.drawable.img079, R.drawable.img080, R.drawable.img081,
			R.drawable.img082, R.drawable.img084, R.drawable.img085,
			R.drawable.img087, R.drawable.img088, R.drawable.img091,
			R.drawable.img092, R.drawable.img093, R.drawable.img094,
			R.drawable.img095, R.drawable.img096, R.drawable.img097,
			R.drawable.img098, R.drawable.img100, R.drawable.img101,
			R.drawable.img102, R.drawable.img103, R.drawable.img104 };
	public static String[] faceImgName = new String[] { "/����", "/����", "/��Ƥ",
			"/����", "/͵Ц", "/�ټ�", "/�ô�", "/����", "/��ͷ", "/õ��", "/����", "/���",
			"/����", "/�ؿ�", "/ץ��", "/ί��", "/���", "/ը��", "/�˵�", "/�ɰ�", "/ɫ",
			"/����", "/����", "/����", "/΢Ц", "/��ŭ", "/����", "/����", "/�ܴ�", "/����",
			"/�촽", "/����", "/����", "/�ѹ�", "/����", "/����", "/˯��", "/����", "/��Ц",
			"/����", "/��˥", "/Ʋ��", "/����", "/�ܶ�", "/����", "/�Һ�", "/����", "/��Ц",
			"/����", "/����", "/����", "/���", "/����", "/��ǿ", "/����", "/����", "/ʤ��",
			"/��ȭ", "/��л", "/�Է�", "/����", "/����", "/����", "/����", "/����", "/ŷ��",
			"/����", "/����", "/����", "/С��", "/����", "/NO", "/ȭͷ", "/����", "/̫��",
			"/����", "/����", "/����", "/����", "/����", "/����", "/����", "/����", "/�ٱ�",
			"/����", "/���", "/��Ƿ", "/����", "/ƹ��", "/����", "/����", "/���", "/תȦ",
			"/��ͷ", "/��ͷ", "/����", "/����", "/����", "/��ȭ", "/��ȭ" };

	// ȫ��·��
	public final static String SYS_PATH_SD_CARD = "sys_path_sd_card";
	public final static String SYS_PATH_APP_FOLDER = "sys_path_app_folder";
	public final static String SYS_PATH_CACHE = "sys_path_cache";
	public final static String SYS_PATH_TEMP = "sys_path_temp";
	public final static String SYS_PATH_CRASH = "sys_path_crash";
	public final static String SYS_PATH_UP = "sys_path_up";//ͼƬ��¼���ļ�����
}
