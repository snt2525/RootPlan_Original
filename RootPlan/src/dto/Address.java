package dto;

public class Address {
   //����, �浵, ���ּ�, ��
   private double lat; //y
   private double lng; //x
   private String address;
   private String Si;
   
   public Address(double lat, double lng, String address,String si) {
      this.lat = lat; 
      this.lng = lng;
      this.address = address;
      this.Si = si;
   }
    
   public double getLat() {
      return lat;
   }
   
   public double getLng() {
      return lng;
   }
   
   public String getAddress() {
      return address;
   }
   
   public String getSi() {
      return Si;
   }   
}