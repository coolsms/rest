import org.json.simple.JSONArray;

public class Set
{
	private String[] to=null;
	private String from=null;
	private String text=null;
	private String type=null;
	private String image=null;
	private String imagePath="./";
	private String refname=null;
	private String country=null;
	private String datetime=null;
	private String mid=null;
	private String gid=null;
	private String subject=null;
	private String count=null;
	private String page=null;
	private String sRcpt=null;
	private String sStart=null;
	private String sEnd=null;
	private String charset="UTF8";	
	private String srk=null;
	private String mode=null;
	private JSONArray extension=null;
	
	
	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}
	
	public void setTo(String to){
		String toArray[] = to.split(",");
		this.to = toArray;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String filePath) {
		this.imagePath = filePath;
	}

	public String getRefname() {
		return refname;
	}

	public void setRefname(String refname) {
		this.refname = refname;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getSRcpt() {
		return sRcpt;
	}

	public void setSRcpt(String sRcpt) {
		this.sRcpt = sRcpt;
	}

	public String getSStart() {
		return sStart;
	}

	public void setSStart(String s_start) {
		this.sStart = s_start;
	}

	public String getSEnd() {
		return sEnd;
	}

	public void setSEnd(String s_end) {
		this.sEnd = s_end;
	}

	public String getSrk() {
		return srk;
	}

	public void setSrk(String srk) {
		this.srk = srk;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public JSONArray getExtension() {
		return extension;
	}

	public void setExtension(JSONArray extension) {
		this.extension = extension;
	}
}
