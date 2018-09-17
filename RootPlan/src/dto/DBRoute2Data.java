package dto;

public class DBRoute2Data {
	private String rid;
	private String cid;
	private String car_html;
	private String car_xml;
	private String car_mark;
	private String pt_html;
	private String pt_xml;
	private String pt_mark;
	
	public DBRoute2Data(String cid,String rid){
		this.cid = cid;
		this.rid = rid;
	}

	public String getCar_html() {
		return car_html;
	}

	public void setCar_html(String car_html) {
		this.car_html = car_html;
	}

	public String getCar_xml() {
		return car_xml;
	}

	public void setCar_xml(String car_xml) {
		this.car_xml = car_xml;
	}

	public String getCar_mark() {
		return car_mark;
	}

	public void setCar_mark(String car_mark) {
		this.car_mark = car_mark;
	}

	public String getPt_html() {
		return pt_html;
	}

	public void setPt_html(String pt_html) {
		this.pt_html = pt_html;
	}

	public String getPt_xml() {
		return pt_xml;
	}

	public void setPt_xml(String pt_xml) {
		this.pt_xml = pt_xml;
	}

	public String getPt_mark() {
		return pt_mark;
	}

	public void setPt_mark(String pt_mark) {
		this.pt_mark = pt_mark;
	}

	public String getRid() {
		return rid;
	}

	public String getCid() {
		return cid;
	}
		
}
