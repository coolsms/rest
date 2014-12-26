#include "coolsms.h"

/**
Copyright(C) 2008 - 2014 NURIGO
http ://www.coolsms.co.kr
**/


user_opt user_opt_init(char *api_key, char *api_secret)
{
	user_opt user_info;
	char * uuid;
	strcpy(user_info.api_key, api_key);
	strcpy(user_info.api_secret, api_secret);
	get_timestamp(user_info.timestamp);
	uuid = get_uuid();
	strcpy(user_info.salt, uuid);
	get_signature(user_info.timestamp, user_info.salt, user_info.api_secret, user_info.signature);
	free(uuid);
	return user_info;
}

void get_timestamp(char *output)
{
	time_t timestamp;
	time(&timestamp);
	sprintf(output, "%d", timestamp);
}

send_opt send_opt_init()
{
	send_opt send_info = { "\0", "\0", "\0", "\0", "\0", "\0", "\0", "\0", "\0", "\0", "\0", "\0", "\0", "\0" };
	send_info.charset = strdup("utf8");
	return send_info;
}

sent_opt sent_opt_init()
{
	sent_opt sent_info = { "\0", "\0", "\0", "\0", "\0", "\0", "\0" };
	return sent_info;
}

cancel_opt cancel_opt_init()
{
	cancel_opt cancel_info = { "\0", "\0" };
	return cancel_info;
}

status_opt status_opt_init()
{
	status_opt status_info = { "\0" };
	return status_info;
}

static size_t WriteMemoryCallback(void *contents, size_t size, size_t nmemb, void *userp)
{
	size_t realsize = size * nmemb;
	response_struct *mem = (response_struct *)userp;

	mem->memory = (char*)realloc(mem->memory, mem->size + realsize + 1);
	if (mem->memory == NULL) {
		/* out of memory! */
		printf("not enough memory (realloc returned NULL)\n");
		return 0;
	}

	memcpy(&(mem->memory[mem->size]), contents, realsize);
	mem->size += realsize;
	mem->memory[mem->size] = 0;

	return realsize;
}

response_struct _sent(const user_opt *u, const sent_opt *s)
{
	char options[1024];
	response_struct output;
	sprintf(options, "api_key=%s&salt=%s&signature=%s&timestamp=%s&count=%s&page=%s&s_rcpt=%s&s_start=%s&s_end=%s&mid=%s&gid=%s", 
		u->api_key, u->salt, u->signature, u->timestamp, s->count, s->page, s->s_rcpt, s->s_start, s->s_end, s->mid, s->gid);
	if (curl_process(options, "sent", GET, &output) == CURLE_OK)
		printf("\nSuccess!\n");
	else
		printf("\nError!\n");
	
	return output;
}

response_struct _balance(const user_opt *u)
{
	char options[1024];
	response_struct output;
	sprintf(options, "api_key=%s&salt=%s&signature=%s&timestamp=%s",
		u->api_key, u->salt, u->signature, u->timestamp);

	if (curl_process(options, "balance", GET, &output) == CURLE_OK)
		printf("\nSuccess!\n");
	else
		printf("\nError!\n");
	return output;
}

response_struct _cancel(const user_opt *u, const cancel_opt *t)
{
	char options[1024];
	response_struct output;
	sprintf(options, "api_key=%s&salt=%s&signature=%s&timestamp=%s&mid=%s&gid=%s",
		u->api_key, u->salt, u->signature, u->timestamp);

	if (curl_process(options, "cancel", POST, &output) == CURLE_OK)
		printf("\nSuccess!\n");
	else
		printf("\nError!\n");
	return output;
}

response_struct _status(const user_opt *u, const status_opt *t)
{
	char options[1024];
	response_struct output;
	sprintf(options, "api_key=%s&salt=%s&signature=%s&timestamp=%s&count=%s",
		u->api_key, u->salt, u->signature, u->timestamp, t->count);
	
	if (curl_process(options, "status", GET, &output) == CURLE_OK)
		printf("\nSuccess!\n");
	else
		printf("\nError!\n");
	return output;
}

response_struct _send(const user_opt *u, const send_opt *s)
{
	char options[1024];
	response_struct output;
	struct curl_httppost *formpost = NULL;
	struct curl_httppost *lastptr = NULL;
	if (strcmp(s->type, "mms"))
	{
		sprintf(options, "api_key=%s&salt=%s&signature=%s&timestamp=%s&to=%s&from=%s&text=%s&type=%s&image=%s&refname=%s&country=%s&datetime=%s&mid=%s&gid=%s&subject=%s&charset=%s&srk=%s",
			u->api_key, u->salt, u->signature, u->timestamp, s->to, s->from, s->text, s->type, s->image, s->refname, s->country, s->datetime, s->mid, s->gid, s->subject, s->charset, s->srk);
		if (curl_process(options, "send", POST, &output) == CURLE_OK)
			printf("\nSuccess!\n");
		else
			printf("\nError!\n");
	}
	else
	{
		if (multi_part_curl_process(u, s, &output) == CURLE_OK)
			printf("\nSuccess!\n");
		else
			printf("\nError!\n");
	}
	return output;
}

