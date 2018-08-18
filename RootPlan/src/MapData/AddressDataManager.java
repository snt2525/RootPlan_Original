package MapData;

import java.util.LinkedList;

import ShortestPath.SetData;

public class AddressDataManager {
   public static LinkedList<Address> addressData; //��� �����͸� �����Ѵ�.   
   int size = 0;
   public AddressDataManager(){
      addressData = new LinkedList<Address>();      
   }
   public int listSize() {	  
      return addressData.size();
   }
   
   public String addData(Address data) { //�����͸� �����Ѵ�
	  for(Address item : addressData) {
		  if(data.getAddress().equals(item.getAddress())) {
			  return "-1";
		  }
	  }
      addressData.add(data);
      String result = "";
      result += "<AddressData>";
      result += "<address>"+data.getAddress()+"</address>";   
      result += "</AddressData>";
      return result;
   }
   
   public int deleteData(int index) {
      addressData.remove(index);
      return 1;
   }
   
   public String callAddress(int index) {
      String result = "";
      result += "<AddressData>";
      result += "<address>"+addressData.get(index).getAddress()+"</address>";   
      result += "</AddressData>";
      return result;
   }
   
   public String callLatLng(SetData sd) {
      int size = addressData.size();
      String result = "";      
      result += "<AddressData>";
      for(int i =0;i< size; i++) {
         result += "<LatLng>";
         result += "<start>"+Integer.toString(sd.GetStartData())+"</start>";
         result += "<lat>"+addressData.get(i).getLat()+"</lat>";
         result += "<lng>"+addressData.get(i).getLng()+"</lng>";
         result += "</LatLng>";
      }
      result += "</AddressData>";
      return result;
   }
   
   public String callAllAddress() {
      int size = addressData.size();
      String result = "";
      if(size == 0) 
         return result;   
      
      result += "<AddressData>";
      for(int i =0;i<size;i++) {
         result += "<Address>";
         result += "<no>"+Integer.toString(i+1)+"</no>";
         result += "<num>"+Integer.toString(i)+"</num>";
         result += "<data>"+addressData.get(i).getAddress()+"</data>";
         result += "<lat>"+addressData.get(i).getLat()+"</lat>";   
         result += "<lng>"+addressData.get(i).getLng()+"</lng>";   
         result += "</Address>";
      }
      result += "</AddressData>";
      
      return result;
   }
   public int resetData() {
      addressData = new LinkedList<Address>();
      return 1;
   }
   
   public String callAllAddress_StartData(SetData sd) {
      int size = addressData.size();
      String result = "";
      if(size == 0) 
         return result;   
      result += "<AddressData>";
      for(int i =0;i<size;i++) {
         result += "<Address>";
         result += "<no>"+Integer.toString(i+1)+"</no>";
         result += "<start>"+Integer.toString(sd.GetStartData())+"</start>";
         result += "<num>"+Integer.toString(i)+"</num>";
         result += "<data>"+addressData.get(i).getAddress()+"</data>";
         result += "<lat>"+addressData.get(i).getLat()+"</lat>";   
         result += "<lng>"+addressData.get(i).getLng()+"</lng>";   
         result += "</Address>";
      }
      result += "</AddressData>";
      return result;
   }
   
   public String callAllLatLng() {
      String result = "";
      int size = addressData.size();
      result += "<LatLngData>";
      for(int i =0;i<size;i++) {
         result += "<LatLng>";
         result += "<Size>" + Integer.toString(size) + "</Size>";
         result += "<lat>" + addressData.get(i).getLat() +"</lat>";
         result += "<lng>" + addressData.get(i).getLng() +"</lng>";
         result += "</LatLng>";
      }
      result += "</LatLngData>";
      
      return result;
   }
   public LinkedList<Address> getList(){
      return addressData;
   }
}