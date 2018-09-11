package ar.edu.utn.frsf.isi.dam.bancolab01;

import android.content.res.Resources;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import ar.edu.utn.frsf.dam.bancolab01.modelo.Cliente;
import ar.edu.utn.frsf.dam.bancolab01.modelo.Moneda;
import ar.edu.utn.frsf.dam.bancolab01.modelo.PlazoFijo;

public class MainActivity extends AppCompatActivity {
    private PlazoFijo pf;
    private Cliente cliente;
    private Button btnHacerPlazoFijo;
    private EditText edtMontoAInvetir;
    private EditText edtMail;
    private EditText edtCUIT;
    private TextView tvMail;
    private TextView tvCUIT;
    private TextView tvMonedaDeInversion;
    private TextView tvMonto;
    private TextView tvCantidadDiasInversion;
    private TextView tvDiasSeleccionados;
    private TextView tvIntereses;
    private TextView tvDetalle;
    //private RadioGroup rgMoneda;
    private RadioButton rbDolar;
    private RadioButton rbPeso;
    private SeekBar sbDiasInversion;
    private Switch swAvisoVencimiento;
    private ToggleButton tbAcreditarCuenta;
    private CheckBox cbTerminosYCondiciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Resources res = getResources();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pf = new PlazoFijo(res.getStringArray(R.array.tasas));
        cliente = new Cliente();

        edtMontoAInvetir = (EditText) findViewById(R.id.editTextMonto);
        edtMail = (EditText) findViewById(R.id.editTextMail);
        edtCUIT = (EditText) findViewById(R.id.editTextCUIT);
        tvMail = (TextView) findViewById(R.id.textViewMail);
        tvCUIT = (TextView) findViewById(R.id.textViewCUIT);
        tvMonedaDeInversion = (TextView) findViewById(R.id.textViewMoneda);
        tvMonto = (TextView) findViewById(R.id.textViewMonto);
        tvCantidadDiasInversion = (TextView) findViewById(R.id.textViewDiasInversion);
        tvDiasSeleccionados = (TextView) findViewById(R.id.textViewDiasSeleccionados);
        tvIntereses = (TextView) findViewById(R.id.textViewIntereses);
        tvDetalle = (TextView) findViewById(R.id.textViewDetalle);
        rbDolar= (RadioButton) findViewById(R.id.radioButtonDolar);
        rbPeso= (RadioButton) findViewById(R.id.radioButtonPeso);
        sbDiasInversion= (SeekBar) findViewById(R.id.seekBarDiasInversion);
        tbAcreditarCuenta = (ToggleButton) findViewById(R.id.toggleButtonAcreditarCuenta);
        cbTerminosYCondiciones = (CheckBox) findViewById(R.id.checkBoxTerminosYCondiciones);
        btnHacerPlazoFijo = (Button) findViewById(R.id.buttonHacerPlazoFijo);
        swAvisoVencimiento = findViewById(R.id.switchAvisoVencimiento);

        sbDiasInversion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               // String dias=progress + "";
                 // String dias=R.string.lblDiasDeLaInversion + progress + "";
                tvDiasSeleccionados.setText( Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        cbTerminosYCondiciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                btnHacerPlazoFijo.setEnabled(isChecked);
                if(!isChecked){
                    Toast.makeText(getApplicationContext(),"Se deben aceptar Términos y condiciones para continuar.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnHacerPlazoFijo.setOnClickListener(new View.OnClickListener() {
            boolean bandera=false;
            String mensaje ="";
            @Override
            public void onClick(View v) {
                if(edtMail.getText().length()<=0){
                    tvDetalle.setTextColor(Color.red(0x1));
                    bandera=false;
                    mensaje+="Email Invalido\n";

                }
                if(edtCUIT.getText().length()<=0){
                    tvDetalle.setTextColor(Color.red(0x1));
                    bandera=true;
                    mensaje+="CUIT/CUIL Invalido\n";
                }
                if(Double.valueOf(edtMontoAInvetir.getText().toString())<=0){
                    tvDetalle.setTextColor(Color.red(0x1));
                    bandera=true;
                    mensaje+="Monto Invalido\n";
                }
                if(sbDiasInversion.getProgress()<10){
                    tvDetalle.setTextColor(Color.red(0x1));
                    bandera=true;
                    mensaje+="Dias Invalido\n";
                }
                if(bandera){
                    Toast.makeText(getApplicationContext(), "Los datos no son correctos",Toast.LENGTH_SHORT).show();
                    tvDetalle.setText(mensaje);
                }else{
                    //cliente=new Cliente();
                    cliente.setMail(""+tvMail.getText());

                    pf.setCliente(cliente);
                    pf.setAvisarVencimiento(swAvisoVencimiento.isChecked());
                    pf.setDias(sbDiasInversion.getProgress());
                    if(rbDolar.isChecked())
                    pf.setMoneda(Moneda.DOLAR);
                    else if(rbPeso.isChecked())pf.setMoneda(Moneda.PESO);
                    pf.setMonto(Double.valueOf(edtMontoAInvetir.getText().toString()));
                    pf.setRenovarAutomaticamente(tbAcreditarCuenta.isChecked());


                    tvIntereses.setText("Intereses: "+pf.calcularTasas(sbDiasInversion.getProgress())+"");
                //    tvDetalle.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tvDetalle.setText(pf.toString());

                }
            }
        });


    }
}
