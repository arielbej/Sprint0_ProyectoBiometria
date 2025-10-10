
package com.example.aabejgom.bltea_alumnos;
// ------------------------------------------------------------------
// ------------------------------------------------------------------

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class MainActivity extends AppCompatActivity {

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private static final String ETIQUETA_LOG = "BEACON";

    private static final int CODIGO_PETICION_PERMISOS = 11223344;

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private BluetoothLeScanner elEscanner;

    private ScanCallback callbackDelEscaneo = null;

    private TramaIBeacon ultimaTramaRecibida=null;

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private void buscarTodosLosDispositivosBTLE() {
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empieza ");

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): instalamos scan callback ");

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult( int callbackType, ScanResult resultado ) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanResult() ");

                mostrarInformacionDispositivoBTLE( resultado );
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanFailed() ");

            }
        };

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empezamos a escanear ");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.elEscanner.startScan( this.callbackDelEscaneo);

    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private void mostrarInformacionDispositivoBTLE( ScanResult resultado ) {

        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();

        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " ****** DISPOSITIVO DETECTADO BTLE ****************** ");
        Log.d(ETIQUETA_LOG, " ****************************************************");
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this.peekAvailableContext()), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d(ETIQUETA_LOG, " nombre = " + bluetoothDevice.getName());
        Log.d(ETIQUETA_LOG, " toString = " + bluetoothDevice.toString());

        /*
        ParcelUuid[] puuids = bluetoothDevice.getUuids();
        if ( puuids.length >= 1 ) {
            //Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].getUuid());
           // Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].toString());
        }*/

        Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        Log.d(ETIQUETA_LOG, " rssi = " + rssi );

        Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
        Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

        TramaIBeacon tib = new TramaIBeacon(bytes);

        Log.d(ETIQUETA_LOG, " ----------------------------------------------------");
        Log.d(ETIQUETA_LOG, " prefijo  = " + Utilidades.bytesToHexString(tib.getPrefijo()));
        Log.d(ETIQUETA_LOG, "          advFlags = " + Utilidades.bytesToHexString(tib.getAdvFlags()));
        Log.d(ETIQUETA_LOG, "          advHeader = " + Utilidades.bytesToHexString(tib.getAdvHeader()));
        Log.d(ETIQUETA_LOG, "          companyID = " + Utilidades.bytesToHexString(tib.getCompanyID()));
        Log.d(ETIQUETA_LOG, "          iBeacon type = " + Integer.toHexString(tib.getiBeaconType()));
        Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + Integer.toHexString(tib.getiBeaconLength()) + " ( " + tib.getiBeaconLength() + " ) ");
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToHexString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToHexString(tib.getMajor()) + "( " + Utilidades.bytesToInt(tib.getMajor()) + " ) ");
        Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToHexString(tib.getMinor()) + "( " + Utilidades.bytesToInt(tib.getMinor()) + " ) ");
        Log.d(ETIQUETA_LOG, " txPower  = " + Integer.toHexString(tib.getTxPower()) + " ( " + tib.getTxPower() + " )");
        Log.d(ETIQUETA_LOG, " ****************************************************");

        this.ultimaTramaRecibida=tib;

    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private void buscarEsteDispositivoBTLE(final String dispositivoBuscado) {
        Log.d(ETIQUETA_LOG, "buscarEsteDispositivoBTLE(): empieza");

        // ✅ 1. Verificar permisos antes de escanear
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.d(ETIQUETA_LOG, "buscarEsteDispositivoBTLE(): solicitando permisos Android 12+");

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        CODIGO_PETICION_PERMISOS
                );
                return;
            }
        } else {
            // ✅ Android 11 o menor
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.d(ETIQUETA_LOG, "buscarEsteDispositivoBTLE(): solicitando permisos Android < 12");

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        CODIGO_PETICION_PERMISOS
                );
                return;
            }
        }

        // ✅ 2. Detener escaneo previo si ya había uno activo
        detenerBusquedaDispositivosBTLE();

        Log.d(ETIQUETA_LOG, "buscarEsteDispositivoBTLE(): instalamos scan callback");

        // ✅ 3. Configurar callback del escaneo
        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                mostrarInformacionDispositivoBTLE(resultado);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                for (ScanResult result : results) {
                    mostrarInformacionDispositivoBTLE(result);
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.e(ETIQUETA_LOG, "buscarEsteDispositivoBTLE(): onScanFailed() con error: " + errorCode);
            }
        };

        // ✅ 4. Crear filtro para el nombre del dispositivo buscado
        ScanFilter filtro = new ScanFilter.Builder()
                .setDeviceName(dispositivoBuscado)
                .build();

        List<ScanFilter> filtros = new ArrayList<>();
        filtros.add(filtro);

        // ✅ 5. Configurar el modo de escaneo
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

        // ✅ 6. Iniciar escaneo
        Log.d(ETIQUETA_LOG, "buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado);
        elEscanner.startScan(filtros, settings, callbackDelEscaneo);
    }



    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private void detenerBusquedaDispositivosBTLE() {

        if ( this.callbackDelEscaneo == null ) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.elEscanner.stopScan( this.callbackDelEscaneo );
        this.callbackDelEscaneo = null;

    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonBuscarDispositivosBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton buscar dispositivos BTLE Pulsado" );
        this.buscarTodosLosDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonBuscarNuestroDispositivoBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton nuestro dispositivo BTLE Pulsado" );
        //this.buscarEsteDispositivoBTLE( Utilidades.stringToUUID( "EPSG-GTI-PROY-3A" ) );

        //this.buscarEsteDispositivoBTLE( "EPSG-GTI-PROY-3A" );
        this.buscarEsteDispositivoBTLE( "ARIEL" );

    } // ()


    public void guardarMedicion(View v) {

        String guardar_log= "GUARDAR_MEDICION";
        try {
            if (ultimaTramaRecibida == null) {
                Log.e(guardar_log, "No hay ninguna trama iBeacon recibida todavía.");
                return;
            }

            // Usar el nuevo método sin signo
            int major = Utilidades.bytesToInt(ultimaTramaRecibida.getMajor());
            int minor = Utilidades.bytesToInt(ultimaTramaRecibida.getMinor());

            // Extraer el tipo de medición (byte alto del major)
            int id_medicion = (major >> 8) & 0xFF;

            // Extraer el contador (byte bajo del major)
            int contador = major & 0xFF;

            // El valor está en el minor
            int valor_contador = minor;

            Log.d(guardar_log, "===========================================");
            Log.d(guardar_log, "Major completo: " + major + " (0x" + Integer.toHexString(major) + ")");
            Log.d(guardar_log, "Tipo de medición: " + id_medicion + " (esperado: 11=CO2, 12=TEMP)");
            Log.d(guardar_log, "Contador: " + contador);
            Log.d(guardar_log, "Minor (valor): " + valor_contador);
            Log.d(guardar_log, "===========================================");

            // Guardar usando el tipo de medición como id_sensor
            int id_sensor = id_medicion;

            new Thread(() -> {
                try {
                    LogicaFake logica = new LogicaFake("http://192.168.18.199:8000/");
                    logica.insertarMedicion(id_sensor, valor_contador);
                } catch (Exception e) {
                    Log.e("ERROR", "Error al insertar medición", e);
                }
            }).start();

        } catch (Exception e) {
            Log.e("ERROR", "Error general en guardarMedicion", e);
        }
    }



    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonDetenerBusquedaDispositivosBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton detener busqueda dispositivos BTLE Pulsado" );
        this.detenerBusquedaDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private void inicializarBlueTooth() {
        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos adaptador BT ");

        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitamos adaptador BT ");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        bta.enable();

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitado =  " + bta.isEnabled() );

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): estado =  " + bta.getState() );

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos escaner btle ");

        this.elEscanner = bta.getBluetoothLeScanner();

        if ( this.elEscanner == null ) {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): Socorro: NO hemos obtenido escaner btle  !!!!");

        }

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): voy a perdir permisos (si no los tuviera) !!!!");

        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION},
                    CODIGO_PETICION_PERMISOS);
        }
        else {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): parece que YA tengo los permisos necesarios !!!!");

        }
    } // ()


    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(ETIQUETA_LOG, " onCreate(): empieza ");

        inicializarBlueTooth();

        Log.d(ETIQUETA_LOG, " onCreate(): termina ");

    } // onCreate()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CODIGO_PETICION_PERMISOS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(ETIQUETA_LOG, "onRequestPermissionsResult(): permisos concedidos. Reintentando búsqueda...");
                buscarEsteDispositivoBTLE("ARIEL"); // 👈 vuelve a intentar con tu dispositivo
            } else {
                Log.d(ETIQUETA_LOG, "onRequestPermissionsResult(): permisos NO concedidos.");
            }
        }
    }


} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------


