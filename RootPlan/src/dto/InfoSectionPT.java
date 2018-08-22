package dto;

import java.util.LinkedList;

public class InfoSectionPT {
	private int trafficType; // trafficType, 1-지하철,2-버스
	private String startStation; // startName
	private String endStation; // endName
	private int sectionDistance; // distance
	private int sectionTime; // sectionTime
	private String subwayLine; // name, 지하철 노선명(지하철만)
	private LinkedList<String> busNoList; //버스 여러개 여러개
	
	public InfoSectionPT() {
		busNoList = new LinkedList<String>();
	}

	public int getBusNoListSize() {
		return busNoList.size();
	}
	public void addBusNoList(String item) {
		busNoList.add(item);
	}
	public String getBusNoList(int idx) {
		return busNoList.get(idx);
	}
	public int getTrafficType() {
		return trafficType;
	}

	public void setTrafficType(int trafficType) {
		this.trafficType = trafficType;
	}

	public String getStartStation() {
		return startStation;
	}

	public void setStartStation(String startStation) {
		this.startStation = startStation;
	}

	public String getEndStation() {
		return endStation;
	}

	public void setEndStation(String endStation) {
		this.endStation = endStation;
	}

	public int getSectionDistance() {
		return sectionDistance;
	}

	public void setSectionDistance(int sectionDistance) {
		this.sectionDistance = sectionDistance;
	}

	public int getSectionTime() {
		return sectionTime;
	}

	public void setSectionTime(int sectionTime) {
		this.sectionTime = sectionTime;
	}

	public String getSubwayLine() {
		return subwayLine;
	}

	public void setSubwayLine(String subwayLine) {
		this.subwayLine = subwayLine;
	}

	public LinkedList<String> getBusNoList() {
		return busNoList;
	}

	public void setBusNoList(LinkedList<String> busNoList) {
		this.busNoList = busNoList;
	}
	
	
}
