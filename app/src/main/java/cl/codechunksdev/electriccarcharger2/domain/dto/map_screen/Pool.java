package cl.codechunksdev.electriccarcharger2.domain.dto.map_screen;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolDetailStation;

public class Pool {
   private final String id;
   private final String name;
   private final String address;
   private final double latitude;
   private final double longitude;
   private List<PoolDetailStation> stations;
   private Marker marker;

   public Pool(String id, String name, String address, double latitude, double longitude) {
      this.id = id;
      this.name = name;
      this.address = address;
      this.latitude = latitude;
      this.longitude = longitude;
      this.stations = new ArrayList<>();
   }

   public String getId() {
      return this.id;
   }

   public void setMarker(Marker marker) {
      this.marker = marker;
   }

   public Marker getMarker() {
      return this.marker;
   }

   public void setStations(List<PoolDetailStation> stations) {
      this.stations = stations;
   }

   public String getName() {
      return this.name;
   }

   public List<PoolDetailStation> getStations() {
      return this.stations;
   }

   public String getAddress() {
      return this.address;
   }

   public LatLng getLatLng() {
      return new LatLng(this.latitude, this.longitude);
   }

   public int getStationsLen() {
      return this.stations.size();
   }

   public int getConnectorsLen() {
      int count = 0;
      for (int i = 0; i < this.stations.size(); i++) {
         count += this.stations.get(i).getConnectorsLen();
      }
      return count;
   }

   public int getConnectorsAvailables() {
      int count = 0;
      for (int i = 0; i < this.stations.size(); i++) {
         count += this.stations.get(i).connectorsAvailables();
      }
      return count;
   }

   public boolean isPoolOffline() {
      int count = 0;
      for (int i = 0; i < this.stations.size(); i++) {
         if (this.stations.get(i).isStationOffline())
            count++;
      }

      return count == this.stations.size();
   }

}
