#include "coolsms.h"

/*
	example_send.c
	- send sms messages
*/

void print_result(response_struct);

/* set 1 to test example */
#if 1
int main()
{
	/* api_key and api_secret can be obtained from http://www.coolsms.co.kr */
	response_struct result;
	char *api_key = "ENTER_YOUR_OWN";
	char *api_secret = "ENTER_YOUR_OWN";
	user_opt user_info = user_opt_init(api_key, api_secret);
	send_opt send_info = send_opt_init();
	
	/* set send infomations 
	   More info found at http://doc.coolsms.co.kr?page_id=1811 */
	coolsms_formset(&send_info, COOLSMS_TO, "01000000000");						//받는 사람 번호
	coolsms_formset(&send_info, COOLSMS_FROM, "01000000000");					//보내는 사람 번호
	coolsms_formset(&send_info, COOLSMS_TEXT, "문자 테스팅 문자입니다. ");		//문자 메시지
	//coolsms_formset(&send_info, COOLSMS_SUBJECT, "문자 테스팅");					//문자 제목 <---- LMS 나 MMS 만 적용
	//coolsms_formset(&send_info, COOLSMS_TYPE, "mms");							//문자 타입
	//coolsms_formset(&send_info, COOLSMS_IMAGE, "image.jpg");					//MMS 사진
	//coolsms_formset(&send_info, COOLSMS_CHARSET, "euckr");        //인코딩 설정 default = utf8 (윈도우는 euckr 로 설정하세요)
	result = _send(&user_info, &send_info);
	
	print_result(result);
	coolsms_formfree(&send_info);
	return 0;
}

void print_result(response_struct result)
{
	/* parsing json results */
	json_error_t error;
	json_t *root;
	root = json_loads(result.memory, 0, &error);
	if (!root){
		fprintf(stderr, "error : root\n");
		fprintf(stderr, "error : on line %d: %s\n", error.line, error.text);
		exit(1);
	}

	/* print keys and its values */
	json_t *data, *obj, *array;
	const char * key;
	int i;
	json_object_foreach(root, key, obj){
		printf("%s:", key);
		data = json_object_get(root, key);
		if (!json_is_array(data))
			printf("%s\n", json_dumps(data, JSON_ENCODE_ANY));

		json_array_foreach(obj, i, array){
			printf("%s:\n", json_dumps(array, JSON_ENCODE_ANY));
		}
	}

	/* Jansson library is used for parsing JSON data
	   More info found at https://jansson.readthedocs.org/en/2.5/ */
}

#endif