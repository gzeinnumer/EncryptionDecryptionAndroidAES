package com.gzeinnumer.encryptiondecryptionandroidaes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.gzeinnumer.encryptiondecryptionandroidaes.databinding.ActivityMainBinding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        binding.btnEn.setOnClickListener(v -> {
            binding.decrypted.setText("");
            String encryptedValue = binding.encrypted.getText().toString();
            String key = binding.edKey.getText().toString();
            try {
                String en = encrypt(encryptedValue, key);
                binding.decrypted.setText(en);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.btnDe.setOnClickListener(v -> {
            binding.encrypted.setText("");
            String decryptedValue = binding.decrypted.getText().toString();
            String key = binding.edKey.getText().toString();
            String en = null;
            try {
                en = decrypt(decryptedValue, key);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Wrong Password : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            binding.encrypted.setText(en);
        });
    }

    private String decrypt(String outputString, String key) throws Exception {
        SecretKeySpec keySpec = generateKey(key);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue); //decryptedValue
    }

    private String encrypt(String data, String key) throws Exception {
        SecretKeySpec keySpec = generateKey(key);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.encodeToString(encVal, Base64.DEFAULT); //encrypted
    }

    private SecretKeySpec generateKey(String key) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = key.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] keySecurity = digest.digest();
        return new SecretKeySpec(keySecurity, AES);
    }
}