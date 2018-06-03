package br.com.lravanelli.findpets.fragments

import android.app.AlertDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.lravanelli.findpets.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var MapPet: GoogleMap

    override fun onMapReady(map: GoogleMap) {
        MapPet = map



        MapPet.clear()

        val geoCoder = Geocoder(context)
        var address : List<Address>?

        address = geoCoder.getFromLocationName("09371-065", 1)

        if(address.isNotEmpty()) {
            val location = address[0]

            adicionarMarcador(location.latitude, location.longitude, "Última localização")
        } else {
            var alert = AlertDialog.Builder(context).create()
            alert.setTitle("Fudeu")
            alert.setMessage("Endereço nao encontrado")

            alert.setCancelable(false)
            alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", {
                dialogInterface, inteiro ->
                alert.dismiss()
            })

            alert.show()
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
        val sydney = LatLng(latitude, longitude)
        MapPet.addMarker(MarkerOptions()
                .position(sydney)
                .title(title))
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_location))) /*modificar o marcardor*/

        //para focar
        MapPet.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16f))
    }




}
