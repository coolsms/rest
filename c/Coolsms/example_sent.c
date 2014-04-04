#include "coolsms.h"

/*
	example_sent.c
	- example to see sent messages
*/

void print_result(response_struct);

/* set 1 to test example */
#if 0
int main()
{
	/* api_key and api_secret can be obtained from http://www.coolsms.co.kr */
	response_struct result;
	char *api_key = "ENTER_YOUR_OWN";
	char *api_secret = "ENTER_YOUR_OWN";
	user_opt user_info = user_opt_init(api_key, api_secret);
	sent_opt sent_info = sent_opt_init();
	
	/* set send infomations
	More info found at http://doc.coolsms.co.kr?page_id=1811 */
	sent_info.count = "30";
	
	result = _sent(&user_info, &sent_info);
	print_result(result);

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