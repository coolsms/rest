#include <stdio.h>
#include <csmd5.h>
#include <jansson.h>
#ifdef _WIN32
#include <windows.h>
#else
#include <unistd.h>
#endif
#include <string.h>
#include <curl/curl.h>

#define SKIP_PEER_VERIFICATION
#define VER "1"
#define GET 0
#define POST 1

/* coolsms_formset options */
#define COOLSMS_TO 1
#define COOLSMS_FROM 2
#define COOLSMS_TEXT 3
#define COOLSMS_TYPE 4
#define COOLSMS_IMAGE 5
#define COOLSMS_REFNAME 6
#define COOLSMS_DATETIME 7
#define COOLSMS_SUBJECT 8
#define COOLSMS_SRK 9
#define COOLSMS_EXTENSION 10
#define COOLSMS_CHARSET 11

typedef struct { char api_key[32], api_secret[64], salt[32], signature[64], user_agent[32], timestamp[64]; }user_opt;
typedef struct { char *to, *from, *text, *type, *image, *refname, *country, *datetime, *mid, *gid, *subject, *charset, *srk, *extension; }send_opt;
typedef struct { char * count, *page, *s_rcpt, *s_start, *s_end, *mid, *gid; } sent_opt;
typedef struct { char * mid, *gid; }cancel_opt;
typedef struct { char * count; }status_opt;
typedef struct { char *memory; size_t size; }response_struct;

int multi_part_curl_process(const user_opt *, const send_opt *, response_struct *);
int curl_process(char*, char*, int, response_struct*);

user_opt user_opt_init(char *, char *);
send_opt send_opt_init();
sent_opt sent_opt_init();
cancel_opt cancel_opt_init();
status_opt status_opt_init();
static size_t WriteMemoryCallback(void *, size_t, size_t, void *);

response_struct _send(const user_opt *, const send_opt *);
response_struct _sent(const user_opt *, const sent_opt *);
response_struct _balance(const user_opt *);
response_struct _cancel(const user_opt *, const cancel_opt *);
response_struct _status(const user_opt *, const status_opt *);

int do_curl_process(char *, char *, int, response_struct *);
char* get_uuid();
void get_timestamp(char*);
void get_signature(const char*, const char *, const char *, char *);
void coolsms_formfree(send_opt *);
void coolsms_formset(send_opt *, int, char *);

