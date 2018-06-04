package br.com.lravanelli.findpets.fragments

import android.app.AlertDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.lravanelli.findpets.R
import br.com.lravanelli.findpets.model.Pet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_pet.*
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var MapPet: GoogleMap

    override fun onMapReady(map: GoogleMap) {
        MapPet = map


        MapPet.clear()

        val geoCoder = Geocoder(context)
        var address : List<Address>?

        val pet: Pet = arguments!!.getParcelable<Pet>("pet")

        address = geoCoder.getFromLocationName(pet.cep, 1)


        if(address.isNotEmpty()) {
            val location = address[0]
            if(address.size > 0) {
                adicionarMarcador(location.latitude, location.longitude, getString(R.string.last_location))
            } else {
                Log.d("map", "location error")
            }
        } else {
            Log.d("map", "location error")
        }

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        MapPet.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        MapPet.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapPet) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    fun adicionarMarcador(latitude: Double, longitude: Double, title : String) {
        // Add a marker in Sydney and move the camera
        val location = LatLng(latitude, longitude)
        MapPet.addMarker(MarkerOptions()
                .position(location)
                .title(title))
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_location))) /*modificar o marcardor*/

        //para focar
        MapPet.moveCamera(CameraUpdateFactory.newLatLngZoom(location,16f))
    }




}
