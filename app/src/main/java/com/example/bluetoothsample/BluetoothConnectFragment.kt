import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.bluetoothsample.R
import kotlinx.android.synthetic.main.fragment_bluetooth_connect.*

class BluetoothConnect : Fragment() {

    companion object {
        private const val TAG = "FirstFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bluetooth_connect, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toSecondButton = view.findViewById<Button>(R.id.button_connection_completed)
        toSecondButton.setOnClickListener{
            Log.d(TAG, "tos Button pressed!")
//            val secondFragment = MyPageFragment()
//            val fragmentTransaction = fragmentManager?.beginTransaction()
//            fragmentTransaction?.addToBackStack(null)
//            fragmentTransaction?.replace(R.id.fragment_container, secondFragment)
//            fragmentTransaction?.commit()
        }
        button_connection_completed.setOnClickListener{
            title_connect.text = "嘘だけど　接続完了〜〜〜〜〜！"
        }
    }
}