void coolsms_formset(send_opt *s, int option, char * value)
{
	switch (option)
	{
	case COOLSMS_TO: s->to = strdup(value);
		break;
	case COOLSMS_FROM: s->from = strdup(value);
		break;
	case COOLSMS_TEXT: s->text = strdup(value);
		break;
	case COOLSMS_TYPE: s->type = strdup(value);
		break;
	case COOLSMS_IMAGE: s->image = strdup(value);
		break;
	case COOLSMS_REFNAME: s->refname = strdup(value);
		break;
	case COOLSMS_DATETIME: s->datetime = strdup(value);
		break;
	case COOLSMS_SUBJECT: s->subject = strdup(value);
		break;
	case COOLSMS_SRK: s->srk = strdup(value);
		break;
	case COOLSMS_CHARSET: 
		if (s->charset) free(s->charset);
		s->charset = strdup(value);
		break;
	case COOLSMS_EXTENSION: s->extension = strdup(value);
		break;
	}
}

void coolsms_formfree(send_opt *s)
{
	if (s->to != "\0")	free(s->to);
	if (s->from != "\0") free(s->from);
	if (s->text != "\0") free(s->text);
	if (s->type != "\0") free(s->type);
	if (s->image != "\0") free(s->image);
	if (s->refname != "\0") free(s->refname);
	if (s->country != "\0") free(s->country);
	if (s->datetime != "\0") free(s->datetime);
	if (s->mid != "\0") free(s->mid);
	if (s->gid != "\0") free(s->gid);
	if (s->subject != "\0") free(s->subject);
	if (s->charset != "\0") free(s->charset);
	if (s->srk != "\0") free(s->srk);
	if (s->extension != "\0") free(s->extension);
}

int multi_part_curl_process(const user_opt *u, const send_opt *s, response_struct *output)
{
	CURL *curl;
	CURLcode res;
	char url[1024];
	struct curl_httppost *formpost = NULL;
	struct curl_httppost *lastptr = NULL;
	struct curl_slist *headerlist = NULL;
	response_struct response;
	/* initialize response struct */
	response.memory = (char*)malloc(1);  /* will be grown as needed by the realloc above */
	response.size = 0;    /* no data at this point */
	
	/* set url and initilize curl */
	sprintf(url, "%s/%s/%s", "https://api.coolsms.co.kr/", VER, "send");
	curl = curl_easy_init();

	/* set values to curl_form */
	curl_formadd(&formpost, &lastptr, CURLFORM_COPYNAME, "api_key", CURLFORM_COPYCONTENTS, u->api_key, CURLFORM_END);
	curl_formadd(&formpost, &lastptr, CURLFORM_COPYNAME, "salt", CURLFORM_COPYCONTENTS, u->salt, CURLFORM_END);
	curl_formadd(&formpost, &lastptr, CURLFORM_COPYNAME, "signature", CURLFORM_COPYCONTENTS, u->signature, CURLFORM_END);
	curl_formadd(&formpost, &lastptr, CURLFORM_COPYNAME, "timestamp", CURLFORM_COPYCONTENTS, u->timestamp, CURLFORM_END);
	curl_formadd(&formpost, &lastptr, CURLFORM_COPYNAME, "image", CURLFORM_FILE, s->image, CURLFORM_CONTENTTYPE, "image/jpeg", CURLFORM_END);
	curl_formadd(&formpost, &lastptr, CURLFORM_COPYNAME, "to", CURLFORM_COPYCONTENTS, s->to, CURLFORM_END);
	curl_formadd(&formpost, &lastptr, CURLFORM_COPYNAME, "from", CURLFORM_COPYCONTENTS, s->from, CURLFORM_END);
	curl_formadd(&formpost, &lastptr, CURLFORM_COPYNAME, "text", CURLFORM_COPYCONTENTS, s->text, CURLFORM_END);
	curl_formadd(&formpost, &lastptr, CURLFORM_COPYNAME, "type", CURLFORM_COPYCONTENTS, s->type, CURLFORM_END);
	curl_formadd(&formpost, &lastptr, CURLFORM_COPYNAME, "refname", CURLFORM_COPYCONTENTS, s->refname, CURLFORM_END);
	curl_formadd(&formpost, &lastptr, CURLFORM_COPYNAME, "datetime", CURLFORM_COPYCONTENTS, s->datetime, CURLFORM_END);
	curl_formadd(&formpost, &lastptr, CURLFORM_COPYNAME, "mid", CURLFORM_COPYCONTENTS, s->mid, CURLFORM_END);
	curl_formadd(&formpost, &lastptr, CURLFORM_COPYNAME, "gid", CURLFORM_COPYCONTENTS, s->gid, CURLFORM_END);
	
	if (curl)
	{
		curl_easy_setopt(curl, CURLOPT_URL, url);
		curl_easy_setopt(curl, CURLOPT_VERBOSE, 1L); /* CURLOPT_VERBOSE shows infomation about connected server. USEFUL for debugging */
		curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headerlist);
		curl_easy_setopt(curl, CURLOPT_HTTPPOST, formpost);
		curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 0L);
		curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteMemoryCallback);
		curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void *)&response);
		curl_easy_setopt(curl, CURLOPT_USERAGENT, "REST SDK C&CPP/1.0");
		res = curl_easy_perform(curl);
		
		/* Check for errors */
		if (res != CURLE_OK)
			fprintf(stderr, "curl_easy_perform() failed: %s\n", curl_easy_strerror(res));
		else
			printf("%lu bytes retrieved\n", (long)response.size);
		
		*output = response;
		/* then cleanup curl, formpost and headerlist */
		curl_easy_cleanup(curl);
		curl_formfree(formpost);
		curl_slist_free_all(headerlist);
	}
	return res;
}

