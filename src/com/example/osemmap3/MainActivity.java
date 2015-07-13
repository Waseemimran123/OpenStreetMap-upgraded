package com.example.osemmap3;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;


public class MainActivity extends Activity {
	private MapView myOpenMapView;
	 private MapController myMapController;
	  static PathOverlay myPath ;
	 LocationManager locationManager;
	 
	 ArrayList<OverlayItem> overlayItemArray;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 myOpenMapView = (MapView)findViewById(R.id.openmapview);
		 myOpenMapView .setTileSource(TileSourceFactory.MAPQUESTOSM);
		 myOpenMapView .setBuiltInZoomControls(true);
		 myOpenMapView .setMultiTouchControls(true);
	        myMapController = myOpenMapView.getController();
	        myMapController.setZoom(25);
	        myPath= new PathOverlay(Color.RED, this);
	        //  map.setBuiltInZoomControls(true);
	        //  map.setMultiTouchControls(true);
	      //--- Create Overlay
	        overlayItemArray = new ArrayList<OverlayItem>();
	        
	        DefaultResourceProxyImpl defaultResourceProxyImpl 
	         = new DefaultResourceProxyImpl(this);
	        MyItemizedIconOverlay myItemizedIconOverlay 
	         = new MyItemizedIconOverlay(
	           overlayItemArray, null, defaultResourceProxyImpl);
	        myOpenMapView.getOverlays().add(myItemizedIconOverlay);
	        //---
	        
	        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	        
	        //for demo, getLastKnownLocation from GPS only, not from NETWORK
	        Location lastLocation 
	         = locationManager.getLastKnownLocation(
	           LocationManager.GPS_PROVIDER);
	        if(lastLocation != null){
	         updateLoc(lastLocation);
	        }
	        
	        //Add Scale Bar
	        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(this);
	        myOpenMapView.getOverlays().add(myScaleBarOverlay);
	    }
	    
	    @Override
	 protected void onResume() {
	  // TODO Auto-generated method stub
	  super.onResume();
	  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, myLocationListener);
	  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 50, myLocationListener);
	 }

	 @Override
	 protected void onPause() {
	  // TODO Auto-generated method stub
	  super.onPause();
	  locationManager.removeUpdates(myLocationListener);
	 }

	 private void updateLoc(Location loc){
	     GeoPoint locGeoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
	     myMapController.setCenter(locGeoPoint);
	     
	     setOverlayLoc(loc);
	     
	     myOpenMapView.invalidate();
	     myPath.addPoint(locGeoPoint);
	     myOpenMapView.getOverlays().add(myPath);
	    }
	 
	 private void setOverlayLoc(Location overlayloc){
	  GeoPoint overlocGeoPoint = new GeoPoint(overlayloc);
	  //---
	     overlayItemArray.clear();
	     
	     OverlayItem newMyLocationItem = new OverlayItem(
	       "My Location", "My Location", overlocGeoPoint);
	     overlayItemArray.add(newMyLocationItem);
	     //---
	 }
	    
	    private LocationListener myLocationListener
	    = new LocationListener(){

	  @Override
	  public void onLocationChanged(Location location) {
	   // TODO Auto-generated method stub
	   updateLoc(location);
	  }

	  @Override
	  public void onProviderDisabled(String provider) {
	   // TODO Auto-generated method stub
	   
	  }

	  @Override
	  public void onProviderEnabled(String provider) {
	   // TODO Auto-generated method stub
	   
	  }

	  @Override
	  public void onStatusChanged(String provider, int status, Bundle extras) {
	   // TODO Auto-generated method stub
	   
	  }
	     
	    };
	    
	    private class MyItemizedIconOverlay extends ItemizedIconOverlay<OverlayItem>{

	  public MyItemizedIconOverlay(
	    List<OverlayItem> pList,
	    org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
	    ResourceProxy pResourceProxy) {
	   super(pList, pOnItemGestureListener, pResourceProxy);
	   // TODO Auto-generated constructor stub
	  }

	  @Override
	  public void draw(Canvas canvas, MapView mapview, boolean arg2) {
	   // TODO Auto-generated method stub
	   super.draw(canvas, mapview, arg2);
	   
	   if(!overlayItemArray.isEmpty()){
	    
	    //overlayItemArray have only ONE element only, so I hard code to get(0)
	    GeoPoint in = overlayItemArray.get(0).getPoint();
	    
	    Point out = new Point();
	    mapview.getProjection().toPixels(in, out);
	    
	    Bitmap bm = BitmapFactory.decodeResource(getResources(), 
	      R.drawable.ic_launcher);
	    canvas.drawBitmap(bm, 
	      out.x - bm.getWidth()/20,  //shift the bitmap center
	      out.y - bm.getHeight()/20,  //shift the bitmap center
	      null);
	   }
	  }

	  @Override
	  public boolean onSingleTapUp(MotionEvent event, MapView mapView) {
	   // TODO Auto-generated method stub
	   //return super.onSingleTapUp(event, mapView);
	   return true;
	  }
	    }

	}