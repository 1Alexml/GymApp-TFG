package com.gymapp.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * Genera un Bitmap con un código QR a partir de un texto.
 * Se usa en HomeFragment para mostrar el "carnet" de entrada del cliente.
 */
public class QRGenerator {

    public static Bitmap generar(String contenido, int tamPx) {
        if (contenido == null || contenido.isEmpty()) return null;

        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN, 1);

            BitMatrix matriz = new QRCodeWriter()
                    .encode(contenido, BarcodeFormat.QR_CODE, tamPx, tamPx, hints);

            Bitmap bmp = Bitmap.createBitmap(tamPx, tamPx, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < tamPx; x++) {
                for (int y = 0; y < tamPx; y++) {
                    bmp.setPixel(x, y, matriz.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;

        } catch (WriterException e) {
            return null;
        }
    }
}