int curl_process(char *options, char *path, int method, response_struct *output)
{
	CURL *curl;
	CURLcode res;
	char url[1024];
	struct curl_slist *headerlist = NULL;
	response_struct response;
	response.memory = (char*)malloc(1);  /* will be grown as needed by the realloc above */
	response.size = 0;    /* no data at this point */
	
	/* set url */
	if (method)
		sprintf(url, "%s/%s/%s", "https://api.coolsms.co.kr/", VER, path);
	else
		sprintf(url, "%s/%s/%s?%s", "https://api.coolsms.co.kr/", VER, path, options);
	
	/* initialize curl */
	curl_global_init(CURL_GLOBAL_DEFAULT);
	curl = curl_easy_init();

	if (curl) {
		curl_easy_setopt(curl, CURLOPT_URL, url);
		if (method)
		{
			curl_easy_setopt(curl, CURLOPT_POST, 1);
			curl_easy_setopt(curl, CURLOPT_POSTFIELDS, options);
		}
		else
		{
			curl_easy_setopt(curl, CURLOPT_HTTPGET, 1);
		}
		curl_easy_setopt(curl, CURLOPT_VERBOSE, 1L); /* CURLOPT_VERBOSE shows infomation about connected server. USEFUL for debugging */
		curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headerlist);
		curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 0L);
		curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteMemoryCallback);
		curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void *)&response);
		curl_easy_setopt(curl, CURLOPT_USERAGENT, "REST SDK C&CPP/1.0");
		res = curl_easy_perform(curl);
		
		/* Check for errors */
		if (res != CURLE_OK)
			fprintf(stderr, "curl_easy_perform() failed: %s\n", curl_easy_strerror(res));
		else
			printf("%lu bytes retrieved\n", (long)response.size);

		/* always cleanup */
		
		curl_easy_cleanup(curl);
	}
	curl_global_cleanup();
	*output = response;
	
	return res;
}

char* get_uuid()
{
	struct tm tm_val;
	char *buf;

	buf = (char *)malloc(40);
	memset(buf, 0, sizeof(buf));
	memset(&tm_val, 0, sizeof(tm_val));
#ifdef _WIN32
	SYSTEMTIME st;
	GetLocalTime(&st);
	sprintf(buf, "%u%u%u%u%u%u%u", st.wYear, st.wMonth, st.wDay, st.wHour, st.wMinute, st.wSecond, st.wMilliseconds);
#else
	t = time(NULL);
	(void)localtime_r(&t, &tm_val);
	strftime(buf, sizeof(buf), "%Y%m%d%H%M%S", &tm_val);
#endif
	
	return buf;
}

void get_signature(const char *datetime, const char *salt, const char * api_secret, char *output)
{
	unsigned char signature[16];
	char data_unsigned[64];
	char * hash_data;
	hash_data = (char*)malloc(strlen(datetime)+strlen(salt)+1);
	/*hash 예외처리*/
	sprintf(data_unsigned, "%s%s", datetime, salt);
	md5_hmac((unsigned char*)api_secret, strlen(api_secret), (unsigned char*)data_unsigned, strlen(data_unsigned), signature);
	for (int i = 0; i < sizeof(signature); i++)
	{
		sprintf(&output[i*2], "%02x", signature[i]);
	}
}