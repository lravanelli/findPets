package br.com.lravanelli.findpets.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.com.lravanelli.findpets.R
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_sobre.view.*

class SobreFragment : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_sobre, container, false)

        view.etKeyFirebase.setText(FirebaseInstanceId.getInstance().token)

        return view
    }



}
